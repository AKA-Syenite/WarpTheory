package shukaro.warptheory.net;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import shukaro.warptheory.net.ClientProxy;
import shukaro.warptheory.net.CommonProxy;
import shukaro.warptheory.net.packets.BlinkPacket;
import shukaro.warptheory.net.packets.WarpPacket;
import shukaro.warptheory.net.packets.WindPacket;

public class PacketDispatcher
{
    public static void sendBlinkEvent(World world, double x, double y, double z)
    {
        try
        {
            int dim = world.provider.dimensionId;
            sendToAllAround(new BlinkPacket(x, y, z), dim, (int)x, (int)y, (int)z, 128);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void sendWindEvent(EntityPlayer player, double x, double y, double z)
    {
        try
        {
            sendToPlayer(new WindPacket(x, y, z), player);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static FMLEmbeddedChannel getClientOutboundChannel()
    {
        return ClientProxy.warpChannel.get(Side.CLIENT);
    }

    private static FMLEmbeddedChannel getServerOutboundChannel()
    {
        return CommonProxy.warpChannel.get(Side.SERVER);
    }

    private static void sendToServer(WarpPacket packet)
    {
        getClientOutboundChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        getClientOutboundChannel().writeOutbound(packet);
    }

    private static void sendToPlayer(WarpPacket packet, EntityPlayer player)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            getServerOutboundChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
            getServerOutboundChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
            getServerOutboundChannel().writeOutbound(packet);
        }
    }

    private static void sendToAll(WarpPacket packet)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            getServerOutboundChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
            getServerOutboundChannel().writeOutbound(packet);
        }
    }

    private static void sendToAllAround(WarpPacket packet, int dim, int x, int y, int z, int range)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            NetworkRegistry.TargetPoint tp = new NetworkRegistry.TargetPoint(dim, x, y, z, range);
            getServerOutboundChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
            getServerOutboundChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(tp);
            getServerOutboundChannel().writeOutbound(packet);
        }
    }

    private static void sendToAllInDim(WarpPacket packet, int dim)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            getServerOutboundChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
            getServerOutboundChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dim);
            getServerOutboundChannel().writeOutbound(packet);
        }
    }
}
