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

    public static int getOpenSlotInChest(int slot)
    {
        int openslot = -1;
        for (int i = 0; i < slot; i++)
        {
            if (mc.thePlayer.inventorySlots.method_2084(i).method_472() == null)
            {
                openslot = i;
                return openslot;
            }
        }
        return openslot;
    }

    public static int getOpenSlotInInventory(int slot)
    {
        int openSlot = -1;
        for (int i = mc.thePlayer.inventorySlots.slots.size() - 1; i >= slot; i--)
        {

            if (mc.thePlayer.inventorySlots.method_2084(i).method_472() == null)
            {
                openSlot = i;
                return openSlot;
            }
        }
        return openSlot;
    }
}