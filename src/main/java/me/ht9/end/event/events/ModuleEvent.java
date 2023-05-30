package me.ht9.end.event.events;

import me.ht9.end.event.Event;
import me.ht9.end.feature.module.Module;

public final class ModuleEvent extends Event
{
    private final Module module;
    private final Type type;

    public ModuleEvent(Module module, Type type) {
        this.module = module;
        this.type = type;
    }

    public Module getModule() {
        return this.module;
    }

    public Type getType() {
        return this.type;
    }

    public enum Type {
        ENABLE,
        DISABLE,
        DRAW,
        UNDRAW
    }
}