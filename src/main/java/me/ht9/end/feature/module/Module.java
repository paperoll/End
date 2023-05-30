package me.ht9.end.feature.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.ht9.end.End;
import me.ht9.end.core.Registries;
import me.ht9.end.event.bus.annotation.SubscribeEvent;
import me.ht9.end.event.events.ModuleEvent;
import me.ht9.end.event.events.UpdateEvent;
import me.ht9.end.feature.Feature;
import me.ht9.end.feature.module.annotation.Aliases;
import me.ht9.end.feature.module.annotation.Description;
import me.ht9.end.feature.module.binding.Bind;
import me.ht9.end.feature.module.setting.Setting;
import me.ht9.end.util.Globals;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class Module extends Feature implements Globals
{
    private final Category category;
    private final String description;
    private Supplier<String> arrayListInfo;
    private final List<Setting<?>> settings = new ArrayList<>();
    private boolean enabled = false;
    private boolean displayed = true;
    private final Setting<Bind> toggleBind;
    private final Setting<BindMode> bindMode;
    private final Setting<Boolean> drawn;

    protected Module()
    {
        this.name = this.getClass().getSimpleName();
        this.aliases = this.getClass().isAnnotationPresent(Aliases.class) ? this.getClass().getAnnotation(Aliases.class).value() : new String[] { };
        this.category = Category.Render;
        this.description = this.getClass().getAnnotation(Description.class).value();
        this.arrayListInfo = () -> "";

        Bind toggle = new Bind(Keyboard.KEY_NONE, Bind.BindType.KEYBOARD, null).withAction(this::toggle);
        this.toggleBind = new Setting<>("Bind", toggle);
        this.bindMode = new Setting<>("Toggle", BindMode.Normal);
        this.drawn = new Setting<>("Drawn", true)
                .withOnChange(val ->
                {
                    ModuleEvent event = new ModuleEvent(this, val ? ModuleEvent.Type.DRAW : ModuleEvent.Type.UNDRAW);
                    End.bus().post(event);
                });
    }

    public void onLogIn()
    {
    }

    public void onEnable()
    {
    }

    public void onUpdate(UpdateEvent event)
    {
    }

    public void onRender2d(float partialTicks)
    {
    }

    public void onRender3d(float partialTicks)
    {
    }

    public void onDisable()
    {
    }

    public void onLogOut()
    {
    }

    public final void toggle()
    {
        if (this.enabled)
        {
            this.disable();
        } else
        {
            this.enable();
        }
    }

    public final void enable()
    {
        if (!this.enabled)
        {
            this.enabled = true;
            End.bus().register(this);
            if (this.category != Category.Hidden)
            {
                End.bus().post(new ModuleEvent(this, ModuleEvent.Type.ENABLE));
            }
            if (mc.thePlayer != null && mc.theWorld != null)
            {
                this.onEnable();
            }
        }
    }

    public final void disable()
    {
        if (this.enabled)
        {
            this.enabled = false;
            if (this.category != Category.Hidden)
            {
                End.bus().post(new ModuleEvent(this, ModuleEvent.Type.DISABLE));
            }
            End.bus().unregister(this);
            if (mc.thePlayer != null && mc.theWorld != null)
            {
                this.onDisable();
            }
        }
    }

    public JsonObject serialize()
    {
        JsonObject module = new JsonObject();
        JsonObject settings = new JsonObject();
        module.add("Enabled", new JsonPrimitive(this.enabled));
        for (Setting<?> setting : this.settings)
        {
            if (setting.getValue() instanceof Boolean)
            {
                settings.add(setting.getName(), new JsonPrimitive((Boolean) setting.getValue()));
            } else if (setting.getValue() instanceof Integer)
            {
                settings.add(setting.getName(), new JsonPrimitive((Integer) setting.getValue()));
            } else if (setting.getValue() instanceof Double)
            {
                settings.add(setting.getName(), new JsonPrimitive((Double) setting.getValue()));
            } else if (setting.getValue() instanceof Float)
            {
                settings.add(setting.getName(), new JsonPrimitive((Float) setting.getValue()));
            } else if (setting.getValue() instanceof Enum<?>)
            {
                settings.add(setting.getName(), new JsonPrimitive(((Enum<?>) setting.getValue()).name()));
            } else if (setting.getValue() instanceof Bind)
            {
                JsonObject bind = new JsonObject();
                bind.add("Key", new JsonPrimitive(((Bind) setting.getValue()).getKey()));
                bind.add("Type", new JsonPrimitive(((Bind) setting.getValue()).getType().name()));
                settings.add(setting.getName(), bind);
            }
        }
        module.add("Settings", settings);
        return module;
    }

    @SuppressWarnings(value = "unchecked")
    public void deserialize(JsonObject object)
    {
        if (object.get("Enabled").getAsBoolean())
        {
            this.enable();
        } else
        {
            this.disable();
        }
        JsonObject settings = object.get("Settings").getAsJsonObject();
        for (Setting<?> setting : this.settings)
        {
            JsonElement element = settings.get(setting.getName());
            if (element != null)
            {
                if (setting.getValue() instanceof Boolean)
                {
                    ((Setting<Boolean>) setting).setValue(element.getAsBoolean());
                } else if (setting.getValue() instanceof Integer)
                {
                    ((Setting<Integer>) setting).setValue(element.getAsInt());
                } else if (setting.getValue() instanceof Double)
                {
                    ((Setting<Double>) setting).setValue(element.getAsDouble());
                } else if (setting.getValue() instanceof Float)
                {
                    ((Setting<Float>) setting).setValue(element.getAsFloat());
                } else if (setting.getValue() instanceof Enum<?>)
                {
                    String enumName = element.getAsString();
                    for (Enum<?> e : ((Setting<Enum<?>>) setting).getValue().getClass().getEnumConstants())
                    {
                        if (e.name().equalsIgnoreCase(enumName))
                        {
                            ((Setting<Enum<?>>) setting).setValue(e);
                            break;
                        }
                    }
                } else if (setting.getValue() instanceof Bind)
                {
                    JsonObject bind = element.getAsJsonObject();
                    int key = bind.get("Key").getAsInt();
                    Bind.BindType type = null;
                    String enumName = bind.get("Type").getAsString();
                    for (Bind.BindType e : Bind.BindType.values())
                    {
                        if (e.name().equalsIgnoreCase(enumName))
                        {
                            type = e;
                        }
                    }
                    ((Setting<Bind>) setting).getValue().setKey(key, type);
                }
            }
        }
    }

    public int getUpdatePriority()
    {
        return 0;
    }

    public final Category getCategory()
    {
        return this.category;
    }

    public final String getDescription()
    {
        return this.description;
    }

    public final String getArrayListInfo()
    {
        return this.arrayListInfo.get();
    }

    public final boolean isEnabled()
    {
        return this.enabled;
    }

    public final List<Setting<?>> getSettings()
    {
        return this.settings;
    }

    public final Setting<Bind> getToggleBind()
    {
        return this.toggleBind;
    }

    public final Setting<BindMode> getBindMode()
    {
        return this.bindMode;
    }

    public final Setting<Boolean> getDrawn()
    {
        return this.drawn;
    }

    public void setArrayListInfo(Supplier<String> arrayListInfo)
    {
        this.arrayListInfo = arrayListInfo;
    }

    public enum BindMode
    {
        Normal,
        Hold
    }

    public enum Category
    {
        Client("me.ht9.end.feature.module.client", "e"),
        Combat("me.ht9.end.feature.module.combat", "a"),
        Exploit("me.ht9.end.feature.module.exploit", "d"),
        Misc("me.ht9.end.feature.module.misc", "c"),
        Movement("me.ht9.end.feature.module.movement", "b"),
        Render("me.ht9.end.feature.module.render", "g"),
        Hidden("me.ht9.end.feature.module.hidden", null);

        private final String packageName;
        private final String iconId;

        Category(String packageName, String iconId)
        {
            this.packageName = packageName;
            this.iconId = iconId;
        }

        public String getPackage()
        {
            return this.packageName;
        }

        public String getIconId()
        {
            return this.iconId;
        }

        public List<Module> getModulesInCategory()
        {
            List<Module> modules = new ArrayList<>();
            Registries.getInstance().getModules().forEach(m ->
            {
                if (m.getCategory() == this)
                {
                    modules.add(m);
                }
            });
            return modules;
        }

        public static Optional<Category> matchCategory(String packageName)
        {
            for (Category category : Category.values())
            {
                if (packageName.startsWith(category.getPackage()))
                {
                    return Optional.of(category);
                }
            }
            return Optional.empty();
        }
    }
}
