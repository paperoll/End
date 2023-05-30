package me.ht9.end.mixin.mixins;

import me.ht9.end.End;
import me.ht9.end.event.events.PushOutOfBlocksEvent;
import me.ht9.end.util.Globals;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP extends EntityPlayer implements Globals
{
    public MixinEntityPlayerSP(World arg)
    {
        super(arg);
    }

    @Inject(method = "pushOutOfBlocks", at = @At(value = "HEAD"), cancellable = true)
    public void pushOutOfBlocks(double x, double y, double z, CallbackInfoReturnable<Boolean> cir)
    {
        PushOutOfBlocksEvent event = new PushOutOfBlocksEvent(this);
        End.bus().post(event);
        if (event.cancelled())
        {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Override
    public void method_494() { }
}
