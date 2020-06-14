package n1kx.mods.basicmachinery.container;

import n1kx.mods.basicmachinery.tileentity.TileEntityCrusher;
import n1kx.mods.basicmachinery.util.generics.GenericContainer;
import n1kx.mods.basicmachinery.util.generics.IHasFields;
import n1kx.mods.basicmachinery.util.generics.slot.GenericFuelSlot;
import n1kx.mods.basicmachinery.util.generics.slot.GenericInputSlot;
import n1kx.mods.basicmachinery.util.generics.slot.GenericOutputSlot;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;

public class ContainerCrusher extends GenericContainer implements IHasFields {

    private final TileEntityCrusher tileEntity;
    private int progressLeft;
    private int progress;
    private int burnTimeLeft;
    private int burnTime;

    public ContainerCrusher( InventoryPlayer playerInventory , TileEntityCrusher tileEntity ) {
        super( playerInventory , tileEntity );

        this.tileEntity = tileEntity;

        super.addSlotToContainer( new GenericInputSlot( tileEntity , 0 , 90 , 17 , tileEntity.recipes ) );
        super.addSlotToContainer( new GenericFuelSlot( tileEntity , 1 , 12 , 53 ) );
        super.addSlotToContainer( new GenericOutputSlot( tileEntity , 2 , 90 , 53 ) );

        super.addInventorySlots();
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for(int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener listener = this.listeners.get(i);

            if( this.progressLeft != this.tileEntity.getField( 0 ) ) listener.sendWindowProperty( this , 0 , this.tileEntity.getField( 0 ) );
            if( this.progress != this.tileEntity.getField( 1 ) ) listener.sendWindowProperty( this , 1 , this.tileEntity.getField( 1 ) );
            if( this.burnTimeLeft != this.tileEntity.getField( 2 ) ) listener.sendWindowProperty( this , 2 , this.tileEntity.getField( 2 ) );
            if( this.burnTime != this.tileEntity.getField( 3 ) ) listener.sendWindowProperty( this , 3 , this.tileEntity.getField( 3 ) );
        }

        this.progressLeft = this.tileEntity.getField( 0 );
        this.progress = this.tileEntity.getField( 1 );
        this.burnTimeLeft = this.tileEntity.getField( 2 );
        this.burnTime = this.tileEntity.getField( 3 );
    }
}