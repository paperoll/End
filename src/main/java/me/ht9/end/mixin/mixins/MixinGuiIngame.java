package me.ht9.end.mixin.mixins;

import me.ht9.end.End;
import me.ht9.end.event.events.DrawDefaultBackgroundEvent;
import me.ht9.end.event.events.RenderGameOverlayEvent;
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
    private RenderGameOverlayEvent eventParent;

    @Inject(method = "renderGameOverlay", at = @At(value = "HEAD"), cancellable = true)
    public void renderGameOverlay(float f, boolean flag, int i, int j, CallbackInfo ci)
    {
        ScaledResolution var5 = null;
        eventParent = new RenderGameOverlayEvent(f, var5);
    }

    @Inject(method = "renderGameOverlay", at = @At(value = "TAIL"), cancellable = true)
    public void renderText(float f, boolean flag, int i, int j, CallbackInfo ci)
    {
        End.bus().post(new RenderGameOverlayEvent.Text(eventParent));
    }
}
