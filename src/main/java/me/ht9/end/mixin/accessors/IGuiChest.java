package me.ht9.end.mixin.accessors;

import net.minecraft.src.GuiChest;
import net.minecraft.src.IInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiChest.class)
public interface IGuiChest
{
    @Accessor("lowerChestInventory")
    IInventory getLowerChestInventory();
}
