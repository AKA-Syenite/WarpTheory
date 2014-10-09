package shukaro.warptheory.handlers.warpevents;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
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

    public void onTick(World w, EntityPlayer p) {}

}
