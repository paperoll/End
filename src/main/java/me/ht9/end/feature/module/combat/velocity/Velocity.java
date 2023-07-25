package me.ht9.end.feature.module.combat.velocity;

import me.ht9.end.event.bus.annotation.SubscribeEvent;
import me.ht9.end.event.events.*;
import me.ht9.end.feature.module.Module;
import me.ht9.end.feature.module.annotation.Description;
import me.ht9.end.feature.module.setting.Setting;
import net.minecraft.src.Explosion;
import net.minecraft.src.Packet28EntityVelocity;
import net.minecraft.src.Packet60Explosion;

@Description("knockback but no.")
public class Velocity extends Module
{
    private static final Velocity instance = new Velocity();

    private final Setting<Boolean> noPush = new Setting<>("NoPush", true);
    private final Setting<Boolean> blocks = new Setting<>("Blocks", true, this.noPush::getValue);
    private final Setting<Boolean> liquids   = new Setting<>("Liquids", true, this.noPush::getValue);
    private final Setting<Boolean> entities  = new Setting<>("Entities", true, this.noPush::getValue);

    private Velocity()
    {
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event)
    {
        if (event.getPacket() instanceof Packet28EntityVelocity)
        {
            Packet28EntityVelocity packet = (Packet28EntityVelocity) event.getPacket();
            if (packet.entityId == mc.thePlayer.entityId)
            {
                event.setCancelled(true);
            }
        } else if (event.getPacket() instanceof Packet60Explosion)
        {
            Packet60Explosion packet = (Packet60Explosion) event.getPacket();
            event.setCancelled(true);
            Explosion explosion = new Explosion(mc.theWorld, null, packet.explosionX, packet.explosionY, packet.explosionZ, packet.explosionSize);
            explosion.doExplosionB(true);
        }
    }

    @SubscribeEvent
    public void onPushOutOfBlocks(PushOutOfBlocksEvent event)
    {
        if (this.noPush.getValue() && this.blocks.getValue())
        {
            if (event.getPushee().equals(mc.thePlayer))
            {
                event.setCancelled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPushByLiquid(PushByLiquidEvent event)
    {
        if (this.noPush.getValue() && this.liquids.getValue())
        {
            if (event.getPushee().equals(mc.thePlayer))
            {
                //event.setCancelled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPushByEntity(PushByEntityEvent event)
    {
        if (this.noPush.getValue() && this.entities.getValue())
        {
            if (event.getPushee().equals(mc.thePlayer))
            {
                event.setCancelled(true);
            }
        }
    }

    public static Velocity getInstance()
    {
        return instance;
    }
}
