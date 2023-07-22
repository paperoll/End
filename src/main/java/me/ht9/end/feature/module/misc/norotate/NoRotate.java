package me.ht9.end.feature.module.misc.norotate;

import me.ht9.end.event.bus.annotation.SubscribeEvent;
import me.ht9.end.event.events.PacketEvent;
import me.ht9.end.feature.module.Module;
import me.ht9.end.feature.module.annotation.Description;
import me.ht9.end.mixin.accessors.IPacket10Flying;
import net.minecraft.src.Packet10Flying;

@Description(value = "Cancels rotation updates via lagbacks.")
public class NoRotate extends Module
{
    private static final NoRotate instance = new NoRotate();

    private float serverSyncedYaw = Float.NaN;
    private float serverSyncedPitch = Float.NaN;

    @SubscribeEvent
    public void onPacket(PacketEvent event)
    {
        if (!event.isServerBound())
        {
            if (event.getPacket() instanceof Packet10Flying)
            {
                Packet10Flying packet = (Packet10Flying) event.getPacket();
                serverSyncedYaw = packet.yaw;
                ((IPacket10Flying) packet).setYaw(mc.thePlayer.rotationYaw);
                serverSyncedPitch = packet.pitch;
                ((IPacket10Flying) packet).setPitch(mc.thePlayer.rotationPitch);
            }
        }
    }

    public static NoRotate getInstance()
    {
        return instance;
    }
}
