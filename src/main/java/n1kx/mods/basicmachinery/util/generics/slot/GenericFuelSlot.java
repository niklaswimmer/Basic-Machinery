package n1kx.mods.basicmachinery.util.generics.slot;

import n1kx.mods.basicmachinery.util.Methods;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GenericFuelSlot extends GenericSlot {

    public GenericFuelSlot( IItemHandler handler , int index , int xPosition , int yPosition ) {
        super( handler , index , xPosition , yPosition );
    }

    @Override
    public boolean isItemValid( ItemStack stack ) {
        return Methods.isFuel( stack );
    }
}
