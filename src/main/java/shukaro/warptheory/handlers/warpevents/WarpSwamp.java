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

public class WarpSwamp implements IWarpEvent
{
    @Override
    public String getName()
    {
        return "biomeSwamp";
    }

    @Override
    public int getCost()
    {
        return 15;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        if (!player.getEntityData().hasKey(WarpTheory.modID))
            player.getEntityData().setTag(WarpTheory.modID, new NBTTagCompound());
        if (!WarpHandler.canDoBiomeEvent(player, getName()))
            return false;
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.swampstart"));
        if (player.getEntityData().getCompoundTag(WarpTheory.modID).hasKey("biomeSwamp"))
            player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger("biomeSwamp", player.getEntityData().getCompoundTag(WarpTheory.modID).getInteger("biomeDecay") + 256 + world.rand.nextInt(256));
        else
            player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger("biomeSwamp", 256 + world.rand.nextInt(256));
        return true;
    }
}
