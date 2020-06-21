package n1kx.mods.basicmachinery.util.generics.slot;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GenericOutputSlot extends GenericSlot {

    public GenericOutputSlot( IItemHandler handler , int index , int xPosition , int yPosition ) {
        super( handler , index , xPosition , yPosition );
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
