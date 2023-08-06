package me.ht9.end.util;

import net.fabricmc.loader.FabricLoaderImpl;
import net.minecraft.client.Minecraft;

public interface Globals
{
    Minecraft mc = (Minecraft) FabricLoaderImpl.getInstance().getGameInstance();
}
