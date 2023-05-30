package me.ht9.end.mixin.mixins;

import me.ht9.end.End;
import me.ht9.end.event.events.RenderOverlaysEvent;
import net.minecraft.src.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemRenderer.class)
public class MixinItemRenderer
{
    @Inject(method = "renderOverlays", at = @At(value = "HEAD"), cancellable = true)
    public void renderOverlays(float partialTicks, CallbackInfo ci)
    {
        RenderOverlaysEvent event = new RenderOverlaysEvent();
        End.bus().post(event);
        if (event.cancelled())
        {
            ci.cancel();
        }
    }
}
