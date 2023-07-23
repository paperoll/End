package me.ht9.end.core;

import me.ht9.end.End;
import me.ht9.end.feature.Feature;
import me.ht9.end.feature.module.Module;
import me.ht9.end.feature.module.binding.Bind;
import me.ht9.end.feature.module.client.clickgui.ClickGui;
import me.ht9.end.feature.module.client.hud.HUD;
import me.ht9.end.feature.module.exploit.crasher.Crasher;
import me.ht9.end.feature.module.exploit.nofall.NoFall;
import me.ht9.end.feature.module.misc.antiaim.AntiAim;
import me.ht9.end.feature.module.misc.norotate.NoRotate;
import me.ht9.end.feature.module.movement.speed.Speed;
import me.ht9.end.feature.module.render.esp.ESP;
import me.ht9.end.feature.module.render.truesight.TrueSight;
import me.ht9.end.feature.module.setting.Setting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public final class Registries
{
    private static final Registries instance = new Registries();

    private final Registry<Module> modules = new Registry<>();
    private final Registry<Bind> binds = new Registry<>();

    private Registries()
    {
    }


    public void init() {
        // Client
//        this.modules.registerFeature(ClickGui.getInstance());
        this.modules.registerFeature(HUD.getInstance());
        // Combat

        // Misc
        this.modules.registerFeature(AntiAim.getInstance());
        this.modules.registerFeature(NoFall.getInstance());
        this.modules.registerFeature(NoRotate.getInstance());
        // Exploit
        this.modules.registerFeature(Crasher.getInstance());
        // Movement
        this.modules.registerFeature(Speed.getInstance());
        // Render
        this.modules.registerFeature(ESP.getInstance());
        this.modules.registerFeature(TrueSight.getInstance());

        this.modules.forEach(module -> {
            for (Field field : module.getClass().getDeclaredFields()) {
                if (Setting.class.isAssignableFrom(field.getType())) {
                    try {
                        field.setAccessible(true);
                        module.getSettings().add((Setting<?>) field.get(module));
                    } catch (Throwable t) {
                        End.getLogger().error("Failed to instantiate setting " + field.getName() + " : ", t);
                    }
                }
            }
            module.getSettings().add(module.getDrawn());
            module.getSettings().add(module.getBindMode());
            module.getSettings().add(module.getToggleBind());
        });
    }

    public Registry<Module> getModules() {
        return modules;
    }

    public Registry<Bind> getBinds() {
        return this.binds;
    }

    public static Registries getInstance() {
        return instance;
    }

    public static final class Registry<T extends Feature> {
        private final List<T> registeredFeatures = new ArrayList<>();

        public Optional<T> get(String name) {
            for (T feature : this.registeredFeatures) {
                if (feature.getName().equalsIgnoreCase(name)) {
                    return Optional.of(feature);
                }
                for (String alias : feature.getAliases()) {
                    if (alias.equalsIgnoreCase(name))
                    {
                        return Optional.of(feature);
                    }
                }
            }
            return Optional.empty();
        }

        public void sort(Comparator<? super Feature> comparator) {
            this.registeredFeatures.sort(comparator);
        }

        public void registerFeature(T object) {
            this.registeredFeatures.add(object);
        }

        public void forEach(Consumer<? super T> consumer) {
            for (T object : this.registeredFeatures)
            {
                consumer.accept(object);
            }
        }

        public final List<T> getRegisteredFeatures() {
            return this.registeredFeatures;
        }
    }
}
