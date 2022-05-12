package net.blightbuster.kiln;

import net.blightbuster.SkyutilsMod;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class KilnInputSlot extends Slot {


    public KilnInputSlot(Inventory inventory, int invSlot, int xPosition, int yPosition) {
        super(inventory, invSlot, xPosition, yPosition);
    }

    public boolean canInsert(ItemStack stack) {
        return (stack.getItem() == SkyutilsMod.RAW_CRUCIBLE) || (stack.getItem() == Items.COBBLESTONE);
    }
}
