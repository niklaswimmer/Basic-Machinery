package n1kx.mods.basicmachinery.util;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GenericTileEntity extends TileEntity implements IInventory {

    private final NonNullList<ItemStack> inventory;

    public final int inputSlots;
    public final int outputSlots;
    public final int fuelSlots;
    public final int inventorySize;

    private final Block block;

    private final String customName;

    public final IRecipe recipes;

    /**
     * constructor
     * @param inputSlots the number of input slots (does not include fuel slots)
     * @param outputSlots the number of output slots
     * @param fuelSlots the number of fuel slots
     * @param block the block this tile entity belongs to
     * @param recipes the recipes for this tile entity (can be null, e.g. a chest)
     */
    public GenericTileEntity( int inputSlots , int outputSlots , int fuelSlots , Block block , @Nullable IRecipe recipes ) {
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
    public GenericTileEntity( int inputSlots , int outputSlots , int fuelSlots , Block block , @Nullable IRecipe recipes , @Nullable String customName ) {
        this.inputSlots = inputSlots;
        this.outputSlots = outputSlots;
        this.fuelSlots = fuelSlots;
        this.inventorySize = this.inputSlots + this.outputSlots + this.fuelSlots;

        this.block = block;

        this.recipes = recipes;

        this.customName = Methods.newUnlocalizedName( customName );

        this.inventory = NonNullList.withSize( this.inventorySize , ItemStack.EMPTY );
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
        //TODO change this to actually work
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
    public int getField( int id ) {
        //TODO don't forget about this
        return 0;
    }

    @Override
    public void setField( int id , int value ) {
        //TODO don't forget about this
    }

    @Override
    public int getFieldCount() {
        //TODO don't forget about this
        return 0;
    }

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
    public void readFromNBT( NBTTagCompound compound ) {
        super.readFromNBT( compound );
    }

    @Override
    public NBTTagCompound writeToNBT( NBTTagCompound compound ) {
        return super.writeToNBT( compound );
    }
}
