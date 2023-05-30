package me.ht9.end.mixin.mixins;

import me.ht9.end.util.Globals;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(value = GuiIngame.class)
public abstract class MixinGuiIngame implements Globals
{
    @Inject(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityRenderer;method_1843()V"), cancellable = true)
    public void renderGameOverlay$method_1843(float flag, boolean i, int j, int par4, CallbackInfo ci)
    {
        //ci.cancel();
    }
}
