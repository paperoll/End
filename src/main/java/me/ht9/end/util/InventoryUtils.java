package me.ht9.end.util;

import net.minecraft.src.ItemStack;

import java.util.function.Predicate;

public final class InventoryUtils implements Globals
{
    public static final Timer CLICK_TIMER = new Timer();

    public static int getInHotbar(Predicate<? super ItemStack> predicate)
    {
        for (int i = 0; i < 9; i++)
        {
            ItemStack itemStack = mc.thePlayer.inventory.method_954(i); /*getStackInSlot*/
            if (itemStack != null && predicate.test(itemStack))
            {
                return i;
            }
        }
        return -1;
    }

    public static int getInInventory(Predicate<? super ItemStack> predicate)
    {
        for (int i = 9; i < 36; i++)
        {
            ItemStack itemStack = mc.thePlayer.inventory.method_954(i);
            if (itemStack != null && predicate.test(itemStack))
            {
                return i;
            }
        }
        return -1;
    }
}