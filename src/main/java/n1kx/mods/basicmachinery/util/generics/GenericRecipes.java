package n1kx.mods.basicmachinery.util.generics;

import n1kx.mods.basicmachinery.BasicMachinery;
import n1kx.mods.basicmachinery.util.IRecipes;
import n1kx.mods.basicmachinery.util.RecipePart;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GenericRecipes implements IRecipes {

    protected final Map<ArrayList<ItemStack> , ArrayList<ItemStack>> recipes = new HashMap<>();
    protected final ArrayList<ArrayList<ItemStack>> inputList = new ArrayList<>();
    protected final ArrayList<ArrayList<ItemStack>> outputList = new ArrayList<>();
    protected final Map<ArrayList<ItemStack> , Integer> processTimeMap = new HashMap<>();

    @Override
    public void addRecipe( ItemStack[] inputs , ItemStack[] outputs , int processTime ) {
        ArrayList<ItemStack> inputList = new ArrayList<>();
        Collections.addAll( inputList , inputs );

        ArrayList<ItemStack> outputList = new ArrayList<>();
        Collections.addAll( outputList , outputs );

        this.recipes.put( inputList , outputList );

        this.inputList.add( inputList );
        this.outputList.add( outputList );
        this.processTimeMap.put( outputList , processTime );
    }

    @Override
    public boolean areInRecipe( RecipePart part , ItemStack... stacks ) throws IllegalArgumentException {
        ArrayList<ArrayList<ItemStack>> list = this.getList( part );
        if( list == null ) throw new IllegalArgumentException( "invalid recipe part: "+ part );

        for( ItemStack stack1 : stacks ) {
            BasicMachinery.logger.info( "checking for "+stack1 );
            boolean bool1 = false;
            for( ArrayList<ItemStack> stackList : list ) {
                for( ItemStack stack : stackList ) {
                    if( stack1.isItemEqual( stack ) ) {
                        bool1 = true;
                        break;
                    }
                }
                if( bool1 ) break;
            }
            if( !bool1 ) return false;
        }
        return true;
    }

    @Override
    public int getTimeNeeded( RecipePart part , ItemStack... stacks ) {
        ArrayList<ItemStack> validated = this.validate( part , stacks );

        switch( part ) {
            case INPUT:
                return this.processTimeMap.getOrDefault( this.recipes.get( validated ) , 0 );
            case OUTPUT:
                return this.processTimeMap.getOrDefault( validated , 0 );
            default:
                return 0;
        }
    }

    @Override
    public ItemStack[] getOutputs( ItemStack... inputs ) {
        ArrayList<ItemStack> validated = this.validate( RecipePart.INPUT , inputs );

        Object[] returnArr = this.recipes.get( validated ).toArray();
        ItemStack[] returnStacks = new ItemStack[returnArr.length];
        for( int i = 0 ; i < returnArr.length ; i++ ) {
            returnStacks[i] = ( (ItemStack)returnArr[i] ).copy();
        }
        return returnStacks;
    }

    private ArrayList<ArrayList<ItemStack>> getList( RecipePart part ) {
        switch( part ) {
            case INPUT:
                return this.inputList;
            case OUTPUT:
                return this.outputList;
            default:
                return null;
        }
    }

    private ArrayList<ItemStack> validate( RecipePart part , ItemStack... stacks ) throws IllegalArgumentException {
        ArrayList<ArrayList<ItemStack>> list = this.getList( part );
        if( list == null ) throw new IllegalArgumentException( "invalid recipe part: "+ part );

        for( ArrayList<ItemStack> stackList : list ) {
            if( stackList.size() != stacks.length ) continue;
            boolean bool = true;
            for( int i = 0 ; i < stacks.length ; i++ ) {
                boolean bool1 = true;
                if( !( stacks[i].isItemEqual( stackList.get( i ) ) && stacks[i].getCount() >= stackList.get( i ).getCount() ) ) {
                    bool1 = false;
                }
                if( !bool1 ) {
                    bool = false;
                    break;
                }
            }
            if( bool ) return stackList;
        }
        return null;
    }
}
