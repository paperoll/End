package me.ht9.end.feature.module.misc.freecam;

import me.ht9.end.event.bus.annotation.SubscribeEvent;
import me.ht9.end.event.events.PacketEvent;
import me.ht9.end.event.events.UpdateEvent;
import me.ht9.end.feature.module.Module;
import me.ht9.end.feature.module.annotation.Description;
import me.ht9.end.feature.module.setting.Setting;
import me.ht9.end.util.MovementUtils;
import me.ht9.end.util.NetworkUtils;
import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;

@Description("Camera for free.")
public class Freecam extends Module
{
    private static final Freecam instance = new Freecam();

    private final Setting<Float> speed = new Setting<>("Speed", 1.0f, 2.5f, 5.0f, 1);

    private double x, y, z;
    private float yaw, pitch;

    @Override
    public void onEnable()
    {
        x = mc.thePlayer.posX;
        y = mc.thePlayer.posY;
        z = mc.thePlayer.posZ;
        yaw = mc.thePlayer.rotationYaw;
        pitch = mc.thePlayer.rotationPitch;
    }

    @Override
    public void onDisable()
    {
        MovementUtils.setSpeed(0.0);
        mc.thePlayer.setPosition(x, y, z);
        mc.thePlayer.rotationYaw = yaw;
        mc.thePlayer.rotationPitch = pitch;
        mc.thePlayer.noClip = false;
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event)
    {
        if(event.getPacket() instanceof Packet10Flying)
        {
            event.setCancelled(true);
        }
        if(event.getPacket() instanceof Packet11PlayerPosition)
        {
            event.setCancelled(true);
        }
        if(event.getPacket() instanceof Packet12PlayerLook)
        {
            event.setCancelled(true);
        }
    }

    @Override
    public void onUpdate(UpdateEvent event)
    {
        mc.thePlayer.noClip = true;
        mc.thePlayer.motionY = 0.0;
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            mc.thePlayer.motionY += 0.55;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            mc.thePlayer.motionY -= 0.65;
        }
        if (mc.thePlayer.movementInput.moveForward != 0 || mc.thePlayer.movementInput.moveStrafe != 0) {
            MovementUtils.setSpeed(0.6);
        }

        NetworkUtils.dispatchPacket(new Packet0KeepAlive());
    }

    public void copyDataFrom(Entity par1Entity, boolean par2)
    {
        NBTTagCompound var3 = new NBTTagCompound();
        par1Entity.writeToNBT(var3);
        par1Entity.readFromNBT(var3);
    }


    public static Freecam getInstance()
    {
        return instance;
    }
}
