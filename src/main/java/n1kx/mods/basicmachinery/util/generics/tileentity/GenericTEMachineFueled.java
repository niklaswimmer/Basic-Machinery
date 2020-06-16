package n1kx.mods.basicmachinery.util.generics.tileentity;

import mcp.MethodsReturnNonnullByDefault;
import n1kx.mods.basicmachinery.util.IHasBurningState;
import n1kx.mods.basicmachinery.util.IHasWorkingState;
import n1kx.mods.basicmachinery.util.IRecipes;
import n1kx.mods.basicmachinery.util.Methods;
import n1kx.mods.basicmachinery.util.generics.GenericBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class GenericTEMachineFueled extends GenericTEMachine {

    protected int burnTimeLeft;
    protected int totalBurnTime;

    public GenericTEMachineFueled( int inputSlots , int outputSlots , int fuelSlots , GenericBlock block , @Nullable IRecipes recipes ) {
        super( inputSlots , outputSlots , fuelSlots , block , recipes );
    }

    public GenericTEMachineFueled( int inputSlots , int outputSlots , int fuelSlots , GenericBlock block , @Nullable IRecipes recipes , @Nullable String customName ) {
        super( inputSlots , outputSlots , fuelSlots , block , recipes , customName );
    }

    @Override
    public void readFromNBT( NBTTagCompound compound ) {
        if( compound.hasKey( "burnTimeLeft" ) ) this.burnTimeLeft = compound.getInteger( "burnTimeLeft" );
        if( compound.hasKey( "totalBurnTime" ) ) this.totalBurnTime = compound.getInteger( "totalBurnTime" );
        super.readFromNBT( compound );
    }

    @Override
    public NBTTagCompound writeToNBT( NBTTagCompound compound ) {
        compound.setInteger( "burnTimeLeft" , this.burnTimeLeft );
        compound.setInteger( "totalBurnTime" , this.totalBurnTime );
        return super.writeToNBT( compound );
    }

    @Override
    public void update() {
        if( !super.world.isRemote ) {
            if( super.areInputsPresent() ) {
                boolean flag = false;

                if( this.burnTimeLeft > 0 ) {
                    this.burnTimeLeft--;

                    if( this.burnTimeLeft == 0 && super.timeLeft - 1 > 0 ) {
                        this.burnTimeLeft = this.getNextBurnTime();

                        if( this.burnTimeLeft == 0 && super.timeLeft > 1 ) {
                            super.timeLeft = 0;
                            super.totalTimeNeeded = 0;
                            if( super.block instanceof IHasBurningState ) {
                                ( (IHasBurningState)super.block ).setBurningState( false , super.world , super.pos );
                            }
                        }
                    }
                    flag = true;
                }

                if( super.timeLeft > 0 ) {
                    super.timeLeft--;
                    if( super.timeLeft == 0 ) {
                        this.attemptMachine();
                    }
                    flag = true;
                } else {
                    this.startMachine();
                }

                if( flag ) super.markDirty();
            }
            else {
                super.timeLeft = 0;
                super.totalTimeNeeded = 0;
                this.burnTimeLeft--;
                if( this.burnTimeLeft == 0 ) {
                    this.totalBurnTime = 0;
                }
                if( super.block instanceof IHasWorkingState ) {
                    ( (IHasWorkingState)super.block ).setWorkingState( false , super.world , super.pos );
                }
            }
        }
    }

    @Override
    public void startMachine() {
        if( super.recipes != null ) {
            boolean flag = false;

            ItemStack[] inputs = this.getInputs();
            int progress = this.recipes.getWorkTime( inputs );
            boolean canWork = progress > -1;
            if( canWork ) {
                if( this.burnTimeLeft > 0 ) {
                    super.timeLeft = progress;
                    super.totalTimeNeeded = progress;
                    flag = true;
                }
                else {
                    int nextBurnTime = this.getNextBurnTime();
                    canWork = nextBurnTime > 0;
                    if( canWork ) {
                        super.timeLeft = progress;
                        super.totalTimeNeeded = progress;
                        this.burnTimeLeft = nextBurnTime;
                        if( super.block instanceof IHasBurningState ) {
                            ( (IHasBurningState)super.block ).setBurningState( true , super.world , super.pos );
                        }
                        flag = true;
                    }
                }
            }

            if( super.block instanceof IHasWorkingState ) {
                ( (IHasWorkingState)super.block ).setWorkingState( canWork , super.world , super.pos );
            }

            if( flag ) super.markDirty();
        }
    }

    public ItemStack[] getFuels() {
        ItemStack[] fuels = new ItemStack[super.fuelSlots];
        for( int i = 0 ; i < fuels.length ; i++ ) {
            fuels[i] = super.fuelHandler.getStackInSlot( i );
        }
        return fuels;
    }

    public int getNextBurnTime() {
        ItemStack[] fuels = this.getFuels();
        int indexOfFuel = -1;

        for( int i = 0 ; i < fuels.length ; i++ ) {
            if( Methods.isFuel( fuels[i] ) ) {
                indexOfFuel = i;
                break;
            }
        }

        if( indexOfFuel != -1 ) {
            super.fuelHandler.extractItem( indexOfFuel , 1 , false );
            this.totalBurnTime = Methods.getFuelValue( fuels[indexOfFuel] );
            return Methods.getFuelValue( fuels[indexOfFuel] );
        }
        else return 0;
    }

    @Override
    public int getField( int id ) {
        int value = super.getField( id );
        if( value == -1 ) {
            switch( id ) {
                case 2:
                    value = this.burnTimeLeft;
                    break;
                case 3:
                    value = this.totalBurnTime;
                    break;
                default:
                    value = -1;
            }
        }
        return value;
    }

    @Override
    public void setField( int id , int value ) {
        switch( id ) {
            case 2:
                this.burnTimeLeft = value;
                break;
            case 3:
                this.totalBurnTime = value;
                break;
            default:
                super.setField( id , value );
        }
    }

    @Override
    public int getFieldCount() {
        return super.getFieldCount() + 2;
    }
}
