package n1kx.mods.basicmachinery.util.generics;

import n1kx.mods.basicmachinery.BasicMachinery;
import n1kx.mods.basicmachinery.list.BlockList;
import n1kx.mods.basicmachinery.util.IDropItemsOnBreak;
import n1kx.mods.basicmachinery.util.IHasModel;
import n1kx.mods.basicmachinery.util.Methods;
import n1kx.mods.basicmachinery.util.generics.tileentity.GenericTileEntityInventory;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class GenericBlock extends Block implements IHasModel {

    public final int guiID;

    public GenericBlock( String name , Material material , int guiID ) {
        super( material );

        this.guiID = guiID;

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

    @Override
    public boolean onBlockActivated( World worldIn , BlockPos pos , IBlockState state , EntityPlayer playerIn , EnumHand hand , EnumFacing facing , float hitX , float hitY , float hitZ ) {
        if( worldIn.isRemote ) {
            return false;
        }

        TileEntity tileEntity = worldIn.getTileEntity( pos );
        if( tileEntity instanceof GenericTileEntityInventory ) {
            playerIn.openGui( BasicMachinery.instance , ( (GenericTileEntityInventory)tileEntity ).getGuiID() , worldIn , pos.getX() , pos.getY() , pos.getZ() );
            return true;
        }

        return false;
    }

    @Override
    public void breakBlock( World worldIn , BlockPos pos , IBlockState blockState ) {
        TileEntity tileEntity = worldIn.getTileEntity( pos );

        if( tileEntity instanceof IDropItemsOnBreak ) {
            ( (IDropItemsOnBreak)tileEntity ).dropInventoryItems( worldIn, pos );
        }
    }
}
