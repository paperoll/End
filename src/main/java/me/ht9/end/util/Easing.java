package me.ht9.end.util;

public final class Easing
{
    public static float linear(float time, float initial, float target, float duration)
    {
        return (time >= duration) ? initial + target : target * time / duration + initial;
    }

    public static float exponential(float time, float initial, float target, float duration)
    {
        return (time >= duration) ? initial + target : target * ((float) -Math.pow(2, -10 * time / duration) + 1) + initial;
    }
}