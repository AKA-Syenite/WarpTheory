package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;

public class WarpFriend implements IWarpEvent
{
    @Override
    public String getName()
    {
        return "friend";
    }

    @Override
    public int getCost()
    {
        return 5;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.friendstart"));
        return false;
    }
}
