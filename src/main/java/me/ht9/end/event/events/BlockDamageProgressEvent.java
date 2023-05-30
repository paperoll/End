package me.ht9.end.event.events;

import me.ht9.end.event.Event;

public final class BlockDamageProgressEvent extends Event
{
    private float progress;

    public BlockDamageProgressEvent(float progress)
    {
        this.progress = progress;
    }

    public float getProgress()
    {
        return this.progress;
    }

    public void setProgress(float progress)
    {
        this.progress = progress;
    }
}
