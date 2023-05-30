package me.ht9.end.event.events;

import me.ht9.end.event.Event;
import net.minecraft.src.Packet;

public final class PacketEvent extends Event
{
    private final Packet packet;
    private final boolean serverBound;

    public PacketEvent(Packet packet, boolean serverBound)
    {
        this.packet = packet;
        this.serverBound = serverBound;
    }

    public Packet getPacket()
    {
        return this.packet;
    }

    public boolean isServerBound()
    {
        return this.serverBound;
    }
}