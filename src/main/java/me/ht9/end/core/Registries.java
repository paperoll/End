package me.ht9.end.core;

import me.ht9.end.End;
import me.ht9.end.feature.Feature;
import me.ht9.end.feature.module.Module;
import me.ht9.end.feature.module.binding.Bind;
import me.ht9.end.feature.module.client.clickgui.ClickGui;
import me.ht9.end.feature.module.client.hud.HUD;
import me.ht9.end.feature.module.combat.aura.Aura;
import me.ht9.end.feature.module.combat.velocity.Velocity;
import me.ht9.end.feature.module.exploit.crasher.Crasher;
import me.ht9.end.feature.module.exploit.instantmine.InstantMine;
import me.ht9.end.feature.module.exploit.nofall.NoFall;
import me.ht9.end.feature.module.exploit.secretclose.SecretClose;
import me.ht9.end.feature.module.misc.antiaim.AntiAim;
import me.ht9.end.feature.module.misc.freecam.Freecam;
import me.ht9.end.feature.module.misc.norotate.NoRotate;
import me.ht9.end.feature.module.misc.nuker.Nuker;
import me.ht9.end.feature.module.movement.pvpspeed.PVPSpeed;
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

    private static final List<Module> modules = new ArrayList<>();
    private final Registry<Bind> binds = new Registry<>();

    private Registries()
    {
    }


    public void init() {
        // Client
//        this.modules.registerFeature(ClickGui.getInstance());
        this.modules.add(HUD.getInstance());
        this.modules.add(me.ht9.end.feature.module.client.arraylist.ArrayList.getInstance());
        // Combat
        this.modules.add(Aura.getInstance());
        this.modules.add(Velocity.getInstance());

        // Misc
        this.modules.add(AntiAim.getInstance());
        this.modules.add(Freecam.getInstance());
        this.modules.add(NoRotate.getInstance());
        this.modules.add(Nuker.getInstance());
        // Exploit
        this.modules.add(Crasher.getInstance());
        this.modules.add(InstantMine.getInstance());
        this.modules.add(NoFall.getInstance());
        this.modules.add(SecretClose.getInstance());
        // Movement
        this.modules.add(Speed.getInstance());
        this.modules.add(PVPSpeed.getInstance());
        // Render
        this.modules.add(ESP.getInstance());
        this.modules.add(TrueSight.getInstance());

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

    public List<Module> getModules()
    {
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
