package me.ht9.end.mixin.mixins;

import me.ht9.end.End;
import me.ht9.end.event.events.PacketEvent;
import me.ht9.end.util.Globals;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(NetworkManager.class)
public class MixinNetworkManager
{
    @Inject(method = "addToSendQueue", at = @At("HEAD"), cancellable = true)
    private void send(Packet packet, CallbackInfo ci)
    {
        PacketEvent event = new PacketEvent(packet, true);
        End.bus().post(event);
        if (event.cancelled())
        {
            ci.cancel();
        }
    }

    @Inject(method = "readPacket", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void onPacketReceive(CallbackInfoReturnable<Boolean> cir, int unused, Packet packet)
    {
        PacketEvent event = new PacketEvent(packet, false);
        End.bus().post(event);
        if (event.cancelled())
        {
            cir.cancel();
        }
    }
}
