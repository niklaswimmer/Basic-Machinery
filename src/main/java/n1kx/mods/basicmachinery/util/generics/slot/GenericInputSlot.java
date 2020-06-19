package n1kx.mods.basicmachinery.util.generics.slot;

import n1kx.mods.basicmachinery.util.IRecipes;
import n1kx.mods.basicmachinery.util.RecipePart;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GenericInputSlot extends GenericSlot {

    private final IRecipes recipes;

    public GenericInputSlot( IInventory inventoryIn , int index , int xPosition , int yPosition , IRecipes recipes ) {
        super( inventoryIn , index , xPosition , yPosition );
        this.recipes = recipes;
    }

    @Override
    public boolean isItemValid( ItemStack stack ) {
        return recipes.areInRecipe( RecipePart.INPUT , stack );
    }
}
