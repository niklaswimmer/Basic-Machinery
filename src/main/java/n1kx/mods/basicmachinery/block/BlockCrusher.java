package n1kx.mods.basicmachinery.block;

import mcp.MethodsReturnNonnullByDefault;
import n1kx.mods.basicmachinery.tileentity.TileEntityCrusher;
import n1kx.mods.basicmachinery.util.IHasBurningState;
import n1kx.mods.basicmachinery.util.IHasWorkingState;
import n1kx.mods.basicmachinery.util.generics.GenericBlockRotatable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockCrusher extends GenericBlockRotatable implements ITileEntityProvider, IHasWorkingState , IHasBurningState {

    public static final PropertyBool WORKING = PropertyBool.create( "working" );
    public static final PropertyBool BURNING = PropertyBool.create( "burning" );

    public BlockCrusher( String name , Material material , int guiID ) {
        super( name , material , guiID );

        super.setHarvestLevel( "pickaxe" , 1 );
        super.setHardness( 3.5f );
        super.setResistance( 3.5f );

        super.setDefaultState( super.getDefaultState()
                .withProperty( BlockCrusher.WORKING , false )
                .withProperty( BlockCrusher.BURNING , false )
        );

    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer( this , BlockCrusher.FACING , BlockCrusher.WORKING , BlockCrusher.BURNING );
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
        super.setBoolState( isWorking , worldIn , pos , BlockCrusher.WORKING );
    }

    @Override
    public void setBurningState( boolean isBurning , World worldIn , BlockPos pos ) {
        super.setBoolState( isBurning , worldIn , pos , BlockCrusher.BURNING );
    }

}
