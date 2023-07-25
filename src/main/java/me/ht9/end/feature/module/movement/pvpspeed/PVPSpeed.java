package me.ht9.end.feature.module.movement.pvpspeed;

import me.ht9.end.event.events.UpdateEvent;
import me.ht9.end.feature.module.Module;
import me.ht9.end.feature.module.annotation.Description;
import me.ht9.end.mixin.accessors.IMinecraft;
import me.ht9.end.util.NetworkUtils;
import me.ht9.end.util.Timer;
import net.minecraft.src.Packet19EntityAction;

@Description(value = "Move faster slower.")
public class PVPSpeed extends Module
{
    private static final PVPSpeed instance = new PVPSpeed();

    private final Timer timer = new Timer();

    @Override
    public void onUpdate(UpdateEvent event)
    {
        ((IMinecraft) mc).getTimer().timerSpeed = 1.25f;
        mc.thePlayer.stepHeight = 1.0f;
        event.setOnGround(true);
        if (timer.hasReached(1100, true))
        {
            NetworkUtils.dispatchPacket(new Packet19EntityAction(mc.thePlayer, 3));
        }
    }

    @Override
    public void onDisable()
    {
        NetworkUtils.dispatchPacket(new Packet19EntityAction(mc.thePlayer, 3));
        ((IMinecraft) mc).getTimer().timerSpeed = 1.0f;
        mc.thePlayer.stepHeight = 0.5f;
    }

    public static PVPSpeed getInstance()
    {
        return instance;
    }
}
