package n1kx.mods.basicmachinery.util.generics.tileentity;

import mcp.MethodsReturnNonnullByDefault;
import n1kx.mods.basicmachinery.util.IRecipes;
import n1kx.mods.basicmachinery.util.Methods;
import n1kx.mods.basicmachinery.util.generics.GenericBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class GenericTEFueledMachine extends GenericTEMachine {

    protected int burnTimeLeft;
    protected int burnTime;

    public GenericTEFueledMachine( int inputSlots , int outputSlots , int fuelSlots , GenericBlock block , @Nullable IRecipes recipes ) {
        super( inputSlots , outputSlots , fuelSlots , block , recipes );
    }

    public GenericTEFueledMachine( int inputSlots , int outputSlots , int fuelSlots , GenericBlock block , @Nullable IRecipes recipes , @Nullable String customName ) {
        super( inputSlots , outputSlots , fuelSlots , block , recipes , customName );
    }

    @Override
    public void readFromNBT( NBTTagCompound compound ) {
        if( compound.hasKey( "burnTimeLeft" ) ) this.burnTimeLeft = compound.getInteger( "burnTimeLeft" );
        if( compound.hasKey( "burnTime" ) ) this.burnTime = compound.getInteger( "burnTime" );
        super.readFromNBT( compound );
    }

    @Override
    public NBTTagCompound writeToNBT( NBTTagCompound compound ) {
        compound.setInteger( "burnTimeLeft" , this.burnTimeLeft );
        compound.setInteger( "burnTime" , this.burnTime );
        return super.writeToNBT( compound );
    }

    @Override
    public void update() {
        if( !super.world.isRemote ) {
            if( super.areInputsPresent() ) {
                boolean flag = false;

                if( this.burnTimeLeft > 0 ) {
                    this.burnTimeLeft--;

                    if( this.burnTimeLeft == 0 && super.progressLeft - 1 > 0 ) {
                        this.burnTimeLeft = this.getNextBurnTime();

                        if( this.burnTimeLeft == 0 && super.progressLeft > 1 ) {
                            super.progressLeft = 0;
                            super.progress = 0;
                        }
                    }
                    flag = true;
                }

                if( super.progressLeft > 0 ) {
                    super.progressLeft--;
                    if( super.progressLeft == 0 ) {
                        this.attemptMachine();
                    }
                    flag = true;
                } else {
                    this.startMachine();
                }

                if( flag ) super.markDirty();
            }
            else {
                super.progressLeft = 0;
                super.progress = 0;
                this.burnTimeLeft--;
                if( this.burnTimeLeft == 0 ) {
                    this.burnTime = 0;
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

            if( progress > -1 ) {
                if( this.burnTimeLeft > 0 ) {
                    super.progressLeft = progress;
                    super.progress = progress;
                    flag = true;
                }
                else {
                    int nextBurnTime = this.getNextBurnTime();
                    if( nextBurnTime > 0 ) {
                        super.progressLeft = progress;
                        super.progress = progress;
                        this.burnTimeLeft = nextBurnTime;
                        flag = true;
                    }
                }
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
            this.burnTime = Methods.getFuelValue( fuels[indexOfFuel] );
            return Methods.getFuelValue( fuels[indexOfFuel] );
        }
        else return 0;
    }

    @Override
    public int getField( int id ) {
        switch( id ) {
            case 0:
                return super.progressLeft;
            case 1:
                return super.progress;
            case 2:
                return this.burnTimeLeft;
            case 3:
                return this.burnTime;
            default:
                return 0;
        }
    }

    @Override
    public void setField( int id , int value ) {
        switch( id ) {
            case 0:
                super.progressLeft = value;
                break;
            case 1:
                super.progress = value;
                break;
            case 2:
                this.burnTimeLeft = value;
                break;
            case 3:
                this.burnTime = value;
                break;
        }
    }

    @Override
    public int getFieldCount() {
        return 4;
    }
}
