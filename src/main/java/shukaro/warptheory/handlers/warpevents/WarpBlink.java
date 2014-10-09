package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;

public class WarpBlink implements IWarpEvent
{
    @Override
    public String getName()
    {
        return "blink";
    }

    @Override
    public int getCost()
    {
        return 5;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.blinkstart"));
        if (!player.getEntityData().hasKey(WarpTheory.modID))
            player.getEntityData().setTag(WarpTheory.modID, new NBTTagCompound());
        if (player.getEntityData().getCompoundTag(WarpTheory.modID).hasKey("blink"))
            player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger("blink", player.getEntityData().getCompoundTag(WarpTheory.modID).getInteger("blink") + 10 + world.rand.nextInt(20));
        else
            player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger("blink", 10 + world.rand.nextInt(20));
        return true;
    }
}
