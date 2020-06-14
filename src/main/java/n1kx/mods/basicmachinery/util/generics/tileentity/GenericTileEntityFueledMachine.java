package n1kx.mods.basicmachinery.util.generics.tileentity;

import n1kx.mods.basicmachinery.util.IRecipes;
import n1kx.mods.basicmachinery.util.Methods;
import n1kx.mods.basicmachinery.util.generics.GenericBlock;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public abstract class GenericTileEntityFueledMachine extends GenericTileEntityMachine {

    private int burnTimeLeft;

    public GenericTileEntityFueledMachine( int inputSlots , int outputSlots , int fuelSlots , GenericBlock block , @Nullable IRecipes recipes ) {
        super( inputSlots , outputSlots , fuelSlots , block , recipes );
    }

    public GenericTileEntityFueledMachine( int inputSlots , int outputSlots , int fuelSlots , GenericBlock block , @Nullable IRecipes recipes , @Nullable String customName ) {
        super( inputSlots , outputSlots , fuelSlots , block , recipes , customName );
    }

    @Override
    public void update() {
        if( !super.world.isRemote ) {
            boolean flag = false;

            if( this.burnTimeLeft > 0 ) {
                this.burnTimeLeft--;

                if( this.burnTimeLeft == 0 && super.progress - 1 > 0 ) {
                    this.burnTimeLeft = this.getNextBurnTime();

                    if( this.burnTimeLeft == 0 && super.progress > 1 ) {
                        super.progress = 0;
                    }
                }
                flag = true;
            }

            if( super.progress > 0 ) {
                super.progress--;
                if( super.progress == 0 ) {
                    this.attemptMachine();
                }
                flag = true;
            } else {
                this.startMachine();
            }

            if( flag ) super.markDirty();
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
                    super.progress = progress;
                    flag = true;
                }
                else {
                    int nextBurnTime = this.getNextBurnTime();
                    if( nextBurnTime > 0 ) {
                        super.progress = progress;
                        this.burnTimeLeft = nextBurnTime;
                        flag = true;
                    }
                }
            }

            if( flag ) super.markDirty();
        }
    }

    @Override
    public int getField( int id ) {
        switch( id ) {
            case 0:
                return super.progress;
            case 1:
                return this.burnTimeLeft;
            default:
                return 0;
        }
    }

    @Override
    public void setField( int id , int value ) {
        switch( id ) {
            case 0:
                super.progress = value;
                break;
            case 1:
                this.burnTimeLeft = value;
                break;
        }
    }

    @Override
    public int getFieldCount() {
        return 2;
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
            return Methods.getFuelValue( fuels[indexOfFuel] );
        }
        else return 0;
    }
}
