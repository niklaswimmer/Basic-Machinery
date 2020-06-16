package n1kx.mods.basicmachinery.util.generics.tileentity;

import mcp.MethodsReturnNonnullByDefault;
import n1kx.mods.basicmachinery.util.IHasWorkingState;
import n1kx.mods.basicmachinery.util.IRecipes;
import n1kx.mods.basicmachinery.util.generics.GenericBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class GenericTEMachine extends GenericTEInventory implements ITickable {

    protected int timeLeft;
    protected int totalTimeNeeded;

    protected GenericTEMachine( int inputSlots , int outputSlots , int fuelSlots , GenericBlock block , @Nullable IRecipes recipes ) {
        this( inputSlots , outputSlots , fuelSlots , block , recipes , null );
    }

    protected GenericTEMachine( int inputSlots , int outputSlots , int fuelSlots , GenericBlock block , @Nullable IRecipes recipes , @Nullable String customName ) {
        super( inputSlots , outputSlots , fuelSlots , block , recipes , customName );

        this.timeLeft = 0;
        this.totalTimeNeeded = 0;
    }

    @Override
    public void readFromNBT( NBTTagCompound compound ) {
        if( compound.hasKey( "timeLeft" ) ) this.timeLeft = compound.getInteger( "timeLeft" );
        if( compound.hasKey( "totalTimeNeeded" ) ) this.totalTimeNeeded = compound.getInteger( "totalTimeNeeded" );
        super.readFromNBT( compound );
    }

    @Override
    public NBTTagCompound writeToNBT( NBTTagCompound compound ) {
        compound.setInteger( "timeLeft" , this.timeLeft );
        compound.setInteger( "totalTimeNeeded" , this.totalTimeNeeded );
        return super.writeToNBT( compound );
    }

    @Override
    public void update() {
        if( !super.world.isRemote ) {
            if( this.areInputsPresent() ) {
                if( this.timeLeft > 0 ) {
                    this.timeLeft--;

                    if( this.timeLeft == 0 ) {
                        this.attemptMachine();
                    }

                    super.markDirty();
                } else {
                    this.startMachine();
                }
            }
            else {
                this.timeLeft = 0;
                this.totalTimeNeeded = 0;
                if( super.block instanceof IHasWorkingState ) {
                    ( (IHasWorkingState)super.block ).setWorkingState( false , super.world , super.pos );
                }
            }
        }
    }

    public void startMachine() {
        if( this.recipes != null ) {
            ItemStack[] inputs = this.getInputs();

            int progress = this.recipes.getWorkTime( inputs );
            boolean canWork = progress > -1;
            if( canWork ) {
                this.timeLeft = progress;
                this.totalTimeNeeded = progress;
            }

            if( super.block instanceof IHasWorkingState ) {
                ( (IHasWorkingState)super.block ).setWorkingState( canWork , super.world , super.pos );
            }

            super.markDirty();
        }
    }

    public void attemptMachine() {
        if( this.recipes != null ) {
            this.totalTimeNeeded = 0;

            ItemStack[] inputs = this.getInputs();

            ItemStack[] output = this.recipes.getOutputs( inputs );

            if( output != null ) {
                boolean insertingPossible = true;
                for( int i = 0 ; i < output.length ; i++ ) {
                    if( !this.insertOutput( output[i] , i , true ) ) {
                        insertingPossible = false;
                        break;
                    }
                }
                if( insertingPossible ) {
                    for( int i = 0 ; i < output.length ; i++ ) {
                        this.insertOutput( output[i] , i , false );
                    }
                    for( int i = 0 ; i < inputs.length ; i++ ) {
                        super.inputHandler.extractItem( i , 1 , false );
                    }
                    this.startMachine();
                }
            }
        }
    }

    public boolean areInputsPresent() {
        boolean areInputsPresent = false;

        for( int i = 0 ; i < super.inputSlots ; i++ ) {
            if( !ItemStack.areItemStacksEqual( super.inputHandler.getStackInSlot( i ) , ItemStack.EMPTY ) ) {
                areInputsPresent = true;
                break;
            }
        }

        return areInputsPresent;
    }

    public ItemStack[] getInputs() {
        ItemStack[] inputs = new ItemStack[super.inputSlots];
        for( int i = 0 ; i < inputs.length ; i++ ) {
            inputs[i] = super.inputHandler.getStackInSlot( i );
        }
        return inputs;
    }

    protected boolean insertOutput( ItemStack output , int outputIndex , boolean simulate ) {
        return ItemStack.areItemStacksEqual( super.outputHandler.insertItem( outputIndex , output , simulate ) , ItemStack.EMPTY );
    }

    @Override
    public int getField( int id ) {
        switch( id ) {
            case 0:
                return this.timeLeft;
            case 1:
                return this.totalTimeNeeded;
            default:
                return -1;
        }
    }

    @Override
    public void setField( int id , int value ) {
        switch( id ) {
            case 0:
                this.timeLeft = value;
                break;
            case 1:
                this.totalTimeNeeded = value;
                break;
        }
    }

    @Override
    public int getFieldCount() {
        return 2;
    }
}
