package shukaro.warptheory.net;

import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import shukaro.warptheory.WarpTheory;

import java.util.EnumMap;

public class CommonProxy
{
    public static EnumMap<Side, FMLEmbeddedChannel> warpChannel;

    public void init()
    {
        warpChannel = NetworkRegistry.INSTANCE.newChannel(WarpTheory.modID, new WarpMessageToMessageCodec(), new PacketHandler());
    }

    public EntityPlayer getPlayer()
    {
        return null;
    }
}
