package me.ht9.end.mixin.mixins;

import me.ht9.end.End;
import me.ht9.end.event.events.InputEvent;
import me.ht9.end.feature.module.render.esp.OutlineShader;
import me.ht9.end.mixin.accessors.IMinecraft;
import me.ht9.end.shader.Framebuffer;
import me.ht9.end.shader.renderer.OpenGlHelper;
import me.ht9.end.util.Globals;
import me.ht9.end.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class)
public abstract class MixinMinecraft implements Globals
{
    @Shadow public StatFileWriter statFileWriter;

    @Shadow public GuiIngame ingameGUI;

    @Shadow public EntityRenderer entityRenderer;

    @Shadow public EntityPlayerSP thePlayer;

    @Shadow public World theWorld;

    @Shadow public volatile boolean isGamePaused;

    @Shadow public PlayerController playerController;

    @Shadow public RenderEngine renderEngine;

    @Shadow public GuiScreen currentScreen;

    @Shadow public abstract void displayGuiScreen(GuiScreen arg);

    @Shadow private int leftClickCounter;

    @Shadow private int mouseTicksRan;

    @Shadow private int ticksRan;

    @Shadow private long systemTime;

    @Shadow public GameSettings gameSettings;

    @Shadow public boolean inGameHasFocus;

    @Shadow protected abstract void clickMouse(int i);

    @Shadow protected abstract void clickMiddleMouseButton();

    @Shadow public abstract void toggleFullscreen();

    @Shadow public abstract void displayInGameMenu();

    @Shadow protected abstract void forceReload();

    @Shadow private Timer timer;

    @Shadow private int joinPlayerCounter;

    @Shadow public RenderGlobal renderGlobal;

    @Shadow public abstract boolean isMultiplayerWorld();

    @Shadow public EffectRenderer effectRenderer;

    @Shadow protected abstract void method_2110(int i, boolean flag);

    @Shadow public abstract void setIngameFocus();

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getMinecraftDir()Ljava/io/File;", shift = At.Shift.BEFORE))
    public void injectFramebuffer(CallbackInfo ci)
    {
        OpenGlHelper.initializeTextures();
        RenderUtils.framebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        RenderUtils.framebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
    }
    @Inject(method = "loadScreen", at = @At(value = "HEAD"))
    public void a(CallbackInfo ci)
    {
        ScaledResolution scaledresolution = new ScaledResolution(this.gameSettings, mc.displayWidth, mc.displayHeight);
        int i = scaledresolution.scaleFactor;
        RenderUtils.framebuffer = new Framebuffer(scaledresolution.getScaledWidth() * i, scaledresolution.getScaledHeight() * i, true);
        RenderUtils.framebuffer.bindFramebuffer(false);
    }

    @Inject(method = "loadScreen", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDisable(I)V", ordinal = 3))
    public void aa(CallbackInfo ci)
    {
        ScaledResolution scaledresolution = new ScaledResolution(this.gameSettings, mc.displayWidth, mc.displayHeight);
        int i = scaledresolution.scaleFactor;
        RenderUtils.framebuffer.unbindFramebuffer();
        RenderUtils.framebuffer.framebufferRender(scaledresolution.getScaledWidth() * i, scaledresolution.getScaledHeight() * i);
    }

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal = 0, shift = At.Shift.AFTER))
    public void bindFramebuffer(CallbackInfo ci)
    {
        RenderUtils.framebuffer.bindFramebuffer(true);
    }

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal = 1, shift = At.Shift.AFTER))
    public void unbindFramebuffer(CallbackInfo ci)
    {
        RenderUtils.framebuffer.unbindFramebuffer();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        RenderUtils.framebuffer.framebufferRender(mc.displayWidth, mc.displayHeight);
    }

    @Inject(method = "resize", at = @At(value = "TAIL"))
    public void updateFramebuffer(CallbackInfo ci)
    {
        this.updateFramebufferSize();
        //((IMinecraft) mc).setSession(new Session("Handbooked", "1"));
    }


    /**
     * @author
     * @reason
     */
    @Overwrite
    public void runTick() {
        statFileWriter.method_1996();
        ingameGUI.updateTick();
        entityRenderer.getMouseOver(1.0F);
        if (thePlayer != null) {
            net.minecraft.src.IChunkProvider ichunkprovider = theWorld.method_259();
            if (ichunkprovider instanceof ChunkProviderLoadOrGenerate) {
                ChunkProviderLoadOrGenerate chunkproviderloadorgenerate =
                        (ChunkProviderLoadOrGenerate) ichunkprovider;
                int j = MathHelper.floor_float((int) thePlayer.posX) >> 4;
                int i1 = MathHelper.floor_float((int) thePlayer.posZ) >> 4;
                chunkproviderloadorgenerate.setCurrentChunkOver(j, i1);
            }
        }
        if (!isGamePaused && theWorld != null) {
            playerController.updateController();
        }
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, renderEngine.getTexture("/terrain.png"));
        if (!isGamePaused) {
            renderEngine.updateDynamicTextures();
        }
        if (currentScreen == null && thePlayer != null) {
            if (thePlayer.health <= 0) {
                displayGuiScreen(null);
            } else if (thePlayer.isPlayerSleeping() && theWorld != null && theWorld.multiplayerWorld) {
                displayGuiScreen(new GuiSleepMP());
            }
        } else if (currentScreen != null
                && (currentScreen instanceof GuiSleepMP)
                && !thePlayer.isPlayerSleeping()) {
            displayGuiScreen(null);
        }
        if (currentScreen != null) {
            leftClickCounter = 10000;
            mouseTicksRan = ticksRan + 10000;
        }
        if (currentScreen != null) {
            currentScreen.handleInput();
            if (currentScreen != null) {
                currentScreen.field_25091_h.method_351();
                currentScreen.updateScreen();
            }
        }
        if (currentScreen == null || currentScreen.field_948_f) {
            while (Mouse.next()) {
                long l = System.currentTimeMillis() - systemTime;
                if (l <= 200L) {
                    int k = Mouse.getEventDWheel();
                    if (k != 0) {
                        thePlayer.inventory.changeCurrentItem(k);
                        if (gameSettings.field_22275_C) {
                            if (k > 0) {
                                k = 1;
                            }
                            if (k < 0) {
                                k = -1;
                            }
                            gameSettings.field_22272_F += (float) k * 0.25F;
                        }
                    }
                    if (currentScreen == null) {
                        if (!inGameHasFocus && Mouse.getEventButtonState()) {
                            setIngameFocus();
                        } else {
                            if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
                                clickMouse(0);
                                mouseTicksRan = ticksRan;
                            }
                            if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
                                clickMouse(1);
                                mouseTicksRan = ticksRan;
                            }
                            if (Mouse.getEventButton() == 2 && Mouse.getEventButtonState()) {
                                clickMiddleMouseButton();
                            }
                        }
                    } else {
                        currentScreen.handleMouseInput();
                    }
                }
                End.bus().post(new InputEvent.MouseInputEvent());
            }
            if (leftClickCounter > 0) {
                leftClickCounter--;
            }
            while (Keyboard.next()) {
                thePlayer.handleKeyPress(Keyboard.getEventKey(), Keyboard.getEventKeyState());
                if (Keyboard.getEventKeyState()) {
                    if (Keyboard.getEventKey() == 87) {
                        toggleFullscreen();
                    } else {
                        if (currentScreen != null) {
                            currentScreen.handleKeyboardInput();
                        } else {

                            if (Keyboard.getEventKey() == 1) {
                                displayInGameMenu();
                            }
                            if (Keyboard.getEventKey() == 31 && Keyboard.isKeyDown(61)) {
                                forceReload();
                            }
                            if (Keyboard.getEventKey() == 59) {
                                gameSettings.hideGUI = !gameSettings.hideGUI;
                            }
                            if (Keyboard.getEventKey() == 61) {
                                gameSettings.showDebugInfo = !gameSettings.showDebugInfo;
                            }
                            if (Keyboard.getEventKey() == 63) {
                                gameSettings.thirdPersonView = !gameSettings.thirdPersonView;
                            }
                            if (Keyboard.getEventKey() == 66) {
                                gameSettings.smoothCamera = !gameSettings.smoothCamera;
                            }
                            if (Keyboard.getEventKey() == gameSettings.keyBindInventory.keyCode) {
                                displayGuiScreen(new GuiInventory(thePlayer));
                            }
                            if (Keyboard.getEventKey() == gameSettings.keyBindDrop.keyCode) {
                                thePlayer.dropCurrentItem();
                            }
                            if (Keyboard.getEventKey() == gameSettings.keyBindChat.keyCode) {
                                displayGuiScreen(new GuiChat());
                            }
                        }
                        for (int i = 0; i < 9; i++) {
                            if (Keyboard.getEventKey() == 2 + i) {
                                thePlayer.inventory.currentItem = i;
                            }
                        }

                        if (Keyboard.getEventKey() == gameSettings.keyBindToggleFog.keyCode) {
                            gameSettings.setOptionValue(
                                    EnumOptions.RENDER_DISTANCE,
                                    !Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(54) ? 1 : -1);
                        }
                    }
                }
                End.bus().post(new InputEvent.KeyInputEvent());
            }
            if (currentScreen == null) {
                if (Mouse.isButtonDown(0)
                        && (float) (ticksRan - mouseTicksRan) >= timer.ticksPerSecond / 4F
                        && inGameHasFocus) {
                    clickMouse(0);
                    mouseTicksRan = ticksRan;
                }
                if (Mouse.isButtonDown(1)
                        && (float) (ticksRan - mouseTicksRan) >= timer.ticksPerSecond / 4F
                        && inGameHasFocus) {
                    clickMouse(1);
                    mouseTicksRan = ticksRan;
                }
            }
            method_2110(0, currentScreen == null && Mouse.isButtonDown(0) && inGameHasFocus);
        }
        if (theWorld != null) {
            if (thePlayer != null) {
                joinPlayerCounter++;
                if (joinPlayerCounter == 30) {
                    joinPlayerCounter = 0;
                    theWorld.joinEntityInSurroundings(thePlayer);
                }
            }
            theWorld.difficultySetting = gameSettings.difficulty;
            if (theWorld.multiplayerWorld) {
                theWorld.difficultySetting = 3;
            }
            if (!isGamePaused) {
                entityRenderer.updateRenderer();
            }
            if (!isGamePaused) {
                renderGlobal.updateClouds();
            }
            if (!isGamePaused) {
                if (theWorld.field_27172_i > 0) {
                    theWorld.field_27172_i--;
                }
                theWorld.updateEntities();
            }
            if (!isGamePaused || isMultiplayerWorld()) {
                theWorld.setAllowedMobSpawns(gameSettings.difficulty > 0, true);
                theWorld.tick();
            }
            if (!isGamePaused && theWorld != null) {
                theWorld.randomDisplayUpdates(
                        MathHelper.floor_double(thePlayer.posX),
                        MathHelper.floor_double(thePlayer.posY),
                        MathHelper.floor_double(thePlayer.posZ));
            }
            if (!isGamePaused) {
                effectRenderer.updateEffects();
            }
        }
        systemTime = System.currentTimeMillis();
    }

    private void updateFramebufferSize()
    {
        RenderUtils.framebuffer.createBindFramebuffer(mc.displayWidth, mc.displayHeight);

        if (mc.entityRenderer != null)
        {
            //this.entityRenderer.updateShaderGroupSize(this.displayWidth, this.displayHeight);
        }
    }
}
