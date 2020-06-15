package n1kx.mods.basicmachinery.recipes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import n1kx.mods.basicmachinery.BasicMachinery;
import n1kx.mods.basicmachinery.list.ItemList;
import n1kx.mods.basicmachinery.util.IRecipes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Map;

public class RecipesCrusher implements IRecipes {

    private static final RecipesCrusher INSTANCE = new RecipesCrusher();
    private final Map<ItemStack , ItemStack> recipeList = Maps.newHashMap();
    private final Map<ItemStack , String> workTimeList = Maps.newHashMap();
    private final ArrayList<ItemStack> inputList = Lists.newArrayList();

    public RecipesCrusher() {
        this.addRecipe( new ItemStack( Items.IRON_INGOT ) , new ItemStack( ItemList.CRUSHED_IRON ) , 200 );
        this.addRecipe( new ItemStack( Blocks.IRON_ORE ) , new ItemStack( ItemList.CRUSHED_IRON , 2 ) , 300 );
    }

    @Override
    public ItemStack[] getOutputs( ItemStack... inputs ) {
        if( inputs.length != 1 ) return new ItemStack[]{ ItemStack.EMPTY };

        ItemStack[] validatedInputs = this.validateInputs( inputs );

        return new ItemStack[]{ recipeList.getOrDefault( validatedInputs[0] , ItemStack.EMPTY ).copy() };
    }

    @Override
    public int getWorkTime( ItemStack... inputs ) {
        if( inputs.length != 1 ) return -1;

        ItemStack[] validatedInputs = this.validateInputs( inputs );
        return Integer.parseInt( this.workTimeList.getOrDefault( this.recipeList.getOrDefault( validatedInputs[0] , ItemStack.EMPTY ) , "-1" ) );
    }

    @Override
    public IRecipes getInstance() {
        return RecipesCrusher.INSTANCE;
    }

    @Override
    public boolean isInput( ItemStack input ) {
        for( int i = 0 ; i < this.inputList.size() ; i++ ) {
            if( input.isItemEqual( this.inputList.get( i ) ) ) {
                return true;
            }
        }
        return false;
    }

    private void addRecipe( ItemStack input , ItemStack output , int workTime ) {
        this.recipeList.put( input , output );
        this.workTimeList.put( output , String.valueOf( workTime ) );

        boolean itemAlreadyInInputList = false;
        for( int i = 0 ; i < this.inputList.size() ; i++ ) {
            if( ItemStack.areItemStacksEqual( input , this.inputList.get( i ) ) ) {
                itemAlreadyInInputList = true;
                break;
            }
        }
        if( !itemAlreadyInInputList ) {
            this.inputList.add( input );
        }
    }

    private ItemStack[] validateInputs( ItemStack... inputs ) {
        ItemStack[] validatedInputs = new ItemStack[ inputs.length ];

        for( int i = 0 ; i < inputs.length ; i++ ) {
            for( int j = 0 ; j < inputList.size() ; j++ ) {
                if( inputs[i].getItem().getTranslationKey( inputs[i] ).equals( this.inputList.get( j ).getItem().getTranslationKey( this.inputList.get( j ) ) ) ) {
                    validatedInputs[i] = this.inputList.get( j );
                    break;
                }
            }
            if( validatedInputs[i] == null ) {
                validatedInputs[i] = new ItemStack( Items.AIR );
            }
        }

        return validatedInputs;
    }

}