package n1kx.mods.basicmachinery.util;

import n1kx.mods.basicmachinery.BasicMachinery;
import n1kx.mods.basicmachinery.tileentity.TileEntityCrusher;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nonnull;

public class Methods {

    public static ResourceLocation newRegistryName( String name ) {
        return new ResourceLocation( BasicMachinery.ID , name );
    }

    public static String newUnlocalizedName( String name ) {
        return BasicMachinery.ID +"."+ name;
    }

    public static ResourceLocation newGuiLocation( ResourceLocation resourceLocation ) {
        String name = resourceLocation.getPath();
        return new ResourceLocation( BasicMachinery.ID , "textures/gui/"+ name +".png" );
    }

    public static boolean isFuel( ItemStack stack ) {
        return TileEntityFurnace.isItemFuel( stack );
    }

    public static int getFuelValue( ItemStack stack ) {
        return TileEntityFurnace.getItemBurnTime( stack );
    }

}
