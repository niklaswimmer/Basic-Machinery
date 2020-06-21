package n1kx.mods.basicmachinery.proxy;

import n1kx.mods.basicmachinery.BasicMachinery;
import n1kx.mods.basicmachinery.list.BlockList;
import n1kx.mods.basicmachinery.list.ItemList;
import n1kx.mods.basicmachinery.tileentity.TileEntityCrusher;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Objects;

@Mod.EventBusSubscriber( modid = BasicMachinery.ID )
public class CommonProxy {

    public void preInit( FMLPreInitializationEvent event ) {

    }

    public void init( FMLInitializationEvent event ) {

    }

    public void postInit( FMLPostInitializationEvent event ) {

    }

    @SubscribeEvent
    public static void registerBlocks( RegistryEvent.Register<Block> register ) {
        register.getRegistry().registerAll( BlockList.BLOCKS.toArray( new Block[0] ) );

        GameRegistry.registerTileEntity( TileEntityCrusher.class , Objects.requireNonNull( BlockList.CRUSHER.getRegistryName() ) );
    }

    @SubscribeEvent
    public static void registerItems( RegistryEvent.Register<Item> register ) {
        register.getRegistry().registerAll( ItemList.ITEMS.toArray( new Item[0] ) );
        register.getRegistry().registerAll( ItemList.ITEM_BLOCKS.toArray( new Item[0] ) );
    }

}
