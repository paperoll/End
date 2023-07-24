package me.ht9.end.feature.module.client.arraylist;

import me.ht9.end.core.Registries;
import me.ht9.end.event.bus.annotation.SubscribeEvent;
import me.ht9.end.event.events.ModuleEvent;
import me.ht9.end.event.events.UpdateEvent;
import me.ht9.end.feature.module.Module;
import me.ht9.end.feature.module.annotation.Description;
import me.ht9.end.util.RenderUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Description("Lists arrays.")
public class ArrayList extends Module
{
    private static final ArrayList instance = new ArrayList();

    private final List<ArrayListModule> renderingMods = new java.util.ArrayList<>();

    @Override
    public void onRender2d(float renderPartialTicks)
    {
        for (Module module : Registries.getInstance().getModules())
        {
            if (module.getCategory() != Category.Hidden)
            {
                boolean contains = false;
                for (ArrayListModule arrayListModule : this.renderingMods)
                {
                    if (arrayListModule.module.equals(module))
                    {
                        contains = true;
                        break;
                    }
                }
                if (module.isEnabled() && module.getDrawn().getValue() && !contains)
                {
                    mc.fontRenderer.drawString(module.getName(), 4, 14, -1);
                    this.renderingMods.add(new ArrayListModule(module, System.currentTimeMillis()));
                }
            }
        }
    }

    @SubscribeEvent
    public void onModule(ModuleEvent event)
    {
        Module module = event.getModule();
        long toggleTime = System.currentTimeMillis();
        if (module.getCategory().equals(Category.Hidden)) return;
        switch (event.getType())
        {
            case DRAW:
            case ENABLE:
            {
                if (module.getDrawn().getValue())
                {
                    for (ArrayListModule arrayListModule : this.renderingMods)
                    {
                        if (arrayListModule.module.equals(module))
                        {
                            arrayListModule.toggleTime = toggleTime;
                            arrayListModule.lastProgress = arrayListModule.progress;
                            return;
                        }
                    }
                    ArrayListModule arrayListModule = new ArrayListModule(module, toggleTime);
                    this.renderingMods.add(arrayListModule);
                    arrayListModule.lastProgress = -arrayListModule.getStringWidth();
                }
                break;
            }
            case UNDRAW:
            case DISABLE:
            {
                for (ArrayListModule arrayListModule : this.renderingMods)
                {
                    if (arrayListModule.module.equals(module))
                    {
                        arrayListModule.toggleTime = toggleTime;
                        arrayListModule.lastProgress = arrayListModule.progress;
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onDisable()
    {
        this.renderingMods.clear();
    }

    public static ArrayList getInstance()
    {
        return instance;
    }

    private static final class ArrayListModule
    {
        private final Module module;
        private long toggleTime;
        private float progress;
        private float lastProgress;

        private ArrayListModule(Module module, long toggleTime)
        {
            this.module = module;
            this.toggleTime = toggleTime;
            this.lastProgress = -this.getStringWidth();
            this.progress = -this.getStringWidth();
        }

        private boolean hasArrayListInfo()
        {
            return !StringUtils.isEmpty(this.module.getArrayListInfo());
        }

        private float getStringWidth()
        {
            String fullName = this.getFullName();
            return mc.fontRenderer.getStringWidth(fullName);
        }

        private String getFullName()
        {
            StringBuilder builder = new StringBuilder();
            builder.append(this.module.getName());
            if (this.hasArrayListInfo())
            {
                builder.append(" [");
                builder.append(this.module.getArrayListInfo());
                builder.append("]");
            }
            return builder.toString();
        }
    }
}
