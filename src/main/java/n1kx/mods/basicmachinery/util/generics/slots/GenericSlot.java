package n1kx.mods.basicmachinery.util.generics.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class GenericSlot extends Slot {

    public GenericSlot( IInventory inventoryIn , int index , int xPosition , int yPosition ) {
        super( inventoryIn , index , xPosition , yPosition );
    }

}
