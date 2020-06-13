package n1kx.mods.basicmachinery.list;

import n1kx.mods.basicmachinery.block.BlockCrusher;
import n1kx.mods.basicmachinery.util.ID;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.ArrayList;

public class BlockList {

    public static final ArrayList<Block> BLOCKS = new ArrayList<>();

    public static final Block CRUSHER = new BlockCrusher( "crusher" , Material.IRON , ID.getNextGuiID() );

}
