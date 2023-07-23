package me.ht9.end.mixin.mixins;

import me.ht9.end.End;
import me.ht9.end.event.events.RenderWorldPassEvent;
import me.ht9.end.event.events.SetupFogEvent;
import me.ht9.end.util.Globals;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer implements Globals
{
    @Shadow public abstract void getMouseOver(float f);

    @Shadow public static int anaglyphField;

    @Shadow protected abstract void updateFogColor(float f);

    @Shadow protected abstract void setupCameraTransform(float f, int i);

    @Shadow protected abstract void setupFog(int i, float f);

    @Shadow private double cameraZoom;

    @Shadow protected abstract void renderRainSnow(float f);

    @Shadow protected abstract void method_1845(float f, int i);

    /**
     * @author ht9
     * @reason words can't express my sorrow
     */
    @Overwrite
    public void renderWorld(float f, long l)
    {
        GL11.glEnable(2884);
        GL11.glEnable(2929);
        if (Globals.mc.renderViewEntity == null) {
            Globals.mc.renderViewEntity = Globals.mc.thePlayer;
        }

        this.getMouseOver(f);
        EntityLiving var4 = Globals.mc.renderViewEntity;
        RenderGlobal var5 = Globals.mc.renderGlobal;
        EffectRenderer var6 = Globals.mc.effectRenderer;
        double var7 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double)f;
        double var9 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double)f;
        double var11 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double)f;
        IChunkProvider var13 = Globals.mc.theWorld.method_259();
        if (var13 instanceof ChunkProviderLoadOrGenerate) {
            ChunkProviderLoadOrGenerate var14 = (ChunkProviderLoadOrGenerate)var13;
            int var15 = MathHelper.floor_float((float)((int)var7)) >> 4;
            int var16 = MathHelper.floor_float((float)((int)var11)) >> 4;
            var14.setCurrentChunkOver(var15, var16);
        }

        for(int var18 = 0; var18 < 2; ++var18) {
            if (Globals.mc.gameSettings.anaglyph) {
                anaglyphField = var18;
                if (anaglyphField == 0) {
                    GL11.glColorMask(false, true, true, false);
                } else {
                    GL11.glColorMask(true, false, false, false);
                }
            }

            GL11.glViewport(0, 0, Globals.mc.displayWidth, Globals.mc.displayHeight);
            this.updateFogColor(f);
            GL11.glClear(16640);
            GL11.glEnable(2884);
            this.setupCameraTransform(f, var18);
            ClippingHelperImpl.method_1973();
            if (Globals.mc.gameSettings.renderDistance < 2) {
                this.setupFog(-1, f);
                var5.renderSky(f);
            }

            GL11.glEnable(2912);
            this.setupFog(1, f);
            if (Globals.mc.gameSettings.ambientOcclusion) {
                GL11.glShadeModel(7425);
            }

            Frustrum var19 = new Frustrum();
            var19.setPosition(var7, var9, var11);
            Globals.mc.renderGlobal.clipRenderersByFrustrum(var19, f);
            if (var18 == 0) {
                while(!Globals.mc.renderGlobal.updateRenderers(var4, false) && l != 0L) {
                    long var20 = l - System.nanoTime();
                    if (var20 < 0L || var20 > 1000000000L) {
                        break;
                    }
                }
            }

            this.setupFog(0, f);
            GL11.glEnable(2912);
            GL11.glBindTexture(3553, Globals.mc.renderEngine.getTexture("/terrain.png"));
            RenderHelper.disableStandardItemLighting();
            var5.sortAndRender(var4, 0, (double)f);
            GL11.glShadeModel(7424);
            RenderHelper.enableStandardItemLighting();
            var5.method_1544(var4.method_931(f), var19, f);
            var6.method_327(var4, f);
            RenderHelper.disableStandardItemLighting();
            this.setupFog(0, f);
            var6.renderParticles(var4, f);
            if (Globals.mc.objectMouseOver != null && var4.isInsideOfMaterial(Material.water) && var4 instanceof EntityPlayer) {
                EntityPlayer var21 = (EntityPlayer)var4;
                GL11.glDisable(3008);
                var5.method_1547(var21, Globals.mc.objectMouseOver, 0, var21.inventory.method_675(), f);
                var5.method_1554(var21, Globals.mc.objectMouseOver, 0, var21.inventory.method_675(), f);
                GL11.glEnable(3008);
            }

            GL11.glBlendFunc(770, 771);
            this.setupFog(0, f);
            GL11.glEnable(3042);
            GL11.glDisable(2884);
            GL11.glBindTexture(3553, Globals.mc.renderEngine.getTexture("/terrain.png"));
            if (Globals.mc.gameSettings.fancyGraphics) {
                if (Globals.mc.gameSettings.ambientOcclusion) {
                    GL11.glShadeModel(7425);
                }

                GL11.glColorMask(false, false, false, false);
                int var22 = var5.sortAndRender(var4, 1, (double)f);
                if (Globals.mc.gameSettings.anaglyph) {
                    if (anaglyphField == 0) {
                        GL11.glColorMask(false, true, true, true);
                    } else {
                        GL11.glColorMask(true, false, false, true);
                    }
                } else {
                    GL11.glColorMask(true, true, true, true);
                }

                if (var22 > 0) {
                    var5.renderAllRenderLists(1, (double)f);
                }

                GL11.glShadeModel(7424);
            } else {
                var5.sortAndRender(var4, 1, (double)f);
            }

            GL11.glDepthMask(true);
            GL11.glEnable(2884);
            GL11.glDisable(3042);
            if (this.cameraZoom == 1.0D && var4 instanceof EntityPlayer && Globals.mc.objectMouseOver != null && !var4.isInsideOfMaterial(Material.water)) {
                EntityPlayer var23 = (EntityPlayer)var4;
                GL11.glDisable(3008);
                var5.method_1547(var23, Globals.mc.objectMouseOver, 0, var23.inventory.method_675(), f);
                var5.method_1554(var23, Globals.mc.objectMouseOver, 0, var23.inventory.method_675(), f);
                GL11.glEnable(3008);
            }

            this.renderRainSnow(f);
            GL11.glDisable(2912);

            this.setupFog(0, f);
            GL11.glEnable(2912);
            var5.renderClouds(f);
            GL11.glDisable(2912);
            this.setupFog(1, f);
            if (this.cameraZoom == 1.0D)
            {
                GL11.glClear(256);
                this.method_1845(f, var18);

                RenderWorldPassEvent event = new RenderWorldPassEvent(f);
                End.bus().post(event);
            }

            if (!Globals.mc.gameSettings.anaglyph)
            {
                return;
            }
        }

        GL11.glColorMask(true, true, true, false);
    }

    @Inject(method = "setupFog", at = @At(value = "TAIL"), cancellable = true)
    public void setupFog(int startCoords, float partialTicks, CallbackInfo ci)
    {
        SetupFogEvent event = new SetupFogEvent();
        End.bus().post(event);
        if (event.cancelled())
        {
            GL11.glFogi(2916, 9729);
        }
    }
}
