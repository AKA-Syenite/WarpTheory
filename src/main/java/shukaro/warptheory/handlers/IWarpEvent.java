package shukaro.warptheory.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class IWarpEvent
{
    public abstract String getName();

    public abstract int getSeverity();

    public final int getCost() { return (int)Math.ceil(getSeverity() / (double)10); }

    public boolean canDo(EntityPlayer player) { return true; }

    public abstract boolean doEvent(World world, EntityPlayer player);
}
