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

public class WarpTongue implements IWarpEvent
{
    public WarpTongue() { MinecraftForge.EVENT_BUS.register(this); }

    @Override
    public String getName()
    {
        return "tongue";
    }

    @Override
    public int getCost()
    {
        return 5;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.tonguestart"));
        MiscHelper.modTag(player, "toungues", 10 + world.rand.nextInt(15));
        return true;
    }

    @SubscribeEvent
    public void onMessageReceived(ServerChatEvent e)
    {
        // Warp tongue
        if (MiscHelper.getTag(e.player, "tongues") > 0)
        {
            int tongues = MiscHelper.getTag(e.player, "tongues");
            e.component = new ChatComponentTranslation(ChatHelper.getFormattedUsername(e.component) + " " + ChatHelper.garbleMessage(e.component));
            MiscHelper.setTag(e.player, "tongues", --tongues);
            if (tongues <= 0)
            {
                MiscHelper.removeTag(e.player, "tongues");
                ChatHelper.sendToPlayer(e.player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.tongueend"));
            }
        }
    }
}
