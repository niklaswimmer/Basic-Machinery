package n1kx.mods.basicmachinery.util.generics.tileentity;

import mcp.MethodsReturnNonnullByDefault;
import n1kx.mods.basicmachinery.BasicMachinery;
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

    protected GenericTEMachine( int inputSlots , int outputSlots , int fuelSlots , GenericBlock block , @Nullable IRecipes recipes , @Nullable String customName ) {
        super( inputSlots , outputSlots , fuelSlots , block , recipes , customName );

        this.timeLeft = 0;
        this.totalTimeNeeded = 0;
    }

    @Override
    public void update() {
        if( !super.world.isRemote ) {
            if( super.inputBools.getValue( super.isNotEmpty ) ) {
                if( this.timeLeft > 0 ) {
                    this.timeLeft--;

                    if( this.timeLeft == 0 ) this.attemptMachine();

                    super.markDirty();
                }
                else this.startMachine();
            }
            else if( super.inputBools.getValue( super.hasRecentlyChanged ) ) {
                this.timeLeft = 0;
                this.totalTimeNeeded = 0;
                if( super.block instanceof IHasWorkingState ) ( (IHasWorkingState)super.block ).setWorkingState( false , super.world , super.pos );
                super.markDirty();
                super.inputBools.setValue( super.hasRecentlyChanged , false );
            }
        }
    }

    protected void startMachine() {
        if( super.recipes != null && !super.inputBools.getValue( super.isEmpty ) ) {
            ItemStack[] inputs = super.getInputs();
            int progress = this.recipes.getWorkTime( inputs );

            if( progress > -1 ) {
                this.timeLeft = progress;
                this.totalTimeNeeded = progress;

                if( super.block instanceof IHasWorkingState ) ( (IHasWorkingState)super.block ).setWorkingState( true , super.world , super.pos );

                super.markDirty();
            }
        }
    }

    protected void attemptMachine() {
        if( super.recipes != null && !super.inputBools.getValue( super.isEmpty ) ) {
            if( !super.outputBools.getValue( super.isFull ) ) {
                this.totalTimeNeeded = 0;

                ItemStack[] inputs = super.getInputs();
                ItemStack[] outputs = this.recipes.getOutputs( inputs );

                if( outputs != null ) {
                    boolean insertingPossible = true;
                    for( int i = 0 ; i < outputs.length ; i++ ) {
                        if( !ItemStack.areItemStacksEqual( super.outputHandler.insertItem( i , outputs[i] , true ) , ItemStack.EMPTY ) ) {
                            insertingPossible = false;
                            break;
                        }
                    }
                    if( insertingPossible ) {
                        for( int i = 0 ; i < outputs.length ; i++ ) {
                            super.outputHandler.insertItem( i , outputs[i] , false );
                        }
                        for( int i = 0 ; i < inputs.length ; i++ ) {
                            super.inputHandler.extractItem( i , 1 , false );
                        }
                        this.startMachine();
                    }
                }
            }
        }
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

    @Override
    public void onLoad() {
        if( !world.isRemote ) {
            if( super.block instanceof IHasWorkingState ) ( (IHasWorkingState)super.block ).setWorkingState( this.timeLeft > 0 , super.world , super.pos );
        }
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = super.getUpdateTag();
        compound.setInteger( "timeLeft" , this.timeLeft );
        return compound;
    }

    @Override
    public void handleUpdateTag( NBTTagCompound compound ) {
        super.handleUpdateTag( compound );
        int timeLeft = 0;
        if( compound.hasKey( "timeLeft" ) ) timeLeft = compound.getInteger( "timeLeft" );
        if( super.block instanceof IHasWorkingState ) ( (IHasWorkingState)super.block ).setWorkingState( timeLeft > 0 , super.world , super.pos );
    }

}
