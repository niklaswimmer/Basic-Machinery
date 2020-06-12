package n1kx.mods.basicmachinery.list;

import n1kx.mods.basicmachinery.block.BlockCrusher;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.ArrayList;

public class BlockList {

    public static final ArrayList<Block> BLOCKS = new ArrayList<Block>();

    public static final Block CRUSHER = new BlockCrusher( "crusher" , Material.IRON );

}
