package n1kx.mods.basicmachinery.util.generics.slot;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class GenericSlot extends SlotItemHandler {

    public GenericSlot( IItemHandler handler , int index , int xPosition , int yPosition ) {
        super( handler , index , xPosition , yPosition );
    }

}
