package me.ht9.end.mixin.mixins;

import me.ht9.end.util.Resource;
import net.minecraft.src.ThreadDownloadResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ThreadDownloadResources.class)
final class MixinThreadDownloadResources
{
    @ModifyConstant(method = "run", constant = @Constant(stringValue = "http://s3.amazonaws.com/MinecraftResources/"), remap = false, require = 0)
    private String changeURL(String url) {
        return Resource.getResources();
    }
}