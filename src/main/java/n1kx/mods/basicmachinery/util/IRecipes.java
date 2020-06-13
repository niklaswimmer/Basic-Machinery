package n1kx.mods.basicmachinery.util;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IRecipes {

    IRecipes getInstance();

    boolean isInput( ItemStack stack );

    ItemStack[] getOutputs( ItemStack... inputs );

    int getWorkTime( ItemStack... inputs );

}
