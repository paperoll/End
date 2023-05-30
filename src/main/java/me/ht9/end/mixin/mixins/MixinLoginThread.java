package me.ht9.end.mixin.mixins;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.ThreadCheckHasPaid.class)
final class MixinLoginThread
{
    @Inject(method = "run", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private void onRun(CallbackInfo ci) {
        ci.cancel();
    }
}