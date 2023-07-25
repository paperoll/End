package me.ht9.end.feature.module.client.hud;

import me.ht9.end.event.bus.annotation.SubscribeEvent;
import me.ht9.end.event.events.PacketEvent;
import me.ht9.end.event.events.UpdateEvent;
import me.ht9.end.feature.module.Module;
import me.ht9.end.feature.module.annotation.Description;
import me.ht9.end.util.NetworkUtils;
import net.minecraft.src.*;

@Description(value = "Render client information on the screen.")
public final class HUD extends Module
{
    private static final HUD instance = new HUD();

    private HUD()
    {
    }

    @Override
    public void onEnable()
    {
        NetworkUtils.dispatchPacket(new Packet11PlayerPosition(0.0, 0.0, Double.NaN, 0.0, true));
    }

    @Override
    public void onDisable()
    {
    }

    @Override
    public void onUpdate(UpdateEvent event)
    {
//        if(mc.thePlayer.ticksExisted % 15 == 0)
//        {
//            NetworkUtils.dispatchPacket(new Packet0KeepAlive());
//        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event)
    {
//        if(event.getPacket() instanceof Packet10Flying)
//        {
//            event.setCancelled(true);
//        }
    }

    public static HUD getInstance()
    {
        return instance;
    }
}