package me.ht9.end.mixin.mixins;

import me.ht9.end.End;
import me.ht9.end.event.events.BlockDamageProgressEvent;
import me.ht9.end.event.events.BlockHitDelayEvent;
import me.ht9.end.event.events.PlayerDamageBlockEvent;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ PlayerControllerMP.class })
public final class MixinPlayerControllerMP
{
    @Shadow
    private int blockHitDelay;

    @Inject(method = "sendBlockRemoving", at = @At(value = "HEAD"), cancellable = true)
    public void onPlayerDamageBlock(int x, int y, int z, int facing, CallbackInfo ci)
    {
        if (this.blockHitDelay <= 0)
        {
            PlayerDamageBlockEvent event = new PlayerDamageBlockEvent(x, y, z, facing);
            End.bus().post(event);
            if (event.cancelled())
            {
                ci.cancel();
            }
        }
    }

    @Redirect(method = "sendBlockRemoving", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Block;blockStrength(Lnet/minecraft/src/EntityPlayer;)F"))
    public float getPlayerRelativeBlockHardness(Block instance, EntityPlayer entityPlayer)
    {
        BlockDamageProgressEvent event = new BlockDamageProgressEvent(instance.blockStrength(entityPlayer));
        End.bus().post(event);
        System.out.println(event.getProgress());
        if (event.cancelled())
        {
            return event.getProgress();
        }
        return instance.blockStrength(entityPlayer);
    }

    @Redirect(method = "sendBlockRemoving", at = @At(value = "FIELD", target = "Lnet/minecraft/src/PlayerControllerMP;blockHitDelay:I", ordinal = 2))
    public void setBlockHitDelay0(PlayerControllerMP playerControllerMP, int original)
    {
        BlockHitDelayEvent event = new BlockHitDelayEvent(original);
        End.bus().post(event);
        if (event.cancelled())
        {
            this.blockHitDelay = event.getBlockHitDelay();
            return;
        }
        this.blockHitDelay = original;
    }

    @Redirect(method = "sendBlockRemoving", at = @At(value = "FIELD", target = "Lnet/minecraft/src/PlayerControllerMP;blockHitDelay:I", ordinal = 3))
    public void setBlockHitDelay1(PlayerControllerMP playerControllerMP, int original)
    {
        BlockHitDelayEvent event = new BlockHitDelayEvent(original);
        End.bus().post(event);
        if (event.cancelled())
        {
            this.blockHitDelay = event.getBlockHitDelay();
            return;
        }
        this.blockHitDelay = original;
    }
}
