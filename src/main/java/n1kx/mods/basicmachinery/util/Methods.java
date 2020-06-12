package n1kx.mods.basicmachinery.util;

import n1kx.mods.basicmachinery.BasicMachinery;
import net.minecraft.util.ResourceLocation;

public class Methods {

    public static ResourceLocation newRegistryName( String name ) {
        return new ResourceLocation( BasicMachinery.ID , name );
    }

    public static String newUnlocalizedName( String name ) {
        return BasicMachinery.ID +"."+ name;
    }

}
