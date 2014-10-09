package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;

import java.util.Iterator;

public class WarpDecay implements IWarpEvent
{
    @Override
    public String getName()
    {
        return "biomeDecay";
    }

    @Override
    public int getCost()
    {
        return 10;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        if (!player.getEntityData().hasKey(WarpTheory.modID))
            player.getEntityData().setTag(WarpTheory.modID, new NBTTagCompound());
        if (!WarpHandler.canDoBiomeEvent(player, getName()))
            return false;
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.decaystart"));
        if (player.getEntityData().getCompoundTag(WarpTheory.modID).hasKey("biomeDecay"))
            player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger("biomeDecay", player.getEntityData().getCompoundTag(WarpTheory.modID).getInteger("biomeDecay") + 256*2 + world.rand.nextInt(256));
        else
            player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger("biomeDecay", 256*2 + world.rand.nextInt(256));
        return true;
    }
}
