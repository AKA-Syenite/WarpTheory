package shukaro.warptheory.handlers.warpevents;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

public class WarpTongue extends IWarpEvent
{
    public WarpTongue() { MinecraftForge.EVENT_BUS.register(this); }

    @Override
    public String getName()
    {
        return "tongue";
    }

    @Override
    public int getSeverity()
    {
        return 11;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.tongue"));
        MiscHelper.modEventInt(player, "tongues", 10 + world.rand.nextInt(15));
        return true;
    }

    @SubscribeEvent
    public void onMessageReceived(ServerChatEvent e)
    {
        // Warp tongue
        if (MiscHelper.getWarpTag(e.player).hasKey("tongues"))
        {
            int tongues = MiscHelper.getWarpTag(e.player).getInteger("tongues");
            e.component = new ChatComponentTranslation(ChatHelper.getFormattedUsername(e.component) + " " + ChatHelper.garbleMessage(e.component));
            MiscHelper.getWarpTag(e.player).setInteger("tongues", --tongues);
            if (tongues <= 0)
                MiscHelper.getWarpTag(e.player).removeTag("tongues");
        }
    }
}
