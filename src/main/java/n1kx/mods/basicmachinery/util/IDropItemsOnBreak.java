package n1kx.mods.basicmachinery.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IDropItemsOnBreak {

    void dropInventoryItems( World worldIn , BlockPos pos );

}
