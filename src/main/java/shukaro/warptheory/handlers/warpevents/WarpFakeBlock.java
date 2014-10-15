package shukaro.warptheory.handlers.warpevents;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWarpEvent;

public class WarpFakeBlock implements IWarpEvent
{
    @Override
    public String getName()
    {
        return "fakeblock";
    }

    @Override
    public int getCost()
    {
        return 0;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        return false;
    }
}
