package me.ht9.end.feature.module.client.arraylist;

import me.ht9.end.core.Registries;
import me.ht9.end.event.bus.annotation.SubscribeEvent;
import me.ht9.end.event.events.ModuleEvent;
import me.ht9.end.event.events.RenderGameOverlayEvent;
import me.ht9.end.event.events.UpdateEvent;
import me.ht9.end.feature.module.Module;
import me.ht9.end.feature.module.annotation.Description;
import me.ht9.end.util.RenderUtils;
import net.minecraft.src.ScaledResolution;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Description("Lists arrays.")
public class ArrayList extends Module
{
    private static final ArrayList instance = new ArrayList();

    @Override
    public void onRender2d(float partialTicks)
    {
        mc.fontRenderer.drawStringWithShadow("End", 4, 14, -1);
        int yOff = 0;

        ScaledResolution scaledResolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);

        for (Module module : Registries.getInstance().getModules())
        {
            if (module.isEnabled())
            {
                mc.fontRenderer.drawStringWithShadow(module.getName(), scaledResolution.getScaledWidth() - mc.fontRenderer.getStringWidth(module.getName()), 2 + yOff * 9, -1);
                ++yOff;
            }
        }

    }

    public static ArrayList getInstance()
    {
        return instance;
    }
}
