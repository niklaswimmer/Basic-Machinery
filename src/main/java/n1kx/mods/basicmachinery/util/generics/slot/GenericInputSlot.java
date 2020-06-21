package n1kx.mods.basicmachinery.util.generics.slot;

import n1kx.mods.basicmachinery.util.IRecipes;
import n1kx.mods.basicmachinery.util.RecipePart;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GenericInputSlot extends GenericSlot {

    private final IRecipes recipes;

    public GenericInputSlot( IItemHandler handler , int index , int xPosition , int yPosition , IRecipes recipes ) {
        super( handler , index , xPosition , yPosition );
        this.recipes = recipes;
    }

    @Override
    public boolean isItemValid( ItemStack stack ) {
        return recipes.areInRecipe( RecipePart.INPUT , stack );
    }

}
