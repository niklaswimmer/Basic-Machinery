package n1kx.mods.basicmachinery.util;

import n1kx.mods.basicmachinery.BasicMachinery;
import n1kx.mods.basicmachinery.list.BlockList;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

import java.util.Objects;

public class GenericBlock extends Block implements IHasModel {

    public GenericBlock( String name , Material material ) {
        super( material );
        super.setRegistryName( Methods.newRegistryName( name ) );
        super.setUnlocalizedName( Methods.newUnlocalizedName( name ) );
        super.setCreativeTab( BasicMachinery.CREATIVE_TAB );

        BlockList.BLOCKS.add( this );
        new GenericItemBlock( this , name );
    }

    @Override
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( this ) , 0 , new ModelResourceLocation( Objects.requireNonNull( super.getRegistryName() ).toString() ) );
    }
}
