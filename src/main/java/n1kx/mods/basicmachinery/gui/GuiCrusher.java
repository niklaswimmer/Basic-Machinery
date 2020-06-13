package n1kx.mods.basicmachinery.gui;

import n1kx.mods.basicmachinery.container.ContainerCrusher;
import n1kx.mods.basicmachinery.tileentity.TileEntityCrusher;
import n1kx.mods.basicmachinery.util.generics.GenericGui;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiCrusher extends GenericGui {

    public GuiCrusher( InventoryPlayer playerInventory , TileEntityCrusher tileEntity ) {
        super( new ContainerCrusher( playerInventory , tileEntity ) );
    }

}
