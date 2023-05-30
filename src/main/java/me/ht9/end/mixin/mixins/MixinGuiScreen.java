package me.ht9.end.mixin.mixins;

import me.ht9.end.End;
import me.ht9.end.event.events.DrawDefaultBackgroundEvent;
import net.minecraft.src.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiScreen.class)
public final class MixinGuiScreen
{
    @Inject(method = "drawDefaultBackground", at = @At(value = "HEAD"), cancellable = true)
    public void drawDefaultBackground(CallbackInfo ci)
    {
        DrawDefaultBackgroundEvent event = new DrawDefaultBackgroundEvent();
        End.bus().post(event);
        if (event.cancelled())
        {
            ci.cancel();
        }
    }
}