package me.ht9.end.event.events;

import me.ht9.end.event.Event;

public final class BlockHitDelayEvent extends Event
{
    private int blockHitDelay;

    public BlockHitDelayEvent(int blockHitDelay)
    {
        this.blockHitDelay = blockHitDelay;
    }

    public int getBlockHitDelay()
    {
        return this.blockHitDelay;
    }

    public void setBlockHitDelay(int blockHitDelay)
    {
        this.blockHitDelay = blockHitDelay;
    }
}
