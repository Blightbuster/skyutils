package net.blightbuster.kiln;

import net.blightbuster.SkyutilsMod;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class KilnFuelSlot extends Slot {


    public KilnFuelSlot(Inventory inventory, int invSlot, int xPosition, int yPosition) {
        super(inventory, invSlot, xPosition, yPosition);
    }

    public boolean canInsert(ItemStack stack) {
        return stack.getItem() == SkyutilsMod.CHARCOAL_BLOCK_ITEM.asItem();
    }
}
