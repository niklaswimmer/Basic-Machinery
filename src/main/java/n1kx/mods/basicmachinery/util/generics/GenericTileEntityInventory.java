package n1kx.mods.basicmachinery.util.generics;

import n1kx.mods.basicmachinery.util.IRecipes;
import n1kx.mods.basicmachinery.util.Methods;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class GenericTileEntityInventory extends TileEntity implements IInventory {

    protected final NonNullList<ItemStack> inventory;
    protected final ItemStackHandler inputHandler, fuelHandler, outputHandler;

    public final int inputSlots;
    public final int outputSlots;
    public final int fuelSlots;
    public final int inventorySize;

    public final GenericBlock block;

    protected final String customName;

    public final IRecipes recipes;

    public final int guiID;

    /**
     * constructor
     * @param inputSlots the number of input slots (does not include fuel slots)
     * @param outputSlots the number of output slots
     * @param fuelSlots the number of fuel slots
     * @param block the block this tile entity belongs to
     * @param recipes the recipes for this tile entity (can be null, e.g. a chest)
     */
    protected GenericTileEntityInventory( int inputSlots , int outputSlots , int fuelSlots , GenericBlock block , @Nullable IRecipes recipes ) {
        this( inputSlots , outputSlots , fuelSlots , block , recipes , null );
    }

    /**
     * constructor
     * @param inputSlots the number of input slots (does not include fuel slots)
     * @param outputSlots the number of output slots
     * @param fuelSlots the number of fuel slots
     * @param block the block this tile entity belongs to
     * @param recipes the recipes for this tile entity (can be null, e.g. a chest)
     * @param customName the custom name for this tile entity (can be null; in case of null the unlocalized name of the block gets used)
     */
    protected GenericTileEntityInventory( int inputSlots , int outputSlots , int fuelSlots , GenericBlock block , @Nullable IRecipes recipes , @Nullable String customName ) {
        this.inputSlots = inputSlots;
        this.outputSlots = outputSlots;
        this.fuelSlots = fuelSlots;
        this.inventorySize = this.inputSlots + this.outputSlots + this.fuelSlots;

        this.block = block;

        this.recipes = recipes;

        this.guiID = block.guiID;

        this.customName = Methods.newUnlocalizedName( customName );

        this.inventory = NonNullList.withSize( this.inventorySize , ItemStack.EMPTY );
        this.inputHandler = new ItemStackHandler( this.inputSlots );
        this.fuelHandler = new ItemStackHandler( this.fuelSlots );
        this.outputHandler = new ItemStackHandler( this.outputSlots );
    }

    @Override
    public int getSizeInventory() {
        return this.inventorySize;
    }

    @Override
    public boolean isEmpty() {
        for( ItemStack stack : this.inventory ) {
            if( !stack.isEmpty() ) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot( int index ) {
        return this.inventory.get( index );
    }

    @Override
    public ItemStack decrStackSize( int index , int count ) {
        return ItemStackHelper.getAndSplit( inventory , index , count );
    }

    @Override
    public ItemStack removeStackFromSlot( int index ) {
        return ItemStackHelper.getAndRemove( inventory , index );
    }

    @Override
    public void setInventorySlotContents( int index , ItemStack stack ) {
        this.inventory.set( index , stack );
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer( EntityPlayer player ) {
        return this.world.getTileEntity( this.pos ) == this && player.getDistanceSq( (double)this.pos.getX() + 0.5 , (double)this.pos.getY() + 0.5 , (double)this.pos.getZ() + 0.5 ) <= 64.0;
    }

    @Override
    public void openInventory( EntityPlayer player ) {}

    @Override
    public void closeInventory( EntityPlayer player ) {}

    @Override
    public boolean isItemValidForSlot( int index , ItemStack stack ) {
        return index < this.inputSlots;
    }

    @Override
    public abstract int getField( int id );

    @Override
    public abstract void setField( int id , int value );

    @Override
    public abstract int getFieldCount();

    @Override
    public void clear() {
        this.inventory.clear();
    }

    @Override
    public String getName() {
        return ( this.customName != null ? this.customName : this.block.getUnlocalizedName() );
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null;
    }

    @Override
    public abstract void readFromNBT( NBTTagCompound compound );

    @Override
    public abstract NBTTagCompound writeToNBT( NBTTagCompound compound );

    public NBTTagCompound writeInventory( NBTTagCompound nbt ) {
        //TODO change this to not use ItemStackHandler
        nbt.setTag( "itemsIn" , inputHandler.serializeNBT());
        nbt.setTag( "itemsFuel" , fuelHandler.serializeNBT() );
        nbt.setTag("itemsOut", outputHandler.serializeNBT());
        return nbt;
    }

    public void readInventory(NBTTagCompound nbt) {
        //TODO change this to not use ItemStackHandler
        if( nbt.hasKey( "itemsIn" ) ) {
            inputHandler.deserializeNBT( (NBTTagCompound)nbt.getTag( "itemsIn" ) );
        }
        if( nbt.hasKey( "itemsFuel" ) ) {
            fuelHandler.deserializeNBT( (NBTTagCompound)nbt.getTag( "itemsFuel" ) );
        }
        if( nbt.hasKey( "itemsOut" ) ) {
            outputHandler.deserializeNBT( (NBTTagCompound)nbt.getTag( "itemsOut" ) );
        }
    }

    public boolean hasFuelSlots() {
        return this.fuelSlots > 0;
    }

    public boolean hasInputSlots() {
        return this.inputSlots > 0;
    }

    public int getGuiID() {
        return this.guiID;
    }
}
