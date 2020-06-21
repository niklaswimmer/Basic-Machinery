package n1kx.mods.basicmachinery.util.generics;

import n1kx.mods.basicmachinery.BasicMachinery;
import n1kx.mods.basicmachinery.list.ItemList;
import n1kx.mods.basicmachinery.util.IHasModel;
import n1kx.mods.basicmachinery.util.Methods;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

import java.util.Objects;

public class GenericItem extends Item implements IHasModel {

    public GenericItem( String name ) {
        super.setRegistryName( Methods.newRegistryName( name ) );
        super.setTranslationKey( Methods.newUnlocalizedName( name ) );
        super.setCreativeTab( BasicMachinery.CREATIVE_TAB );

        ItemList.ITEMS.add( this );
    }

    @Override
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation( this , 0 , new ModelResourceLocation( Objects.requireNonNull( super.getRegistryName() ).toString() ) );
    }

}
