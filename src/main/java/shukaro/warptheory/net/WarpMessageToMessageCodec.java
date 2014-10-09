package shukaro.warptheory.net;

import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import shukaro.warptheory.net.packets.BlinkPacket;
import shukaro.warptheory.net.packets.WarpPacket;

public class WarpMessageToMessageCodec extends FMLIndexedMessageToMessageCodec<WarpPacket>
{
    public static final int BLINKEVENT = 1;

    public WarpMessageToMessageCodec()
    {
        addDiscriminator(BLINKEVENT, BlinkPacket.class);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, WarpPacket msg, ByteBuf target) throws Exception
    {
        msg.writeBytes(target);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, WarpPacket msg)
    {
        msg.readBytes(source);
    }
}
