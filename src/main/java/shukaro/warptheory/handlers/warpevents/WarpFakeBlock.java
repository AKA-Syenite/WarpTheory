package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import shukaro.warptheory.handlers.IWarpEvent;

/**
 * Created by rob on 10/9/2014.
 * This file is licensed under GPLv2
 */
public class WarpFakeBlock implements IWarpEvent {

    public WarpFakeBlock() {
    }
    @Override
    public String getName() {
        return "fakeblock";
    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player) {
        return false;
    }

    //Not called per-tick, stubbed out.
    public void onTick(World w, EntityPlayer p) {}

}
