package n1kx.mods.basicmachinery.block;

import n1kx.mods.basicmachinery.tileentity.TileEntityCrusher;
import n1kx.mods.basicmachinery.util.IHasBurningState;
import n1kx.mods.basicmachinery.util.IHasWorkingState;
import n1kx.mods.basicmachinery.util.generics.GenericBlock;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockCrusher extends GenericBlock implements ITileEntityProvider, IHasWorkingState , IHasBurningState {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool WORKING = PropertyBool.create( "working" );
    public static final PropertyBool BURNING = PropertyBool.create( "burning" );

    public BlockCrusher( String name , Material material , int guiID ) {
        super( name , material , guiID );

        super.setHarvestLevel( "pickaxe" , 1 );
        super.setHardness( 3.5f );
        super.setResistance( 3.5f );

        super.setDefaultState( super.blockState.getBaseState()
                .withProperty( BlockCrusher.FACING , EnumFacing.NORTH )
                .withProperty( BlockCrusher.WORKING , false )
                .withProperty( BlockCrusher.BURNING , false )
        );
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer( this , BlockCrusher.FACING , BlockCrusher.WORKING , BlockCrusher.BURNING );
    }

    @Override
    public IBlockState getStateForPlacement( World worldIn , BlockPos pos , EnumFacing facing , float hitX , float hitY , float hitZ , int meta , EntityLivingBase placer , EnumHand hand ) {
        return super.getDefaultState().withProperty( BlockCrusher.FACING , placer.getHorizontalFacing().getOpposite() );
    }

    @Override
    public int getMetaFromState( IBlockState state ) {
        return state.getValue( BlockCrusher.FACING ).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta( int meta ) {
        return this.getDefaultState().withProperty( BlockCrusher.FACING , EnumFacing.getFront( meta ) );
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity( World worldIn , int meta ) {
        return new TileEntityCrusher();
    }

    @Override
    public boolean hasTileEntity( IBlockState state ) {
        return true;
    }

    @Override
    public void setWorkingState( boolean isWorking , World worldIn , BlockPos pos ) {
        worldIn.setBlockState( pos , worldIn.getBlockState( pos ).withProperty( BlockCrusher.WORKING , isWorking ) , 2 );
    }

    @Override
    public void setBurningState( boolean isBurning , World worldIn , BlockPos pos ) {
        worldIn.setBlockState( pos , worldIn.getBlockState( pos ).withProperty( BlockCrusher.BURNING , isBurning ) , 2 );
    }

}
