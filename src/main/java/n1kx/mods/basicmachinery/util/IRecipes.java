package n1kx.mods.basicmachinery.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public interface IRecipes {

    void addRecipe( ItemStack[] inputs , ItemStack[] outputs , int processTime );

    boolean areInRecipe( RecipePart part , ItemStack... inputs );

    int getTimeNeeded( RecipePart part , ItemStack... stacks );
    ItemStack[] getOutputs( ItemStack... inputs );

}
