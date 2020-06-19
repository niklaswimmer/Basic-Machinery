package n1kx.mods.basicmachinery.util.generics;

import mcp.MethodsReturnNonnullByDefault;
import n1kx.mods.basicmachinery.block.BlockCrusher;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GenericBlockRotatable extends GenericBlock {

    public static final PropertyDirection FACING = PropertyDirection.create( "facing" , EnumFacing.Plane.HORIZONTAL );

    public GenericBlockRotatable( String name , Material material , int guiID ) {
        super( name , material , guiID );

        super.setDefaultState( super.blockState.getBaseState().withProperty( BlockCrusher.FACING , EnumFacing.NORTH ) );
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer( this , BlockCrusher.FACING );
    }

    @Override
    public IBlockState getStateForPlacement( World worldIn , BlockPos pos , EnumFacing facing , float hitX , float hitY , float hitZ , int meta , EntityLivingBase placer , EnumHand hand ) {
        return super.getDefaultState().withProperty( BlockCrusher.FACING , placer.getHorizontalFacing().getOpposite() );
    }

    @Override
    public int getMetaFromState( IBlockState state ) {
        return state.getValue( BlockCrusher.FACING ).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta( int meta ) {
        return this.getDefaultState().withProperty( BlockCrusher.FACING , EnumFacing.byHorizontalIndex( meta ) );
    }

}
