package n1kx.mods.basicmachinery.proxy;

import n1kx.mods.basicmachinery.BasicMachinery;
import n1kx.mods.basicmachinery.list.ItemList;
import n1kx.mods.basicmachinery.util.IHasModel;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber( value = Side.CLIENT  , modid = BasicMachinery.ID )
public class ClientProxy extends CommonProxy {

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

    @SubscribeEvent
    public static void registerModels( ModelRegistryEvent event ) {
        for( Item item : ItemList.ITEMS ) {
            if( item instanceof IHasModel ) {
                ( (IHasModel)item ).initModel();
            }
        }
    }

}
