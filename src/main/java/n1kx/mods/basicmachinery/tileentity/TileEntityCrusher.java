package n1kx.mods.basicmachinery.tileentity;

import n1kx.mods.basicmachinery.list.BlockList;
import n1kx.mods.basicmachinery.network.MessageSyncCrusher;
import n1kx.mods.basicmachinery.network.PacketHandler;
import n1kx.mods.basicmachinery.recipes.RecipesCrusher;
import n1kx.mods.basicmachinery.util.IHasGui;
import n1kx.mods.basicmachinery.util.generics.GenericBlock;
import n1kx.mods.basicmachinery.util.generics.tileentity.GenericTEMachineFueled;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class TileEntityCrusher extends GenericTEMachineFueled implements IHasGui {

    public TileEntityCrusher() {
        super( 1 , 1 , 1 , (GenericBlock)BlockList.CRUSHER , new RecipesCrusher() , null );
    }

    public int getGuiID() {
        return super.block.guiID;
    }

    @Override
    protected void sendChangesToClient() {
        PacketHandler.INSTANCE.sendToAllTracking( new MessageSyncCrusher( super.timeLeft , super.totalTimeNeeded , super.burnTimeLeft , super.totalBurnTime , super.pos ) , super.getTargetPoint() );
    }

}
