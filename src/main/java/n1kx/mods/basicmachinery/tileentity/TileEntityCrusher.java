package n1kx.mods.basicmachinery.tileentity;

import n1kx.mods.basicmachinery.list.BlockList;
import n1kx.mods.basicmachinery.util.GenericTileEntity;
import net.minecraft.util.ITickable;

public class TileEntityCrusher extends GenericTileEntity implements ITickable {

    public TileEntityCrusher() {
        super( 1 , 1 , 1 , BlockList.CRUSHER );
    }

    @Override
    public void update() {
        //TODO ahm jeah implement an update method... maybe??
    }

}
