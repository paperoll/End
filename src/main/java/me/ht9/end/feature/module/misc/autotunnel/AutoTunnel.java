package me.ht9.end.feature.module.misc.autotunnel;

import me.ht9.end.feature.module.Module;
import me.ht9.end.feature.module.annotation.Description;

import java.util.Arrays;

@Description(value = "Automatically tunnels forward for you.")
public class AutoTunnel extends Module
{
    private static final AutoTunnel instance = new AutoTunnel();

    private AutoTunnel()
    {
    }

    public boolean isExposed(int x, int y, int z)
    {
        return Arrays.stream(new int[][]
                {
                        {x + 1, y, z},
                        {x - 1, y, z},
                        {x, y, z + 1},
                        {x, y, z - 1},
                        {x, y + 1, z},
                        {x, y - 1, z}
                })
                .anyMatch(coord -> mc.theWorld.isAirBlock(coord[0], coord[1], coord[2]));
    }

    public static AutoTunnel getInstance()
    {
        return instance;
    }
}
