package n1kx.mods.basicmachinery.gui;

import n1kx.mods.basicmachinery.container.ContainerCrusher;
import n1kx.mods.basicmachinery.tileentity.TileEntityCrusher;
import n1kx.mods.basicmachinery.util.generics.GenericGui;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiCrusher extends GenericGui {

    public GuiCrusher( InventoryPlayer playerInventory , TileEntityCrusher tileEntity ) {
        super( new ContainerCrusher( playerInventory , tileEntity ) );
    }

    @Override
    protected void drawGuiContainerBackgroundLayer( float partialTicks , int mouseX , int mouseY ) {
        super.drawGuiContainerBackgroundLayer( partialTicks , mouseX , mouseY );

        int progressScaled = super.getProgressScaled( 10 );
        if( progressScaled != -1 ) {
            super.drawTexturedModalRect( super.guiLeft + 95 , super.guiTop + 38 , 176 , 33 , 6 , 10 - progressScaled );
        }

        int burnTimeScaled = super.getBurnTimeScaled( 33 );
        if( burnTimeScaled != -1 ) {
            this.drawTexturedModalRect( super.guiLeft + 16 , super.guiTop + 12 + 33 - burnTimeScaled , 176 , 33 - burnTimeScaled , 8 , burnTimeScaled );
        }
    }
}
