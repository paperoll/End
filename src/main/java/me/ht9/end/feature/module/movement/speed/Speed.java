package me.ht9.end.feature.module.movement.speed;

import me.ht9.end.event.events.UpdateEvent;
import me.ht9.end.feature.module.Module;
import me.ht9.end.feature.module.annotation.Description;
import me.ht9.end.mixin.accessors.IMinecraft;
import me.ht9.end.util.NetworkUtils;
import me.ht9.end.util.Timer;
import net.minecraft.src.Packet19EntityAction;

@Description(value = "Move faster.")
public class Speed extends Module
{
    private static final Speed instance = new Speed();

    private Timer timer = new Timer();

    @Override
    public void onUpdate(UpdateEvent event)
    {
        ((IMinecraft) mc).getTimer().timerSpeed = 100.0f;
        mc.thePlayer.stepHeight = 1.0f;
        event.setOnGround(true);
        if (timer.hasReached(450))
        {
            for (int i = 0; i < 2; i++)
            {
//                event.setPacketX(0.4723658827582);
//                event.setPacketY(0.4723658827582);
//                event.setStance(1.4723658827582);
//                event.setPacketZ(0.4723658827582);
            }
            NetworkUtils.dispatchPacket(new Packet19EntityAction(mc.thePlayer, 3));
            timer.reset();
        }
    }

    @Override
    public void onDisable()
    {
        NetworkUtils.dispatchPacket(new Packet19EntityAction(mc.thePlayer, 3));
        ((IMinecraft) mc).getTimer().timerSpeed = 1.0f;
        mc.thePlayer.stepHeight = 0.5f;
    }

    public static Speed getInstance()
    {
        return instance;
    }
}
