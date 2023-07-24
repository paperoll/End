package me.ht9.end.event.events;

import me.ht9.end.event.Event;
import net.minecraft.src.Entity;

public final class PushByLiquidEvent extends Event
{
    private final Entity pushee;

    public PushByLiquidEvent(Entity pushee)
    {
        this.pushee = pushee;
    }

    public Entity getPushee()
    {
        return this.pushee;
    }
}
