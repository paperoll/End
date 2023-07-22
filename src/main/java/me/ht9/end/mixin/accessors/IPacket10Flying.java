package me.ht9.end.mixin.accessors;


import net.minecraft.src.Packet10Flying;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Packet10Flying.class)
public interface IPacket10Flying
{
    @Accessor(value = "yaw")
    void setYaw(float yaw);

    @Accessor(value = "pitch")
    void setPitch(float pitch);
}
