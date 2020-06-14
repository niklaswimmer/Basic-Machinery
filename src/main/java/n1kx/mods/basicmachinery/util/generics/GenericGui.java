package n1kx.mods.basicmachinery.util.generics;

import n1kx.mods.basicmachinery.util.Methods;
import n1kx.mods.basicmachinery.util.generics.tileentity.GenericTileEntityInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class GenericGui extends GuiContainer {

    public final ResourceLocation texture;

    private final InventoryPlayer playerInventory;
    private final GenericTileEntityInventory tileEntity;


    public GenericGui( GenericContainer inventorySlotsIn ) {
        super( inventorySlotsIn );

        this.playerInventory = inventorySlotsIn.playerInventory;
        this.tileEntity = inventorySlotsIn.tileEntity;

        this.texture = Methods.newGuiLocation( Objects.requireNonNull( tileEntity.block.getRegistryName() ) );
    }

    @Override
    protected void drawGuiContainerForegroundLayer( int mouseX , int mouseY ) {

    }

    @Override
    protected void drawGuiContainerBackgroundLayer( float partialTicks , int mouseX , int mouseY ) {
        GlStateManager.color( 1.0f , 1.0f , 1.0f , 1.0f );
        super.mc.getTextureManager().bindTexture( this.texture );
        super.drawTexturedModalRect( super.guiLeft , super.guiTop , 0 , 0 , super.xSize , super.ySize );
    }
}
