package shukaro.warptheory.handlers.warpevents;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

public class WarpEars extends IWarpEvent
{
    public WarpEars() { MinecraftForge.EVENT_BUS.register(this); }

    @Override
    public String getName()
    {
        return "ears";
    }

    @Override
    public int getSeverity()
    {
        return 16;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.ears"));
        MiscHelper.modTag(player, "ears", 10 + world.rand.nextInt(30));
        return true;
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
        if (MiscHelper.getTag(player, "ears") > 0)
        {
            int ears = MiscHelper.getTag(player, "ears");
            e.message = new ChatComponentText(ChatHelper.getFormattedUsername(e.message) + " " + ChatHelper.garbleMessage(e.message));
            MiscHelper.setTag(player, "ears", --ears);
            if (ears <= 0)
                MiscHelper.removeTag(player, "ears");
        }
    }
}
