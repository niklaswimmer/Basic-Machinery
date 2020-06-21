package n1kx.mods.basicmachinery.util.generics.tileentity;

import mcp.MethodsReturnNonnullByDefault;
import n1kx.mods.basicmachinery.util.IHasBurningState;
import n1kx.mods.basicmachinery.util.IHasWorkingState;
import n1kx.mods.basicmachinery.util.IRecipes;
import n1kx.mods.basicmachinery.util.Methods;
import n1kx.mods.basicmachinery.util.generics.GenericBlock;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class GenericTEMachineFueled extends GenericTEMachine {

    protected int burnTimeLeft;
    protected int totalBurnTime;

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
            if( this.burnTimeLeft > 0 ) {
                super.update();

                if( --this.burnTimeLeft == 0 ) {
                    if( super.inputBools.getValue( super.isNotEmpty )) {
                        if( ( this.burnTimeLeft = this.getNextBurnTime() ) == 0 ) {
                            this.totalBurnTime = 0;
                            super.timeLeft = 0;
                            super.totalTimeNeeded = 0;
                            if( super.block instanceof IHasWorkingState ) ( (IHasWorkingState)super.block ).setWorkingState( false , super.world , super.pos );
                            if( super.block instanceof IHasBurningState ) ( (IHasBurningState)super.block ).setBurningState( false , super.world , super.pos );
                        }
                        else {
                            this.totalBurnTime = this.burnTimeLeft;
                            super.fuelHandler.extractItem( -1 , 1 , false );
                        }
                    }
                    else{
                        this.totalBurnTime = 0;
                        if( super.block instanceof IHasBurningState ) ( (IHasBurningState)super.block ).setBurningState( false , super.world , super.pos );
                    }
                }

                super.markDirty();
            }
            else if( super.fuelBools.getValue( super.hasRecentlyChanged ) ) {
                if( super.fuelBools.getValue( super.isNotEmpty ) ) {
                    if( super.inputBools.getValue( super.isNotEmpty ) ) {
                        if( ( this.burnTimeLeft = this.getNextBurnTime() ) > 0 ) {
                            this.totalBurnTime = this.burnTimeLeft;
                            super.fuelHandler.extractItem( -1 , 1 , false );

                            if( super.block instanceof IHasBurningState ) ( (IHasBurningState)super.block ).setBurningState( true , super.world , super.pos );

                            super.markDirty();
                        }
                        super.fuelBools.setValue( super.hasRecentlyChanged , false );
                    }
                }
                else super.fuelBools.setValue( super.hasRecentlyChanged , false );
                this.sendChangesToClient();
            }
        }
    }

    public int getNextBurnTime() {
        return Methods.getFuelValue( super.fuelHandler.extractItem( -1 , 1 , true ) );
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

    @Override
    public void onLoad() {
        if( !world.isRemote ) {
            super.onLoad();
            if( super.block instanceof IHasBurningState ) ( (IHasBurningState)super.block ).setBurningState( this.burnTimeLeft > 0 , super.world , super.pos );
        }
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = super.getUpdateTag();
        compound.setInteger( "burnTimeLeft" , this.burnTimeLeft );
        return compound;
    }

    @Override
    public void handleUpdateTag( NBTTagCompound compound ) {
        super.handleUpdateTag( compound );
        int burnTimeLeft = 0;
        if( compound.hasKey( "burnTimeLeft" ) ) burnTimeLeft = compound.getInteger( "burnTimeLeft" );
        if( super.block instanceof IHasBurningState ) ( (IHasBurningState)super.block ).setBurningState( burnTimeLeft > 0 , super.world , super.pos );
    }
}
