package me.ht9.end.mixin.accessors;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Session;
import net.minecraft.src.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface IMinecraft
{
    @Accessor(value = "timer")
    Timer getTimer();

    @Accessor(value = "leftClickCounter")
    void setLeftClickCounter(int leftClickCounter);

    @Accessor(value = "session")
    void setSession(Session session);
}
