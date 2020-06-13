package n1kx.mods.basicmachinery.block;

import n1kx.mods.basicmachinery.tileentity.TileEntityCrusher;
import n1kx.mods.basicmachinery.util.generics.GenericBlock;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockCrusher extends GenericBlock implements ITileEntityProvider {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockCrusher( String name , Material material , int guiID ) {
        super( name , material , guiID );
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer( this , FACING );
    }

    @Override
    public IBlockState getStateForPlacement( World worldIn , BlockPos pos , EnumFacing facing , float hitX , float hitY , float hitZ , int meta , EntityLivingBase placer ) {
        return super.getDefaultState().withProperty( FACING , placer.getHorizontalFacing().getOpposite() );
    }

    @Override
    public int getMetaFromState( IBlockState state ) {
        return state.getValue( FACING ).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta( int meta ) {
        return this.getDefaultState().withProperty( FACING , EnumFacing.getFront( meta ) );
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity( World worldIn , int meta ) {
        return new TileEntityCrusher();
    }
}
