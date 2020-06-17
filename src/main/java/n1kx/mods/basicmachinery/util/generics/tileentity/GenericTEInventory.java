package n1kx.mods.basicmachinery.util.generics.tileentity;

import n1kx.mods.basicmachinery.util.BooleanHolder;
import n1kx.mods.basicmachinery.util.IDropItemsOnBreak;
import n1kx.mods.basicmachinery.util.IRecipes;
import n1kx.mods.basicmachinery.util.Methods;
import n1kx.mods.basicmachinery.util.generics.GenericBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class GenericTEInventory extends TileEntity implements IInventory , IDropItemsOnBreak {
    protected final String isEmpty = "isEmpty";
    protected final String isFull = "isFull";
    protected final String hasRecentlyChanged = "hasRecentlyChanged";
    protected final String isNotEmpty = "isNotEmpty";

    public final int inputSlots , outputSlots , fuelSlots;
    public final int inventorySize;

    protected final ItemStackHandler inputHandler, fuelHandler, outputHandler;
    protected final CombinedInvWrapper combinedHandler;

    protected final BooleanHolder inputBools , fuelBools , outputBools;

    public final GenericBlock block;

    protected final String customName;

    public final IRecipes recipes;

    /**
     * constructor
     * @param inputSlots the number of input slots (does not include fuel slots)
     * @param outputSlots the number of output slots
     * @param fuelSlots the number of fuel slots
     * @param block the block this tile entity belongs to
     * @param customName the custom name for this tile entity (can be null; in case of null the unlocalized name of the block gets used)
     */
    protected GenericTEInventory( int inputSlots , int outputSlots , int fuelSlots , GenericBlock block , @Nullable IRecipes recipes , @Nullable String customName ) {
        this.inputSlots = inputSlots;
        this.outputSlots = outputSlots;
        this.fuelSlots = fuelSlots;
        this.inventorySize = this.inputSlots + this.outputSlots + this.fuelSlots;

        this.inputBools = new BooleanHolder( new String[]{ this.isEmpty , this.isNotEmpty , this.isFull , this.hasRecentlyChanged } , new boolean[]{ false , false , false , false } );
        this.fuelBools = new BooleanHolder( new String[]{ this.isEmpty , this.isNotEmpty , this.isFull , this.hasRecentlyChanged } , new boolean[]{ false , false , false , false } );
        this.outputBools = new BooleanHolder( new String[]{ this.isEmpty , this.isNotEmpty , this.isFull , this.hasRecentlyChanged } , new boolean[]{ false , false , false , false } );

        this.block = block;

        this.customName = Methods.newUnlocalizedName( customName );

        this.recipes = recipes;

        this.inputHandler = new ItemStackHandler( this.inputSlots ) {
            @Override
            protected void onContentsChanged( int slot ) {
                GenericTEInventory.super.markDirty();
                GenericTEInventory.this.contentsChanged( this , GenericTEInventory.this.inputBools , slot );
            }
            @Override
            protected void onLoad() {
                GenericTEInventory.this.contentsChanged( this , GenericTEInventory.this.inputBools , 0 );
            }
        };
        this.fuelHandler = new ItemStackHandler( this.fuelSlots ) {
            @Override
            protected void onContentsChanged( int slot ) {
                GenericTEInventory.super.markDirty();
                GenericTEInventory.this.contentsChanged( this , GenericTEInventory.this.fuelBools , slot );
            }
            @Override
            protected void onLoad() {
                GenericTEInventory.this.contentsChanged( this , GenericTEInventory.this.fuelBools , 0 );
            }
            public ItemStack extractItem( int amount ) {
                int slotIndex = -1;

                return this.extractItem( slotIndex , amount , false );
            }
            @Nonnull
            @Override
            public ItemStack extractItem( int slot , int amount , boolean simulate ) {
                if( slot == -1 ) {
                    for( int i = 0 ; i < this.getSlots() ; i++ ) if( !ItemStack.areItemStacksEqual( this.getStackInSlot( i ) , ItemStack.EMPTY ) ) {
                        slot = i;
                        break;
                    }
                }
                if( slot == -1 ) return super.extractItem( 0 , 0 , true );
                return super.extractItem( slot , amount , simulate );
            }
        };
        this.outputHandler = new ItemStackHandler( this.outputSlots ) {
            @Override
            protected void onContentsChanged( int slot ) {
                GenericTEInventory.super.markDirty();
                GenericTEInventory.this.contentsChanged( this , GenericTEInventory.this.outputBools , slot );
            }
            @Override
            protected void onLoad() {
                GenericTEInventory.this.contentsChanged( this , GenericTEInventory.this.outputBools , 0 );
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

    public void readInventory( NBTTagCompound compound ) {
        if( compound.hasKey( "inputs" ) ) this.inputHandler.deserializeNBT( (NBTTagCompound)compound.getTag( "inputs" ) );
        if( compound.hasKey( "fuels" ) ) this.fuelHandler.deserializeNBT( (NBTTagCompound)compound.getTag( "fuels" ) );
        if( compound.hasKey( "outputs" ) ) this.outputHandler.deserializeNBT( (NBTTagCompound)compound.getTag( "outputs" ) );
    }

    public NBTTagCompound writeInventory( NBTTagCompound compound ) {
        compound.setTag( "inputs" , this.inputHandler.serializeNBT() );
        compound.setTag( "fuels" , this.fuelHandler.serializeNBT() );
        compound.setTag( "outputs" , this.outputHandler.serializeNBT() );
        return compound;
    }

    @Override
    public int getSizeInventory() {
        return this.inventorySize;
    }

    @Override
    public boolean isEmpty() {
        return inputBools.getValue( this.isEmpty ) && fuelBools.getValue( this.isEmpty ) && outputBools.getValue( this.isEmpty );
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
        return this.hasCustomName() ? this.customName : Objects.requireNonNull( this.block.getRegistryName() ).getPath();
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
    public boolean shouldRefresh( World worldIn , BlockPos pos , IBlockState oldState , IBlockState newState ) {
        return oldState.getBlock() != newState.getBlock();
    }

    public ItemStack[] getInputs() {
        ItemStack[] inputs = new ItemStack[this.inputSlots];
        for( int i = 0 ; i < inputs.length ; i++ ) {
            inputs[i] = this.inputHandler.getStackInSlot( i );
        }
        return inputs;
    }

    public ItemStack[] getFuels() {
        ItemStack[] fuels = new ItemStack[this.fuelSlots];
        for( int i = 0 ; i < fuels.length ; i++ ) {
            fuels[i] = this.fuelHandler.getStackInSlot( i );
        }
        return fuels;
    }

    public ItemStack[] getOutputs() {
        ItemStack[] outputs = new ItemStack[this.outputSlots];
        for( int i = 0 ; i < outputs.length ; i++ ) {
            outputs[i] = this.outputHandler.getStackInSlot( i );
        }
        return outputs;
    }

    private void contentsChanged( ItemStackHandler handler , BooleanHolder bool , int slot ) {
        bool.setValue( this.isEmpty , true );
        bool.setValue( this.isNotEmpty , false );
        for( int i = 0 ; i < handler.getSlots() ; i++ ) {
            if( !ItemStack.areItemStacksEqual( handler.getStackInSlot( i ) , ItemStack.EMPTY ) ) {
                bool.setValue( this.isEmpty , false );
                bool.setValue( this.isNotEmpty , true );
                break;
            }
        }
        if( bool.getValue( this.isNotEmpty ) ) {
            bool.setValue( this.isFull , true );
            for( int i = 0 ; i < handler.getSlots() ; i++ ) {
                if( handler.getStackInSlot( i ).getCount() != this.getInventoryStackLimit() ) {
                    bool.setValue( this.isFull , false );
                }
            }
        }
        else {
            bool.setValue( this.isFull , false );
        }
        bool.setValue( this.hasRecentlyChanged , true );
    }
}
