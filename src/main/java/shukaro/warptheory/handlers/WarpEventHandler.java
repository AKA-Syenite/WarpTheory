package shukaro.warptheory.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.ServerChatEvent;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

public class WarpEventHandler
{
    @SubscribeEvent
    public void onMessageReceived(ServerChatEvent e)
    {
        if (!e.player.getEntityData().hasKey(WarpTheory.modID))
            return;

        // Warp tongue
        if (e.player.getEntityData().getCompoundTag(WarpTheory.modID).getInteger("tongues") > 0)
        {
            int tongues = e.player.getEntityData().getCompoundTag(WarpTheory.modID).getInteger("tongues");
            e.component = new ChatComponentTranslation(ChatHelper.getFormattedUsername(e.component) + " " + ChatHelper.garbleMessage(e.component));
            e.player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger("tongues", --tongues);
            if (tongues <= 0)
            {
                e.player.getEntityData().getCompoundTag(WarpTheory.modID).removeTag("tongues");
                ChatHelper.sendToPlayer(e.player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.tongueend"));
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onMessageReceived(ClientChatReceivedEvent e)
    {
        EntityPlayer player = MiscHelper.getServerSidePlayer();
        if (player == null || ChatHelper.getUsername(e.message).length() == 0 || player.getCommandSenderName().equals(ChatHelper.getUsername(e.message)))
            return;
        if (!player.getEntityData().hasKey(WarpTheory.modID))
            return;

        // Warp ears
        if (player.getEntityData().getCompoundTag(WarpTheory.modID).getInteger("ears") > 0)
        {
            int ears = player.getEntityData().getCompoundTag(WarpTheory.modID).getInteger("ears");
            e.message = new ChatComponentText(ChatHelper.getFormattedUsername(e.message) + " " + ChatHelper.garbleMessage(e.message));
            player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger("ears", --ears);
            if (ears <= 0)
            {
                player.getEntityData().getCompoundTag(WarpTheory.modID).removeTag("ears");
                ChatHelper.sendToPlayer(Minecraft.getMinecraft().thePlayer, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.earsend"));
            }
        }
    }
}
