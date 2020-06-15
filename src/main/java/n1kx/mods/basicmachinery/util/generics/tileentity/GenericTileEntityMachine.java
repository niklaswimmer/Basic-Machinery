package n1kx.mods.basicmachinery.util.generics.tileentity;

import mcp.MethodsReturnNonnullByDefault;
import n1kx.mods.basicmachinery.util.IRecipes;
import n1kx.mods.basicmachinery.util.generics.GenericBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class GenericTileEntityMachine extends GenericTileEntityInventory implements ITickable {

    protected int progressLeft;
    protected int progress;


    protected GenericTileEntityMachine( int inputSlots , int outputSlots , int fuelSlots , GenericBlock block , @Nullable IRecipes recipes ) {
        this( inputSlots , outputSlots , fuelSlots , block, recipes , null );
    }

    protected GenericTileEntityMachine( int inputSlots , int outputSlots , int fuelSlots , GenericBlock block , @Nullable IRecipes recipes , @Nullable String customName ) {
        super( inputSlots , outputSlots , fuelSlots , block , recipes , customName );

        this.progressLeft = 0;
    }

    @Override
    public void readFromNBT( NBTTagCompound compound ) {
        if( compound.hasKey( "progressLeft" ) ) this.progressLeft = compound.getInteger( "progressLeft" );
        if( compound.hasKey( "progress" ) ) this.progress = compound.getInteger( "progress" );
        super.readFromNBT( compound );
    }

    @Override
    public NBTTagCompound writeToNBT( NBTTagCompound compound ) {
        compound.setInteger( "progressLeft" , this.progressLeft );
        compound.setInteger( "progress" , this.progress );
        return super.writeToNBT( compound );
    }

    @Override
    public void update() {
        if( !super.world.isRemote ) {
            if( this.areInputsPresent() ) {
                if( this.progressLeft > 0 ) {
                    this.progressLeft--;

                    if( this.progressLeft == 0 ) {
                        this.attemptMachine();
                    }

                    super.markDirty();
                } else {
                    this.startMachine();
                }
            }
            else {

                this.progressLeft = 0;
                this.progress = 0;
            }
        }
    }

    public void startMachine() {
        if( super.recipes != null ) {
            ItemStack[] inputs = this.getInputs();

            int progress = this.recipes.getWorkTime( inputs );
            if( progress > -1 ) {
                this.progressLeft = progress;
                this.progress = progress;
            }

            super.markDirty();
        }
    }

    public void attemptMachine() {
        if( super.recipes != null ) {
            this.progress = 0;

            ItemStack[] inputs = this.getInputs();

            ItemStack[] output = super.recipes.getOutputs( inputs );

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
                return this.progressLeft;
            case 1:
                return this.progress;
            default:
                return 0;
        }
    }

    @Override
    public void setField( int id , int value ) {
        switch( id ) {
            case 0:
                this.progressLeft = value;
                break;
            case 1:
                this.progress = value;
                break;
        }
    }

    @Override
    public int getFieldCount() {
        return 2;
    }
}
