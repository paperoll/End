package me.ht9.end.event.events;

import me.ht9.end.event.Event;
import net.minecraft.src.Entity;

public final class PushByEntityEvent extends Event
{
    private final Entity pushee;

    public PushByEntityEvent(Entity pushee)
    {
        this.pushee = pushee;
    }

    public Entity getPushee()
    {
        return this.pushee;
    }
}
