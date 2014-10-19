package shukaro.warptheory.net;

import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import shukaro.warptheory.net.packets.*;

public class WarpMessageToMessageCodec extends FMLIndexedMessageToMessageCodec<IWarpPacket>
{
    public static final int BLINKEVENT = 1;
    public static final int WINDEVENT = 2;
    public static final int BLOODEVENT = 3;
    public static final int CLEAREVENT = 4;
    public static final int DECREMENTEVENT = 5;
    public static final int CLIENTEVENT = 6;

    public WarpMessageToMessageCodec()
    {
        addDiscriminator(BLINKEVENT, EnderParticlesPacket.class);
        addDiscriminator(WINDEVENT, VelocityPacket.class);
        addDiscriminator(BLOODEVENT, BloodPacket.class);
        addDiscriminator(CLEAREVENT, ClearPacket.class);
        addDiscriminator(DECREMENTEVENT, DecrementPacket.class);
        addDiscriminator(CLIENTEVENT, ClientEventPacket.class);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, IWarpPacket msg, ByteBuf target) throws Exception
    {
        msg.writeBytes(target);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, IWarpPacket msg)
    {
        msg.readBytes(source);
    }
}
