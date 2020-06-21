package n1kx.mods.basicmachinery.network;

import n1kx.mods.basicmachinery.BasicMachinery;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

    public static SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel( BasicMachinery.ID );

    private static int id = 0;

    public static void registerMessages() {
        PacketHandler.INSTANCE.registerMessage( MessageSyncCrusher.MessageHolder.class , MessageSyncCrusher.class , PacketHandler.id++ , Side.CLIENT );
    }

}