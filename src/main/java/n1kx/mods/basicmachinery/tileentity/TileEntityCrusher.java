package n1kx.mods.basicmachinery.tileentity;

import n1kx.mods.basicmachinery.list.BlockList;
import n1kx.mods.basicmachinery.recipes.RecipesCrusher;
import n1kx.mods.basicmachinery.util.generics.GenericBlock;
import n1kx.mods.basicmachinery.util.generics.GenericTileEntityMachine;

public class TileEntityCrusher extends GenericTileEntityMachine {

    public TileEntityCrusher() {
        super( 1 , 1 , 1 , (GenericBlock)BlockList.CRUSHER , new RecipesCrusher() );
    }

}
