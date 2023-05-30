package me.ht9.end.feature.module.client.hud;

import me.ht9.end.event.bus.annotation.SubscribeEvent;
import me.ht9.end.event.events.PacketEvent;
import me.ht9.end.feature.module.Module;
import me.ht9.end.feature.module.annotation.Description;
import me.ht9.end.mixin.accessors.IMinecraft;
import me.ht9.end.util.NetworkUtils;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet11PlayerPosition;

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
    }

    @Override
    public void onDisable()
    {
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event)
    {
        if(event.getPacket() instanceof Packet10Flying)
        {
            //event.setCancelled(true);
        }
    }

    public static HUD getInstance()
    {
        return instance;
    }
}