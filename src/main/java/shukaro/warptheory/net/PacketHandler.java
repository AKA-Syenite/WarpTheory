package shukaro.warptheory.net;

import cpw.mods.fml.common.network.NetworkRegistry;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.warpevents.WarpBlood;
import shukaro.warptheory.net.packets.*;
import shukaro.warptheory.util.BlockCoord;

import java.util.ArrayList;
import java.util.HashMap;

@ChannelHandler.Sharable
public class PacketHandler extends SimpleChannelInboundHandler<WarpPacket>
{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WarpPacket msg) throws Exception
    {
        INetHandler handler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();

        if (handler instanceof NetHandlerPlayServer)
        {
        }
        else if (handler instanceof NetHandlerPlayClient)
        {
            if (msg instanceof EnderParticlesPacket)
            {
                EnderParticlesPacket blink = (EnderParticlesPacket)msg;
                World world = Minecraft.getMinecraft().thePlayer.worldObj;
                for (int l = 0; l < 128; ++l)
                    world.spawnParticle("portal", blink.x + world.rand.nextDouble() - world.rand.nextDouble(), blink.y + world.rand.nextDouble() - world.rand.nextDouble(), blink.z + world.rand.nextDouble() - world.rand.nextDouble(), (double)(world.rand.nextFloat() - 0.5F) * 0.2F, (double)(world.rand.nextFloat() - 0.5F) * 0.2F, (double)(world.rand.nextFloat() - 0.5F) * 0.2F);
            }
            else if (msg instanceof VelocityPacket)
            {
                VelocityPacket wind = (VelocityPacket)msg;
                Minecraft.getMinecraft().thePlayer.addVelocity(wind.x, wind.y, wind.z);
            }
            else if (msg instanceof BloodPacket)
            {
                BloodPacket blood = (BloodPacket)msg;
                if (WarpBlood.bloody.get(blood.dim) == null)
                    WarpBlood.bloody.put(blood.dim, new ArrayList<BlockCoord>());
                WarpBlood.bloody.get(blood.dim).add(new BlockCoord(blood.x, blood.y, blood.z));
            }
            else if (msg instanceof ClearPacket)
            {
                ClearPacket clear = (ClearPacket)msg;
                if (clear.id == 0)
                    WarpBlood.bloody = new HashMap<Integer, ArrayList<BlockCoord>>();
            }
        }
    }
}
