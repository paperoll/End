package me.ht9.end.mixin.mixins;

import me.ht9.end.End;
import me.ht9.end.event.events.PlayerMoveEvent;
import me.ht9.end.event.events.PushByEntityEvent;
import me.ht9.end.event.events.PushByLiquidEvent;
import me.ht9.end.util.Globals;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntity implements Globals
{
    @Final
    @Shadow
    public AxisAlignedBB boundingBox;

    @Shadow public boolean noClip;
    @Shadow public double posX;
    @Shadow public double posY;
    @Shadow public double posZ;
    @Shadow public float ySize;
    @Shadow public boolean isInWeb;
    @Shadow public double motionX;
    @Shadow public double motionY;
    @Shadow public double motionZ;
    @Shadow public boolean onGround;

    @Shadow public abstract boolean isSneaking();

    @Shadow public World worldObj;
    @Shadow public float yOffset;

    private boolean shouldInject = true;

    @Inject(method = "applyEntityCollision", at = @At(value = "HEAD"), cancellable = true)
    public void applyEntityCollision(Entity entityIn, CallbackInfo ci)
    {
        Entity $this = (Entity) (Object) this;
        PushByEntityEvent event = new PushByEntityEvent($this);
        End.bus().post(event);
        if (event.cancelled())
        {
            ci.cancel();
        }
    }

    @Inject(method = "handleWaterMovement", at = @At(value = "HEAD"), cancellable = true)
    public void handleWaterMovement(CallbackInfoReturnable<Boolean> cir)
    {
        Entity $this = (Entity) (Object) this;
        PushByLiquidEvent event = new PushByLiquidEvent($this);
        End.bus().post(event);
        if (event.cancelled())
        {
            cir.setReturnValue(false);
        }
    }


    @Inject(method = "moveEntity", at = @At("HEAD"), cancellable = true)
    public void moveEntity(double x, double y, double z, CallbackInfo ci)
    {
        Entity $this = (Entity) (Object) this;
        // noinspection ConstantConditions
        if ($this == mc.thePlayer && this.shouldInject)
        {
            PlayerMoveEvent event = new PlayerMoveEvent(x, y, z);
            End.bus().post(event);
            this.shouldInject = false;
            $this.moveEntity(event.getX(), event.getY(), event.getZ());
            this.shouldInject = true;
            ci.cancel();
        }
    }
}
