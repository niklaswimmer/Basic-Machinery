package n1kx.mods.basicmachinery.container;

import n1kx.mods.basicmachinery.tileentity.TileEntityCrusher;
import n1kx.mods.basicmachinery.util.generics.GenericContainer;
import n1kx.mods.basicmachinery.util.generics.IHasFields;
import n1kx.mods.basicmachinery.util.generics.slots.GenericFuelSlot;
import n1kx.mods.basicmachinery.util.generics.slots.GenericInputSlot;
import n1kx.mods.basicmachinery.util.generics.slots.GenericOutputSlot;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.tileentity.TileEntity;

public class ContainerCrusher extends GenericContainer implements IHasFields {

    private final TileEntityCrusher tileEntity;
    private int progress;

    public ContainerCrusher( InventoryPlayer playerInventory , TileEntityCrusher tileEntity ) {
        super( playerInventory , tileEntity );

        this.tileEntity = tileEntity;

        super.addSlotToContainer( new GenericInputSlot( tileEntity , 0 , 56 , 17 , tileEntity.recipes ) );
        super.addSlotToContainer( new GenericFuelSlot( tileEntity , 1 , 56 , 53 ) );
        super.addSlotToContainer( new GenericOutputSlot( tileEntity , 2 , 116 , 35 ) );

        super.addInventorySlots();
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for(int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener listener = this.listeners.get(i);

            if( this.progress != this.tileEntity.getField( 0 ) ) listener.sendWindowProperty( this , 0 , this.tileEntity.getField( 0 ) );
        }

        this.progress = this.tileEntity.getField( 0 );
    }
}