package n1kx.mods.basicmachinery.proxy;

import n1kx.mods.basicmachinery.BasicMachinery;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber( value = Side.SERVER , modid = BasicMachinery.ID )
public class ServerProxy extends CommonProxy {

}
