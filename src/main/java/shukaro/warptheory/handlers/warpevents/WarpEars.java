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

public class WarpEars implements IWarpEvent
{
    @Override
    public String getName()
    {
        return "ears";
    }

    @Override
    public int getCost()
    {
        return 5;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.earsstart"));
        if (!player.getEntityData().hasKey(WarpTheory.modID))
            player.getEntityData().setTag(WarpTheory.modID, new NBTTagCompound());
        if (player.getEntityData().getCompoundTag(WarpTheory.modID).hasKey("ears"))
            player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger("ears", player.getEntityData().getCompoundTag(WarpTheory.modID).getInteger("ears") + 10 + world.rand.nextInt(30));
        else
            player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger("ears", 10 + world.rand.nextInt(30));
        return true;
    }
}
