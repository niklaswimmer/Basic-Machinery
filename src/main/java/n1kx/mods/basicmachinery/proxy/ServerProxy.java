package n1kx.mods.basicmachinery.proxy;

import n1kx.mods.basicmachinery.BasicMachinery;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber( value = Side.SERVER , modid = BasicMachinery.ID )
public class ServerProxy extends CommonProxy {

    @Override
    public void preInit( FMLPreInitializationEvent event ) {
        super.preInit( event );
    }

    @Override
    public void init( FMLInitializationEvent event ) {
        super.init( event );
    }

    @Override
    public void postInit( FMLPostInitializationEvent event ) {
        super.postInit( event );
    }

}