package shukaro.warptheory.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class IWarpEvent
{
    public abstract String getName();

    public abstract int getSeverity();

    public final int getCost() { return (int)Math.ceil(getSeverity() / 10); }

    public abstract boolean doEvent(World world, EntityPlayer player);
}
