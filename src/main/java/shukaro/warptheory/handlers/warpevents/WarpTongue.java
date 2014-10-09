package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.handlers.WarpEventHandler;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;

public class WarpTongue implements IWarpEvent
{
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
        if (!player.getEntityData().hasKey(WarpTheory.modID))
            player.getEntityData().setTag(WarpTheory.modID, new NBTTagCompound());
        if (player.getEntityData().getCompoundTag(WarpTheory.modID).hasKey("tongues"))
            player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger("tongues", player.getEntityData().getCompoundTag(WarpTheory.modID).getInteger("tongues") + 10 + world.rand.nextInt(15));
        else
            player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger("tongues", 10 + world.rand.nextInt(15));
        return true;
    }
}
