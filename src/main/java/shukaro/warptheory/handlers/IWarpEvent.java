package shukaro.warptheory.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IWarpEvent
{
    public String getName();

    public int getCost();

    public boolean doEvent(World world, EntityPlayer player);
}
