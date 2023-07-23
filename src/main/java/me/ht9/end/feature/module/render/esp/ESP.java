package me.ht9.end.feature.module.render.esp;

import me.ht9.end.event.bus.annotation.SubscribeEvent;
import me.ht9.end.event.events.DrawDefaultBackgroundEvent;
import me.ht9.end.event.events.RenderOverlaysEvent;
import me.ht9.end.event.events.RenderWorldPassEvent;
import me.ht9.end.event.events.UpdateEvent;
import me.ht9.end.feature.module.Module;
import me.ht9.end.feature.module.annotation.Aliases;
import me.ht9.end.feature.module.annotation.Description;
import me.ht9.end.feature.module.setting.Setting;
import me.ht9.end.mixin.accessors.IEntityRenderer;
import me.ht9.end.render.Render3d;
import me.ht9.end.shader.Framebuffer;
import me.ht9.end.shader.GlStateManager;
import me.ht9.end.shader.renderer.OpenGlHelper;
import me.ht9.end.util.RenderUtils;
import net.minecraft.src.*;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
@Aliases(value = { "Shaders" })
@Description(value = "Render outlines over things.")
public final class ESP extends Module
{
    private static final ESP instance = new ESP();

    private final Setting<ColorMode> colorMode = new Setting<>("ColorMode", ColorMode.Rainbow);
    private final Setting<Integer> red = new Setting<>("Red", 0, 150, 255, () -> this.colorMode.getValue() == ColorMode.Custom);
    private final Setting<Integer> green = new Setting<>("Green", 0, 50, 255, () -> this.colorMode.getValue() == ColorMode.Custom);
    private final Setting<Integer> blue = new Setting<>("Blue", 0, 255, 255, () -> this.colorMode.getValue() == ColorMode.Custom);
    private final Setting<Boolean> fill = new Setting<>("Fill", true);

    private final Setting<Boolean> players = new Setting<>("Players", true);
    private final Setting<Boolean> armor = new Setting<>("Armor", false, this.players::getValue);
    private final Setting<Boolean> heldItem = new Setting<>("HeldItem", true, this.players::getValue);
    private final Setting<Boolean> arrows = new Setting<>("StuckArrows", false, this.players::getValue);
    private final Setting<Boolean> cape = new Setting<>("Cape", true, this.players::getValue);

    private final Setting<Boolean> storage = new Setting<>("Chests", true);
    private final Setting<Double> storageRange = new Setting<>("ChestRange", 6.0D, 24.0D, 36.0D, this.storage::getValue);

    private final Setting<Boolean> items = new Setting<>("Items", true);
    private final Setting<Double> itemsRange = new Setting<>("ItemsRange", 6.0D, 18.0D, 36.0D, this.items::getValue);
    private final Setting<Boolean> hands = new Setting<>("Hands", true);
    private final Setting<Boolean> particles = new Setting<>("Particles", true);
    private final Setting<Boolean> hud = new Setting<>("Hud", true);
    private final Setting<Boolean> guis = new Setting<>("GUIs", true);

    private final ArrayList<Integer[]> positions = new ArrayList<>();

    private boolean listenForLayers = false;
    private boolean cancelGuiBackground = false;
    private boolean cancelRenderOverlays = false;
    private boolean renderingEntity = false;

    private ESP()
    {
    }

    @SubscribeEvent
    public void onRenderWorldPass(RenderWorldPassEvent event)
    {
        if (mc.thePlayer == null || mc.theWorld == null)
        {
            return;
        }

        if (this.guis.getValue())
        {
            if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiGameOver))
            {
                mc.entityRenderer.method_1843();
                mc.currentScreen.drawWorldBackground(0);
            }
        }

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        OutlineShader shader = OutlineShader.getInstance();

        Framebuffer framebuffer = shader.getFramebuffer();
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);

        if (this.guis.getValue())
        {
            this.renderGUI(event.getPartialTicks());
        }

        if (this.hud.getValue())
        {
            this.renderHud(event.getPartialTicks());
        }

        if (this.particles.getValue())
        {
            this.renderParticles(event.getPartialTicks());
        }

        if (this.players.getValue() || this.items.getValue())
        {
            this.renderEntities(event.getPartialTicks());
        }

        if (this.storage.getValue())
        {
            this.renderStorages(event.getPartialTicks());
        }

        if (this.hands.getValue())
        {
            this.renderHand(event.getPartialTicks());
        }

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        mc.entityRenderer.method_1843();
        RenderUtils.framebuffer.bindFramebuffer(true);
        GL20.glUseProgram(shader.getProgramId());

        GL20.glUniform2f(shader.getUniform("resolution"), (float) new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight).getScaledWidth() * 2.5F, (float) new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight).getScaledHeight() * 2.5F);
        GL20.glUniform1f(shader.getUniform("time"), ((System.currentTimeMillis() * 3) % 1000000) / 5000.0F);
        switch (this.colorMode.getValue())
        {
            case Sync:
            {
//                GL20.glUniform1f(shader.getUniform("red"), ClickGUI.getInstance().red.getValue() / 255.0F);
//                GL20.glUniform1f(shader.getUniform("green"), ClickGUI.getInstance().green.getValue() / 255.0F);
//                GL20.glUniform1f(shader.getUniform("blue"), ClickGUI.getInstance().blue.getValue() / 255.0F);
                break;
            }
            case Custom:
            {
                GL20.glUniform1f(shader.getUniform("red"), this.red.getValue() / 255.0F);
                GL20.glUniform1f(shader.getUniform("green"), this.green.getValue() / 255.0F);
                GL20.glUniform1f(shader.getUniform("blue"), this.blue.getValue() / 255.0F);
                break;
            }
        }
        GL20.glUniform1i(shader.getUniform("rainbow"), this.colorMode.getValue() == ColorMode.Rainbow ? 1 : 0);
        GL20.glUniform1f(shader.getUniform("fill"), this.fill.getValue() ? 1 : 0);

        RenderUtils.drawShader(framebuffer);
        GL20.glUseProgram(0);
        RenderUtils.framebuffer.bindFramebuffer(false);

        GL11.glDisable(GL11.GL_LINE_SMOOTH);

        GlStateManager.disableBlend();

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void onDrawDefaultBackground(DrawDefaultBackgroundEvent event)
    {
        if (mc.thePlayer == null || mc.theWorld == null)
        {
            return;
        }

        if (this.guis.getValue() && mc.currentScreen != null)
        {
            if (mc.currentScreen instanceof GuiGameOver)
            {
                if (this.cancelGuiBackground)
                {
                    event.setCancelled(true);
                }
            } else
            {
                event.setCancelled(true);
            }
        }
    }

    @SubscribeEvent
    public void onRenderOverlays(RenderOverlaysEvent event)
    {
        if (this.cancelRenderOverlays)
        {
            event.setCancelled(true);
        }
    }

    private void renderHud(float partialTicks)
    {
        if (!this.mc.gameSettings.hideGUI || mc.currentScreen != null)
        {
            ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            mc.entityRenderer.method_1843();

            GL11.glEnable(GL11.GL_ALPHA_TEST);

            mc.ingameGUI.renderGameOverlay(partialTicks, true, sr.getScaledWidth(), (sr.getScaledWidth() / 2) - 91);
        }
    }

    private void renderGUI(float partialTicks)
    {
        if (this.mc.currentScreen != null)
        {
            ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            final int k1 = Mouse.getX() * sr.getScaledWidth() / this.mc.displayWidth;
            final int l1 = sr.getScaledHeight() - Mouse.getY() * sr.getScaledHeight() / this.mc.displayHeight - 1;
            mc.entityRenderer.method_1843();
            this.cancelGuiBackground = true;
            mc.currentScreen.drawScreen(k1, l1, partialTicks);
            this.cancelGuiBackground = false;
        }
    }

    private void renderEntities(float partialTicks)
    {
        for (Object entity : mc.theWorld.loadedEntityList)
        {
            if (!entity.equals(mc.thePlayer))
            {
                this.renderEntityStatic((Entity) entity, partialTicks);
            }
//            if (entity instanceof EntityItem && items.getValue())
//            {
//                if (mc.thePlayer.getDistanceToEntity((Entity) entity) <= this.itemsRange.getValue())
//                {
//                    this.renderEntityStatic((Entity) entity, partialTicks);
//                }
//            } else if (entity instanceof EntityPlayer && this.players.getValue() && (!entity.equals(mc.thePlayer)))
//            {
//                this.listenForLayers = true;
//                this.renderingEntity = true;
//                this.renderEntityStatic((Entity) entity, partialTicks);
//                this.listenForLayers = false;
//                this.renderingEntity = false;
//            } else if (entity instanceof EntitySlime)
//            {
//                this.renderEntityStatic((Entity) entity, partialTicks);
//            } else if (entity instanceof EntitySlimeFX)
//            {
//                this.renderEntityStatic((Entity) entity, partialTicks);
//            } else if (entity instanceof EntityCow)
//            {
//                this.renderEntityStatic((Entity) entity, partialTicks);
//            } else if (entity instanceof EntityPig)
//            {
//                this.renderEntityStatic((Entity) entity, partialTicks);
//            } else if (entity instanceof EntitySheep)
//            {
//                this.renderEntityStatic((Entity) entity, partialTicks);
//            } else if (entity instanceof EntityChicken)
//            {
//                this.renderEntityStatic((Entity) entity, partialTicks);
//            } else if (entity instanceof EntitySkeleton)
//            {
//                this.renderEntityStatic((Entity) entity, partialTicks);
//            } else if (entity instanceof EntityZombie)
//            {
//                this.renderEntityStatic((Entity) entity, partialTicks);
//            } else if (entity instanceof EntityCreeper)
//            {
//                this.renderEntityStatic((Entity) entity, partialTicks);
//            } else if (entity instanceof EntitySpider)
//            {
//                this.renderEntityStatic((Entity) entity, partialTicks);
//            } else if (entity instanceof EntityGhast)
//            {
//                this.renderEntityStatic((Entity) entity, partialTicks);
//            } else if (entity instanceof EntityMinecart)
//            {
//                this.renderEntityStatic((Entity) entity, partialTicks);
//            }
        }
    }

    private void renderStorages(float partialTicks)
    {
        this.positions.clear();
        ((IEntityRenderer) mc.entityRenderer).invokeSetupCameraTransform(partialTicks, 0);
        // cme
        List<TileEntity> dup = new ArrayList<>(mc.theWorld.loadedTileEntityList);
        for (TileEntity entity : dup)
        {
            if (entity instanceof TileEntity)
            {
                TileEntityRenderer.instance.renderTileEntity(entity, partialTicks);
                double distance = mc.thePlayer.getDistanceSq(entity.xCoord, entity.yCoord, entity.zCoord);
                if (entity instanceof TileEntityChest)
                {
                    this.positions.add(new Integer[]{ entity.xCoord, entity.yCoord, entity.zCoord });
                    for (Integer[] pos : positions)
                    {
                        Render3d.drawShaderBox(
                                AxisAlignedBB.method_87(
                                        pos[0] - RenderManager.renderPosX,
                                        pos[1] - RenderManager.renderPosY,
                                        pos[2] - RenderManager.renderPosZ,
                                        pos[0] + 1 - RenderManager.renderPosX,
                                        pos[1] + 1 - RenderManager.renderPosY,
                                        pos[2] + 1 - RenderManager.renderPosZ
                                )
                        );
                    }
                }
            }
        }
    }

    private void renderHand(float partialTicks)
    {
        this.cancelRenderOverlays = true;
        ((IEntityRenderer) mc.entityRenderer).invokeRenderHand(partialTicks, 2);
        this.cancelRenderOverlays = false;
    }

    private void renderParticles(float partialTicks)
    {
        ((IEntityRenderer) mc.entityRenderer).invokeSetupCameraTransform(partialTicks, 0);
        mc.effectRenderer.renderParticles(mc.thePlayer, partialTicks);
    }

    public void disableLightmap()
    {
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public void enableLightmap()
    {
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        float f = 0.00390625F;
        GlStateManager.scale(0.00390625F, 0.00390625F, 0.00390625F);
        GlStateManager.translate(8.0F, 8.0F, 8.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.glTexParameteri(3553, 10241, 9729);
        GlStateManager.glTexParameteri(3553, 10240, 9729);
        GlStateManager.glTexParameteri(3553, 10242, 10496);
        GlStateManager.glTexParameteri(3553, 10243, 10496);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public void renderEntityStatic(Entity entityIn, float partialTicks)
    {
        if (entityIn.ticksExisted == 0)
        {
            entityIn.lastTickPosX = entityIn.posX;
            entityIn.lastTickPosY = entityIn.posY;
            entityIn.lastTickPosZ = entityIn.posZ;
        }

        double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
        double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
        double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
        float f = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;


        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderManager.instance.renderEntity(entityIn, partialTicks);
    }

    public static ESP getInstance()
    {
        return instance;
    }

    public enum ColorMode
    {
        Sync,
        Custom,
        Rainbow
    }
}
