package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

public class WarpBats implements IWarpEvent
{
    @Override
    public String getName()
    {
        return "bats";
    }

    @Override
    public int getCost()
    {
        return 10;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.batsstart"));
        /*
        if (!player.getEntityData().hasKey(WarpTheory.modID))
            player.getEntityData().setTag(WarpTheory.modID, new NBTTagCompound());
        if (player.getEntityData().getCompoundTag(WarpTheory.modID).hasKey("bats"))
            player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger("bats", player.getEntityData().getCompoundTag(WarpTheory.modID).getInteger("bats") + 15 + world.rand.nextInt(30));
        else
            player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger("bats", 15 + world.rand.nextInt(30));
        return true;
        */
        //Set tag on player inside the WarpTheory compound tag.
        //Creates compound if it does not exist.
        //Creates tag if it does not exist.
        //If tag exists, value is added.
        //Should probably name this better than "setTag" since it increments if exists.  "addToTag"?
        MiscHelper.setTag(player, "bats", 15 + world.rand.nextInt(30));
        return true;
    }
}
