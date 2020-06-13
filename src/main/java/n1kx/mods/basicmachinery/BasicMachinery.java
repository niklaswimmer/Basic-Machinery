package n1kx.mods.basicmachinery;

import n1kx.mods.basicmachinery.proxy.CommonProxy;
import n1kx.mods.basicmachinery.proxy.GuiProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

@Mod( modid = BasicMachinery.ID, name = BasicMachinery.NAME, version = BasicMachinery.VERSION , dependencies = BasicMachinery.DEPENDENCIES , acceptedMinecraftVersions = BasicMachinery.MC_VERSIONS )
public class BasicMachinery {

    public static final String ID = "basicmachinery";
    public static final String NAME = "Basic Machinery";
    public static final String VERSION = "0.0.1";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2847,)";
    public static final String MC_VERSIONS = "[1.12.2]";

    public static Logger logger;

    @Mod.Instance()
    public static BasicMachinery instance;

    @SidedProxy( clientSide = "n1kx.mods.basicmachinery.proxy.ClientProxy" , serverSide = "n1kx.mods.basicmachinery.proxy.ServerProxy" )
    public static CommonProxy proxy;

    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs( "basicmachinery" ) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack( Blocks.FURNACE );
        }
    };

    @Mod.EventHandler
    public void preInit( FMLPreInitializationEvent event ) {
        logger = event.getModLog();
        logger.info( "preInit" );
    }

    @Mod.EventHandler
    public void init( FMLInitializationEvent e ) {
        logger.info( "init" );
        NetworkRegistry.INSTANCE.registerGuiHandler( BasicMachinery.instance , new GuiProxy() );
    }

    @Mod.EventHandler
    public void postInit( FMLPostInitializationEvent e ) {
        logger.info( "postInit" );
    }
}
