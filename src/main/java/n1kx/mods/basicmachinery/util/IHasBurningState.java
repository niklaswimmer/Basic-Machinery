package n1kx.mods.basicmachinery.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IHasBurningState {
    void setBurningState( boolean isBurning , World worldIn , BlockPos pos );
}
