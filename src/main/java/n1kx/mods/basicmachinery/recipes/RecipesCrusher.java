package n1kx.mods.basicmachinery.recipes;

import n1kx.mods.basicmachinery.list.ItemList;
import n1kx.mods.basicmachinery.util.generics.GenericRecipes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RecipesCrusher extends GenericRecipes {

    public RecipesCrusher() {
        super.addRecipe( new ItemStack[]{ new ItemStack( Items.IRON_INGOT ) } , new ItemStack[]{ new ItemStack( ItemList.CRUSHED_IRON ) } , 200 );
        super.addRecipe( new ItemStack[]{ new ItemStack( Blocks.IRON_ORE ) } , new ItemStack[]{ new ItemStack( ItemList.CRUSHED_IRON , 2 ) } , 300 );
    }

}