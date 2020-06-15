package n1kx.mods.basicmachinery.proxy;

import n1kx.mods.basicmachinery.list.ItemList;
import n1kx.mods.basicmachinery.util.IHasModel;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber( Side.CLIENT )
public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    public static void registerModels( ModelRegistryEvent event ) {
        for( Item item : ItemList.ITEMS ) {
            if( item instanceof IHasModel ) {
                ( (IHasModel)item ).initModel();
            }
        }
    }

}
