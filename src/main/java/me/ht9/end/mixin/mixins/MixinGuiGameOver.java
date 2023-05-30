package me.ht9.end.mixin.mixins;

import me.ht9.end.End;
import me.ht9.end.event.events.DrawDefaultBackgroundEvent;
import net.minecraft.src.GuiGameOver;
import net.minecraft.src.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = GuiGameOver.class)
public abstract class MixinGuiGameOver extends GuiScreen
{
    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiGameOver;drawGradientRect(IIIIII)V"))
    public void drawGradientRect(GuiGameOver guiGameOver, int left, int top, int right, int bottom, int startColor, int endColor)
    {
        DrawDefaultBackgroundEvent event = new DrawDefaultBackgroundEvent();
        End.bus().post(event);
        if (!event.cancelled())
        {
            this.drawGradientRect(left, top, right, bottom, startColor, endColor);
        }
    }
}
