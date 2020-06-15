package n1kx.mods.basicmachinery.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IHasWorkingState {

    void setWorkingState( boolean isWorking , World worldIn , BlockPos pos );

}
