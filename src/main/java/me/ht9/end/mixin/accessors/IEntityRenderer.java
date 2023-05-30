package me.ht9.end.mixin.accessors;

import net.minecraft.src.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = EntityRenderer.class)
public interface IEntityRenderer
{
    @Invoker(value = "setupCameraTransform")
    void invokeSetupCameraTransform(float partialTicks, int pass);

    @Invoker(value = "method_1845")
    void invokeRenderHand(float partialTicks, int pass);
}
