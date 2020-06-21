package n1kx.mods.basicmachinery.util;

import n1kx.mods.basicmachinery.BasicMachinery;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

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

    public static boolean compareItemStacks( ItemStack[] stacks , ItemStack[] stacks1 ) {
        if( stacks.length != stacks1.length ) return false;
        for( int i = 0 ; i < stacks.length ; i++ ) {
            if( !( stacks[i].isItemEqual( stacks1[i] ) ) ) return false;
        }
        return true;
    }

    public static boolean areItemStacksEqual( ItemStack stack , ItemStack stack1 ) {
        return stack.isItemEqual( stack1 ) && stack.getCount() == stack1.getCount();
    }

}
