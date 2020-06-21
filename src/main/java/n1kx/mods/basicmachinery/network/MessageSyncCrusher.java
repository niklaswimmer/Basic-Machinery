package n1kx.mods.basicmachinery.network;

import io.netty.buffer.ByteBuf;
import n1kx.mods.basicmachinery.BasicMachinery;
import n1kx.mods.basicmachinery.tileentity.TileEntityCrusher;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSyncCrusher implements IMessage {

    private int timeLeft;
    private int totalTimeNeeded;
    private int burnTimeLeft;
    private int totalBurnTime;

    private BlockPos tileEntityPos;

    public MessageSyncCrusher() {}

    public MessageSyncCrusher( int timeLeft , int totalTimeNeeded , int burnTimeLeft , int totalBurnTime , BlockPos tileEntityPos ) {
        this.timeLeft = timeLeft;
        this.totalTimeNeeded = totalTimeNeeded;
        this.burnTimeLeft = burnTimeLeft;
        this.totalBurnTime = totalBurnTime;
        this.tileEntityPos = tileEntityPos;
    }

    @Override
    public void fromBytes( ByteBuf buf ) {
        this.timeLeft = buf.readInt();
        this.totalTimeNeeded = buf.readInt();
        this.burnTimeLeft = buf.readInt();
        this.totalBurnTime = buf.readInt();
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        this.tileEntityPos = new BlockPos( x , y , z );
    }

    @Override
    public void toBytes( ByteBuf buf ) {
        buf.writeInt( this.timeLeft );
        buf.writeInt( this.totalTimeNeeded );
        buf.writeInt( this.burnTimeLeft );
        buf.writeInt( this.totalBurnTime );
        buf.writeInt( this.tileEntityPos.getX() );
        buf.writeInt( this.tileEntityPos.getY() );
        buf.writeInt( this.tileEntityPos.getZ() );
    }

    public static class MessageHolder implements IMessageHandler<MessageSyncCrusher , IMessage> {
        @Override
        public IMessage onMessage( final MessageSyncCrusher message , final MessageContext ctx ) {
            if( message.tileEntityPos != null ) {
                World world = Minecraft.getMinecraft().world;
                TileEntity tileEntity = world.getTileEntity( message.tileEntityPos );
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    if( tileEntity instanceof TileEntityCrusher ) {
                        ( (TileEntityCrusher)tileEntity ).setField( 0 , message.timeLeft );
                        ( (TileEntityCrusher)tileEntity ).setField( 1 , message.totalTimeNeeded );
                        ( (TileEntityCrusher)tileEntity ).setField( 2 , message.burnTimeLeft );
                        ( (TileEntityCrusher)tileEntity ).setField( 3 , message.totalBurnTime );
                    }
                });
            }
            return null;
        }
    }

}
