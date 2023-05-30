package me.ht9.end.event.events;

import me.ht9.end.event.Event;

public final class RenderWorldPassEvent extends Event
{
    private final float partialTicks;

    public RenderWorldPassEvent(float partialTicks)
    {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks()
    {
        return this.partialTicks;
    }
}
