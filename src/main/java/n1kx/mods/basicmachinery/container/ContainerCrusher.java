package n1kx.mods.basicmachinery.container;

import n1kx.mods.basicmachinery.tileentity.TileEntityCrusher;
import n1kx.mods.basicmachinery.util.generics.GenericContainer;
import n1kx.mods.basicmachinery.util.generics.slot.GenericFuelSlot;
import n1kx.mods.basicmachinery.util.generics.slot.GenericInputSlot;
import n1kx.mods.basicmachinery.util.generics.slot.GenericOutputSlot;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCrusher extends GenericContainer {

    public ContainerCrusher( InventoryPlayer playerInventory , TileEntityCrusher tileEntity ) {
        super( playerInventory , tileEntity );

        IItemHandler handler = this.tileEntity.getCapability( CapabilityItemHandler.ITEM_HANDLER_CAPABILITY , null );
        if( handler != null ) {
            super.addSlotToContainer( new GenericInputSlot( handler , 0 , 90 , 17 , tileEntity.recipes ) );
            super.addSlotToContainer( new GenericFuelSlot( handler , 1 , 12 , 53 ) );
            super.addSlotToContainer( new GenericOutputSlot( handler , 2 , 90 , 53 ) );
        }

        super.addInventorySlots();
    }

}