package me.ht9.end.util;

import net.minecraft.src.NetClientHandler;
import net.minecraft.src.Packet;

public final class NetworkUtils implements Globals
{
    public static void dispatchPacket(Packet packet)
    {
        NetClientHandler netHandlerPlayClient = mc.method_2145();
        if (netHandlerPlayClient != null)
        {
            netHandlerPlayClient.addToSendQueue(packet);
        }
    }
}
