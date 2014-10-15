package shukaro.warptheory.net;

import cpw.mods.fml.common.network.NetworkRegistry;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.net.packets.BlinkPacket;
import shukaro.warptheory.net.packets.ChestPacket;
import shukaro.warptheory.net.packets.WarpPacket;
import shukaro.warptheory.net.packets.WindPacket;

@ChannelHandler.Sharable
public class PacketHandler extends SimpleChannelInboundHandler<WarpPacket>
{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WarpPacket msg) throws Exception
    {
        INetHandler handler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
        WarpTheory.logger.info("got packet");
        if (handler instanceof NetHandlerPlayServer)
        {
        }
        else if (handler instanceof NetHandlerPlayClient)
        {
            WarpTheory.logger.info("for client");
            if (msg instanceof BlinkPacket)
            {
                BlinkPacket blink = (BlinkPacket)msg;
                World world = Minecraft.getMinecraft().thePlayer.worldObj;
                for (int l = 0; l < 128; ++l)
                    world.spawnParticle("portal", blink.x + world.rand.nextDouble() - world.rand.nextDouble(), blink.y + world.rand.nextDouble() - world.rand.nextDouble(), blink.z + world.rand.nextDouble() - world.rand.nextDouble(), (double)(world.rand.nextFloat() - 0.5F) * 0.2F, (double)(world.rand.nextFloat() - 0.5F) * 0.2F, (double)(world.rand.nextFloat() - 0.5F) * 0.2F);
            }
            else if (msg instanceof WindPacket)
            {
                WarpTheory.logger.info("for wind");
                WindPacket wind = (WindPacket)msg;
                Minecraft.getMinecraft().thePlayer.addVelocity(wind.x, wind.y, wind.z);
            }
            else if (msg instanceof ChestPacket)
            {
                WarpTheory.logger.info("for chest");
                ChestPacket chest = (ChestPacket)msg;
                World world = Minecraft.getMinecraft().theWorld;
                TileEntityChest tec = (TileEntityChest)world.getTileEntity(chest.x, chest.y, chest.z);
                tec.closeInventory();
            }
        }
    }
}
