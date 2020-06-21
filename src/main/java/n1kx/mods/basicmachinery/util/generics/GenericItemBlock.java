package n1kx.mods.basicmachinery.util.generics;

import n1kx.mods.basicmachinery.list.ItemList;
import n1kx.mods.basicmachinery.util.IHasModel;
import n1kx.mods.basicmachinery.util.Methods;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;

import java.util.Objects;

public class GenericItemBlock extends ItemBlock implements IHasModel {

    public GenericItemBlock( Block block , String name ) {
        super( block );

        super.setRegistryName( Methods.newRegistryName( name ) );
        super.setTranslationKey( Methods.newUnlocalizedName( name ) );

        ItemList.ITEMS.add( this );
    }

    @Override
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation( this , 0 , new ModelResourceLocation( Objects.requireNonNull( super.getRegistryName() ).toString() ) );
    }

}
