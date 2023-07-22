package me.ht9.end.mixin.mixins;

import me.ht9.end.End;
import me.ht9.end.event.events.UpdateEvent;
import me.ht9.end.util.Globals;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityClientPlayerMP.class)
public class MixinEntityClientPlayerMP extends EntityPlayer implements Globals
{
    private double oldX;
    private double oldY;
    private double oldStance;
    private double oldZ;
    private float oldYaw;
    private float oldPitch;
    private boolean oldOnGround;

    public MixinEntityClientPlayerMP(World world) {
        super(world);
    }

    @Inject(method = "method_1922", at = @At(value = "HEAD"))
    public void onUpdateWalkingPlayer$Head(CallbackInfo ci) {
        this.oldX = this.posX;
        this.oldY = this.posY;
        this.oldStance = this.boundingBox.minY;
        this.oldZ = this.posZ;
        this.oldYaw = this.rotationYaw;
        this.oldPitch = this.rotationPitch;
        this.oldOnGround = this.onGround;
        UpdateEvent event = new UpdateEvent(this.posX, this.posY, this.oldStance, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround, UpdateEvent.Stage.PRE);
        End.bus().post(event);
        this.posX = event.getPacketX();
        this.posY = event.getPacketY();
        this.boundingBox.minY = event.getStance();
        this.posZ = event.getPacketZ();
        this.rotationYaw = event.getYaw();
        this.rotationPitch = event.getPitch();
        this.onGround = event.isOnGround();
    }

    @Inject(method = "method_1922", at = @At(value = "RETURN"))
    public void onUpdateWalkingPlayer$Return(CallbackInfo ci) {
        UpdateEvent event = new UpdateEvent(this.oldX, this.oldY, this.oldStance, this.oldZ, this.oldYaw, this.oldPitch, this.oldOnGround, UpdateEvent.Stage.POST);
        End.bus().post(event);
        this.posX = this.oldX;
        this.posY = this.oldY;
        this.boundingBox.minY = this.oldStance;
        this.posZ = this.oldZ;
        this.rotationYaw = this.oldYaw;
        this.rotationPitch = this.oldPitch;
        this.onGround = this.oldOnGround;
    }

    @Override
    public void method_494() { }
}
