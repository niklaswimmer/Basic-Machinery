package n1kx.mods.basicmachinery.util.generics.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import mcp.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GenericOutputSlot extends GenericSlot {

    public GenericOutputSlot( IInventory inventoryIn , int index , int xPosition , int yPosition ) {
        super( inventoryIn , index , xPosition , yPosition );
    }

    @Override
    public ItemStack onTake( EntityPlayer thePlayer , ItemStack stack ) {
        super.onCrafting( stack );
        return super.onTake( thePlayer , stack );
    }

    @Override
    public boolean isItemValid( ItemStack stack ) {
        return false;
    }
}
