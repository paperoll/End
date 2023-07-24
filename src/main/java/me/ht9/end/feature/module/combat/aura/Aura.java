package me.ht9.end.feature.module.combat.aura;

import me.ht9.end.event.events.UpdateEvent;
import me.ht9.end.feature.module.Module;
import me.ht9.end.feature.module.annotation.Description;
import me.ht9.end.feature.module.setting.Setting;
import me.ht9.end.util.Timer;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPlayer;


@Description("Kills people.")
public class Aura extends Module
{
    private static final Aura instance = new Aura();

    private final Setting<Boolean> players = new Setting<>("Players", true);
    private final Setting<Boolean> animals = new Setting<>("Animals", true);
    private final Setting<Boolean> monsters = new Setting<>("Monsters", true);
    private final Setting<Integer> aps = new Setting<>("APS", 1, 7, 20, 1);
    private final Setting<Double> range = new Setting<>("Range", 1.0, 7.0, 15.0, 1);

    private Timer timer = new Timer();

    @Override
    public void onUpdate(UpdateEvent event)
    {
        for (int i = 0; i < mc.theWorld.loadedEntityList.size(); i++)
        {
            if (mc.theWorld.loadedEntityList.get(i) instanceof EntityPlayer && this.players.getValue() || mc.theWorld.loadedEntityList.get(i) instanceof EntityAnimal && this.animals.getValue() || mc.theWorld.loadedEntityList.get(i) instanceof EntityMob && this.monsters.getValue())
            {
                if (this.timer.hasReached(((double) 1000 / this.aps.getValue()), true))
                {
                    if (mc.thePlayer.getDistanceToEntity((Entity) mc.theWorld.loadedEntityList.get(i)) <= this.range.getValue())
                    {
                        mc.playerController.method_1714(mc.thePlayer, (Entity) mc.theWorld.loadedEntityList.get(i));
                    }
                }
            }
        }
    }

    public static Aura getInstance()
    {
        return instance;
    }
}