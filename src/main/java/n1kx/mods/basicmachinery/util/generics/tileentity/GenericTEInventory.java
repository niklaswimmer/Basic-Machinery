package n1kx.mods.basicmachinery.util.generics.tileentity;

import n1kx.mods.basicmachinery.util.IDropItemsOnBreak;
import n1kx.mods.basicmachinery.util.IRecipes;
import n1kx.mods.basicmachinery.util.Methods;
import n1kx.mods.basicmachinery.util.generics.GenericBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class GenericTEInventory extends TileEntity implements IInventory , IDropItemsOnBreak {

    protected final ItemStackHandler inputHandler, fuelHandler, outputHandler;
    protected final CombinedInvWrapper combinedHandler;

    public final int inputSlots;
    public final int outputSlots;
    public final int fuelSlots;
    public final int inventorySize;

    public final GenericBlock block;

    protected final String customName;

    public final IRecipes recipes;

    /**
     * constructor
     * @param inputSlots the number of input slots (does not include fuel slots)
     * @param outputSlots the number of output slots
     * @param fuelSlots the number of fuel slots
     * @param block the block this tile entity belongs to
     * @param recipes the recipes for this tile entity (can be null, e.g. a chest)
     */
    protected GenericTEInventory( int inputSlots , int outputSlots , int fuelSlots , GenericBlock block , @Nullable IRecipes recipes ) {
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
    protected GenericTEInventory( int inputSlots , int outputSlots , int fuelSlots , GenericBlock block , @Nullable IRecipes recipes , @Nullable String customName ) {
        this.inputSlots = inputSlots;
        this.outputSlots = outputSlots;
        this.fuelSlots = fuelSlots;
        this.inventorySize = this.inputSlots + this.outputSlots + this.fuelSlots;

        this.block = block;

        this.recipes = recipes;

        this.customName = Methods.newUnlocalizedName( customName );

        this.inputHandler = new ItemStackHandler( this.inputSlots ) {
            @Override
            protected void onContentsChanged( int slot ) {
                GenericTEInventory.super.markDirty();
            }
        };
        this.fuelHandler = new ItemStackHandler( this.fuelSlots ) {
            @Override
            protected void onContentsChanged( int slot ) {
                GenericTEInventory.super.markDirty();
            }
        };
        this.outputHandler = new ItemStackHandler( this.outputSlots ) {
            @Override
            protected void onContentsChanged( int slot ) {
                GenericTEInventory.super.markDirty();
            }
        };

        this.combinedHandler = new CombinedInvWrapper( this.inputHandler , this.fuelHandler , this.outputHandler );
    }

    @Override
    public void readFromNBT( NBTTagCompound compound ) {
        this.readInventory( compound );
        super.readFromNBT( compound );
    }

    @Override
    public NBTTagCompound writeToNBT( NBTTagCompound compound ) {
        compound = this.writeInventory( compound );
        return super.writeToNBT( compound );
    }

    public NBTTagCompound writeInventory( NBTTagCompound nbt ) {
        nbt.setTag( "itemsIn" , inputHandler.serializeNBT() );
        nbt.setTag( "itemsFuel" , fuelHandler.serializeNBT() );
        nbt.setTag( "itemsOut" , outputHandler.serializeNBT() );
        return nbt;
    }

    public void readInventory(NBTTagCompound nbt) {
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

    @Override
    public int getSizeInventory() {
        return this.inventorySize;
    }

    @Override
    public boolean isEmpty() {
        for( int i = 0 ; i < this.inputHandler.getSlots() ; i++ ) {
            if( !this.inputHandler.getStackInSlot( i ).isEmpty() ) {
                return false;
            }
        }
        for( int i = 0 ; i < this.fuelHandler.getSlots() ; i++ ) {
            if( !this.fuelHandler.getStackInSlot( i ).isEmpty() ) {
                return false;
            }
        }
        for( int i = 0 ; i < this.outputHandler.getSlots() ; i++ ) {
            if( !this.outputHandler.getStackInSlot( i ).isEmpty() ) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot( int index ) {
        ItemStack returnStack = ItemStack.EMPTY;
        if( index < this.inputSlots ) {
            returnStack = this.inputHandler.getStackInSlot( index );
        }
        else if( index < this.inputSlots + this.fuelSlots ) {
            returnStack = this.fuelHandler.getStackInSlot( index - this.inputSlots );
        }
        else if( index < this.inputSlots + this.fuelSlots + this.outputSlots ) {
            returnStack = this.outputHandler.getStackInSlot( index - this.inputSlots - this.fuelSlots );
        }
        return returnStack;
    }

    @Override
    public ItemStack decrStackSize( int index , int count ) {
        ItemStack returnStack = ItemStack.EMPTY;
        if( index < this.inputSlots ) {
            returnStack = this.inputHandler.extractItem( index , count , false );
        }
        else if( index < this.inputSlots + this.fuelSlots ) {
            returnStack = this.fuelHandler.extractItem( index - this.inputSlots , count , false );
        }
        else if( index < this.inputSlots + this.fuelSlots + this.outputSlots ) {
            returnStack = this.outputHandler.extractItem( index - this.inputSlots - this.fuelSlots , count , false );
        }
        return returnStack;
    }

    @Override
    public ItemStack removeStackFromSlot( int index ) {
        ItemStack returnStack = ItemStack.EMPTY;
        if( index < this.inputSlots ) {
            returnStack = this.inputHandler.extractItem( index , this.inputHandler.getStackInSlot( index ).getCount() , false );
            this.inputHandler.setStackInSlot( index , ItemStack.EMPTY );
        }
        else if( index < this.inputSlots + this.fuelSlots ) {
            returnStack = this.fuelHandler.extractItem( index - this.inputSlots , this.fuelHandler.getStackInSlot( index ).getCount() , false );
            this.fuelHandler.setStackInSlot( index - this.inputSlots , ItemStack.EMPTY );
        }
        else if( index < this.inputSlots + this.fuelSlots + this.outputSlots ) {
            returnStack = this.outputHandler.extractItem( index - this.inputSlots - this.fuelSlots , this.outputHandler.getStackInSlot( index ).getCount() , false );
            this.outputHandler.setStackInSlot( index - this.inputSlots - this.fuelSlots , ItemStack.EMPTY );
        }
        return returnStack;
    }

    @Override
    public void setInventorySlotContents( int index , ItemStack stack ) {
        if( index < this.inputSlots ) {
            this.inputHandler.setStackInSlot( index , stack );
        }
        else if( index < this.inputSlots + this.fuelSlots ) {
            this.fuelHandler.setStackInSlot( index - this.inputSlots , stack );
        }
        else if( index < this.inputSlots + this.fuelSlots + this.outputSlots ) {
            this.outputHandler.setStackInSlot( index - this.inputSlots - this.fuelSlots , stack );
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer( EntityPlayer player ) {
        return super.world.getTileEntity( super.pos ) == this && player.getDistanceSq( (double)super.pos.getX() + 0.5 , (double)super.pos.getY() + 0.5 , (double)super.pos.getZ() + 0.5 ) <= 64.0;
    }

    @Override
    public void openInventory( EntityPlayer player ) {}

    @Override
    public void closeInventory( EntityPlayer player ) {}

    @Override
    public boolean isItemValidForSlot( int index , ItemStack stack ) {
        return index < this.inputSlots + this.fuelSlots;
    }

    @Override
    public abstract int getField( int id );

    @Override
    public abstract void setField( int id , int value );

    @Override
    public abstract int getFieldCount();

    @Override
    public void clear() {
        for( int i = 0 ; i < this.inputHandler.getSlots() ; i++ ) {
            this.inputHandler.setStackInSlot( i , ItemStack.EMPTY );
        }
        for( int i = 0 ; i < this.fuelHandler.getSlots() ; i++ ) {
            this.fuelHandler.setStackInSlot( i , ItemStack.EMPTY );
        }
        for( int i = 0 ; i < this.outputHandler.getSlots() ; i++ ) {
            this.outputHandler.setStackInSlot( i , ItemStack.EMPTY );
        }
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : Objects.requireNonNull( this.block.getRegistryName() ).getResourcePath();
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null;
    }

    @Override
    public void dropInventoryItems( World worldIn , BlockPos pos ) {
        InventoryHelper.dropInventoryItems( worldIn , pos , this );
    }

    @Override
    public boolean shouldRefresh( World world , BlockPos pos , IBlockState oldState , IBlockState newState ) {
        return oldState.getBlock() != newState.getBlock();
    }
}
