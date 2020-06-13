package n1kx.mods.basicmachinery.util.generics.slots;

import n1kx.mods.basicmachinery.util.Methods;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GenericFuelSlot extends GenericSlot {

    public GenericFuelSlot( IInventory inventoryIn , int index , int xPosition , int yPosition ) {
        super( inventoryIn , index , xPosition , yPosition );
    }

    @Override
    public boolean isItemValid( ItemStack stack ) {
        return Methods.isFuel( stack );
    }
}
