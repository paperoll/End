package me.ht9.end.util;

public final class Timer implements Globals
{
    private long time;

    public Timer()
    {
        this.time = System.currentTimeMillis();
    }

    public boolean hasReached(double ms, boolean reset)
    {
        if (reset)
        {
            this.reset();
        }
        return System.currentTimeMillis() - this.time >= ms;
    }

    public long getTime()
    {
        return System.currentTimeMillis() - this.time;
    }

    public void reset()
    {
        this.time = System.currentTimeMillis();
    }
}