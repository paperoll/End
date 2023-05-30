package me.ht9.end.event.events;

import me.ht9.end.event.Event;

public final class PlayerDamageBlockEvent extends Event
{
    private final int x, y, z, facing;

    public PlayerDamageBlockEvent(int x, int y, int z, int facing)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.facing = facing;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public int getZ()
    {
        return this.z;
    }

    public int getFacing()
    {
        return this.facing;
    }
}
