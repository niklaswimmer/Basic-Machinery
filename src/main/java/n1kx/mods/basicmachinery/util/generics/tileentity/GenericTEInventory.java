package n1kx.mods.basicmachinery.util.generics.tileentity;

import mcp.MethodsReturnNonnullByDefault;
import n1kx.mods.basicmachinery.util.BooleanHolder;
import n1kx.mods.basicmachinery.util.IDropItemsOnBreak;
import n1kx.mods.basicmachinery.util.IRecipes;
import n1kx.mods.basicmachinery.util.Methods;
import n1kx.mods.basicmachinery.util.generics.GenericBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class GenericTEInventory extends TileEntity implements IDropItemsOnBreak {
    protected final String isNotEmpty = "isNotEmpty";
    protected final String isFull = "isFull";
    protected final String hasRecentlyChanged = "hasRecentlyChanged";

    public final int inputSlots , outputSlots , fuelSlots;
    public final int inventorySize;

    protected final ItemStackHandler inputHandler, fuelHandler, outputHandler;
    protected final CombinedInvWrapper combinedHandler;

    protected final BooleanHolder inputBools , fuelBools , outputBools;

    public final GenericBlock block;

    protected final String customName;

    public final IRecipes recipes;

    protected GenericTEInventory( int inputSlots , int outputSlots , int fuelSlots , GenericBlock block , @Nullable IRecipes recipes , @Nullable String customName ) {
        this.inputSlots = inputSlots;
        this.outputSlots = outputSlots;
        this.fuelSlots = fuelSlots;
        this.inventorySize = this.inputSlots + this.outputSlots + this.fuelSlots;

        this.inputBools = new BooleanHolder( new String[]{ this.isNotEmpty , this.isFull , this.hasRecentlyChanged } , new boolean[]{ false , false , false } );
        this.fuelBools = new BooleanHolder( new String[]{ this.isNotEmpty , this.isFull , this.hasRecentlyChanged } , new boolean[]{ false , false , false } );
        this.outputBools = new BooleanHolder( new String[]{ this.isNotEmpty , this.isFull , this.hasRecentlyChanged } , new boolean[]{ false , false , false } );

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
            @Nonnull
            @Override
            public ItemStack extractItem( int slot , int amount , boolean simulate ) {
                if( slot == -1 ) {
                    for( int i = 0 ; i < this.getSlots() ; i++ ) if( !ItemStack.areItemStacksEqual( this.getStackInSlot( i ) , ItemStack.EMPTY ) ) {
                        slot = i;
                        break;
                    }
                }
                if( slot == -1 ) return ItemStack.EMPTY;
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

    public abstract int getField( int id );

    public abstract void setField( int id , int value );

    public abstract int getFieldCount();

    @Override
    public void dropInventoryItems( World worldIn , BlockPos pos ) {
        for( int i = 0 ; i < this.combinedHandler.getSlots() ; i++ ) {
            ItemStack stack = this.combinedHandler.getStackInSlot( i );
            if( !stack.isEmpty() ) {
                InventoryHelper.spawnItemStack( super.world , super.pos.getX() , super.pos.getY() , super.pos.getZ() , stack );
            }
        }
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

    private void contentsChanged( ItemStackHandler handler , BooleanHolder bool , int slot ) {
        bool.setValue( this.isNotEmpty , false );
        for( int i = 0 ; i < handler.getSlots() ; i++ ) {
            if( !ItemStack.areItemStacksEqual( handler.getStackInSlot( i ) , ItemStack.EMPTY ) ) {
                bool.setValue( this.isNotEmpty , true );
                break;
            }
        }
        if( bool.getValue( this.isNotEmpty ) ) {
            bool.setValue( this.isFull , true );
            for( int i = 0 ; i < handler.getSlots() ; i++ ) {
                if( handler.getStackInSlot( i ).getCount() != handler.getStackInSlot( i ).getMaxStackSize() ) {
                    bool.setValue( this.isFull , false );
                }
            }
        }
        else {
            bool.setValue( this.isFull , false );
        }
        bool.setValue( this.hasRecentlyChanged , true );
    }

    @Override
    public boolean hasCapability( Capability<?> capability , @Nullable EnumFacing facing ) {
        if( capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ) return true;
        return super.hasCapability( capability , facing );
    }

    @Nullable
    @Override
    public <T> T getCapability( Capability<T> capability , @Nullable EnumFacing facing ) {
        if( capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast( this.combinedHandler );
        }
        return super.getCapability( capability , facing );
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return !isInvalid() && playerIn.getDistanceSq( pos.add( 0.5D , 0.5D , 0.5D ) ) <= 64D;
    }

    public NetworkRegistry.TargetPoint getTargetPoint() {
        return new NetworkRegistry.TargetPoint( super.world.provider.getDimension() , super.pos.getX() , super.pos.getY() , super.pos.getZ() , 0 );
    }

}
