package shukaro.warptheory.handlers;

import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IWarpEvent
{
    public String getName();

    public int getCost();

    public boolean doEvent(World world, EntityPlayer player);

    public void onTick(World world, EntityPlayer player);
}
