package me.ht9.end.feature.module.movement.speed;

import me.ht9.end.event.bus.annotation.SubscribeEvent;
import me.ht9.end.event.events.PacketEvent;
import me.ht9.end.event.events.UpdateEvent;
import me.ht9.end.feature.module.Module;
import me.ht9.end.feature.module.annotation.Description;
import me.ht9.end.mixin.accessors.IMinecraft;
import me.ht9.end.util.NetworkUtils;
import me.ht9.end.util.Timer;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet19EntityAction;
import org.lwjgl.input.Keyboard;

@Description(value = "Move faster.")
public class Speed extends Module
{
    private static final Speed instance = new Speed();

    private final Timer timer = new Timer();

    @SubscribeEvent
    public void onUpdate(UpdateEvent event)
    {
        ((IMinecraft) mc).getTimer().timerSpeed = 100;
        mc.thePlayer.stepHeight = 1.0f;
//        mc.thePlayer.motionY = 0;
        if (this.timer.hasReached(450, true))
        {
            NetworkUtils.dispatchPacket(new Packet19EntityAction(mc.thePlayer, 3));
        }
    }

    @Override
    public void onDisable()
    {
        mc.thePlayer.noClip = false;
        NetworkUtils.dispatchPacket(new Packet19EntityAction(mc.thePlayer, 3));
        ((IMinecraft) mc).getTimer().timerSpeed = 1.0f;
        mc.thePlayer.stepHeight = 0.5f;
    }

    public static Speed getInstance()
    {
        return instance;
    }
}
