package me.ht9.end.feature.module.render.truesight;

import me.ht9.end.event.bus.annotation.SubscribeEvent;
import me.ht9.end.event.events.SetupFogEvent;
import me.ht9.end.event.events.UpdateEvent;
import me.ht9.end.feature.module.Module;
import me.ht9.end.feature.module.annotation.Description;
import me.ht9.end.feature.module.setting.Setting;

import java.util.Arrays;

@Description(value = "Allows you to see based on settings provided.")
public class TrueSight extends Module
{
    private static final TrueSight instance = new TrueSight();

    private final Setting<Boolean> noFog = new Setting<>("NoFog", true);
    private final Setting<Boolean> fullBright = new Setting<>("FullBright", true);

    private boolean brightnessTableFilled = false;

    private TrueSight()
    {
    }

    @Override
    public void onUpdate(UpdateEvent event)
    {
        if (this.fullBright.getValue())
        {
            this.fillBrightnessTable();
        } else
        {
            this.resetBrightnessTable();
        }
    }

    @Override
    public void onDisable()
    {
        this.resetBrightnessTable();
    }

    @SubscribeEvent
    public void onSetupFog(SetupFogEvent event)
    {
        if (this.noFog.getValue())
        {
            //event.setCancelled(true);
        }
    }

    private void fillBrightnessTable()
    {
        this.brightnessTableFilled = true;
        Arrays.fill(mc.theWorld.worldProvider.lightBrightnessTable, 1.0F);
    }

    private void resetBrightnessTable()
    {
        if (this.brightnessTableFilled)
        {
            for (int i = 0; i <= 15; ++i)
            {
                float f1 = 1.0F - (float) i / 15.0F;
                mc.theWorld.worldProvider.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F);
            }
            this.brightnessTableFilled = false;
        }
    }

    @Override
    public void onEnable()
    {

    }

    public static TrueSight getInstance()
    {
        return instance;
    }
}
