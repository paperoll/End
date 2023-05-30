package me.ht9.end.feature.module.misc.antiaim;

import me.ht9.end.event.bus.annotation.SubscribeEvent;
import me.ht9.end.event.events.PacketEvent;
import me.ht9.end.event.events.UpdateEvent;
import me.ht9.end.feature.module.Module;
import me.ht9.end.feature.module.annotation.Description;
import me.ht9.end.feature.module.setting.Setting;
import me.ht9.end.mixin.accessors.IMinecraft;
import net.minecraft.src.Packet10Flying;

@Description(value = "Fucks with your aim to make you look retarded.")
public final class AntiAim extends Module
{
    private static final AntiAim instance = new AntiAim();

    private final Setting<Float> increment = new Setting<>("Pitch-Increment", 1.0F, 2.0F, 5.0F, 2);
    private final Setting<Float> speed = new Setting<>("Jitter-Speed", 0.1F, 2.0F, 10.0F, 2);
    private final Setting<Float> degree = new Setting<>("Jitter-Degree", 15.0F, 45.0F, 360.0F, 2);

    private int tickCounter = 0;
    private float pitch = 0.0f;

    private AntiAim()
    {
    }

    @Override
    public void onUpdate(UpdateEvent event)
    {
        pitch = (pitch + increment.getValue() + 540.0f) % 360.0f - 180.0f;

        event.setYaw((float) Math.sin(tickCounter * speed.getValue()) * degree.getValue());
        event.setPitch(Math.max(-180.0f, Math.min(pitch, 180.0f)));
        tickCounter++;
    }

    public static AntiAim getInstance()
    {
        return instance;
    }
}