package me.ht9.end.mixin.mixins;

import me.ht9.end.feature.module.client.hud.HUD;
import me.ht9.end.mixin.accessors.IGuiChest;
import me.ht9.end.util.InventoryUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(GuiScreen.class)
public class MixinGuiContainer
{
    @Shadow protected Minecraft mc;
    @Shadow protected List controlList;
    @Shadow public int height;
    @Shadow public int width;
    @Unique
    private GuiButton button;

    @Inject(method = "initGui", at = @At("HEAD"))
    public void initGui(CallbackInfo ci)
    {
        if (mc.currentScreen instanceof GuiChest)
        {
            GuiChest chest = (GuiChest) mc.currentScreen;
            if (((IGuiChest) chest).getLowerChestInventory().getInvName().equals("Minecart"))
            {
                controlList.add(this.button = new GuiButton(1000, width / 2 - 52, height / 4 - 45, 50, 20, "Steal"));
                controlList.add(new GuiButton(1001, width / 2 + 2, height / 4 - 45, 50, 20, "Store"));
            }
        }
    }

    @Inject(method = "actionPerformed", at = @At("TAIL"))
    private void actionPerformed0(final GuiButton button, final CallbackInfo callbackInfo)
    {
        if (mc.currentScreen instanceof GuiChest)
        {
            GuiChest chest = (GuiChest) mc.currentScreen;
            if (((IGuiChest) chest).getLowerChestInventory().getInvName().equals("Minecart"))
            {
                if (this.button.id == 1000)
                {
                    HUD.getInstance().enable();
//                    List<Slot> slots = new ArrayList<>();
//                    int container = mc.thePlayer.inventorySlots.slots.size() - 36;
//
//                    for (int i = 0; i < chest.inventorySlots.slots.size() - 35; i++)
//                    {
//                        Slot slot = (Slot) chest.inventorySlots.slots.get(i);
//                        ItemStack stack = slot.method_472();
//                        if (stack != null)
//                        {
//                            slots.add(slot);
//                        }
//                    }
//
//                    for (Slot slot : slots)
//                    {
//                        mc.playerController.method_1708(chest.inventorySlots.windowId, slot.slotNumber, 0, false, mc.thePlayer);
//                        mc.playerController.method_1708(chest.inventorySlots.windowId, -999, 0, false, mc.thePlayer);
//                    }

                } else if (this.button.id == 1001)
                {
                    System.out.println("2");
                }
            }
        }
    }
}
