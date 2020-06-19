package n1kx.mods.basicmachinery.container;

import n1kx.mods.basicmachinery.tileentity.TileEntityCrusher;
import n1kx.mods.basicmachinery.util.generics.GenericContainer;
import n1kx.mods.basicmachinery.util.generics.slot.GenericFuelSlot;
import n1kx.mods.basicmachinery.util.generics.slot.GenericInputSlot;
import n1kx.mods.basicmachinery.util.generics.slot.GenericOutputSlot;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;

public class ContainerCrusher extends GenericContainer {

    private final TileEntityCrusher tileEntity;
    private int timeLeft;
    private int totalTimeNeeded;
    private int burnTimeLeft;
    private int totalBurnTime;

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

            if( this.timeLeft != this.tileEntity.getField( 0 ) ) listener.sendWindowProperty( this , 0 , this.tileEntity.getField( 0 ) );
            if( this.totalTimeNeeded != this.tileEntity.getField( 1 ) ) listener.sendWindowProperty( this , 1 , this.tileEntity.getField( 1 ) );
            if( this.burnTimeLeft != this.tileEntity.getField( 2 ) ) listener.sendWindowProperty( this , 2 , this.tileEntity.getField( 2 ) );
            if( this.totalBurnTime != this.tileEntity.getField( 3 ) ) listener.sendWindowProperty( this , 3 , this.tileEntity.getField( 3 ) );
        }

        this.timeLeft = this.tileEntity.getField( 0 );
        this.totalTimeNeeded = this.tileEntity.getField( 1 );
        this.burnTimeLeft = this.tileEntity.getField( 2 );
        this.totalBurnTime = this.tileEntity.getField( 3 );
    }
}