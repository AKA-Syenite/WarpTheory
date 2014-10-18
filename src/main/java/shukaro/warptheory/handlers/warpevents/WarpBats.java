package shukaro.warptheory.handlers.warpevents;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

import java.util.ArrayList;

public class WarpBats extends IWarpEvent
{
    public WarpBats()
    {
        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public String getName()
    {
        return "bats";
    }

    @Override
    public int getSeverity()
    {
        return 15;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.bats"));
        MiscHelper.modEventInt(player, "bats", 15 + world.rand.nextInt(30));
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
            return;
        // Spawning bats
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (MiscHelper.getWarpTag(player).hasKey("bats"))
            {
                int bats = MiscHelper.getWarpTag(player).getInteger("bats");
                for (int i = 0; i < 6; i++)
                {
                    int targetX = (int)player.posX + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                    int targetY = (int)player.posY + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                    int targetZ = (int)player.posZ + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                    if (e.world.isAirBlock(targetX, targetY, targetZ))
                    {
                        EntityBat bat = new EntityBat(e.world);
                        bat.playLivingSound();
                        bat.setLocationAndAngles((double)targetX + e.world.rand.nextDouble(), (double)targetY + e.world.rand.nextDouble(), (double)targetZ + e.world.rand.nextDouble(), e.world.rand.nextFloat(), e.world.rand.nextFloat());
                        if (e.world.spawnEntityInWorld(bat))
                        {
                            MiscHelper.getWarpTag(player).setInteger("bats", --bats);
                            if (bats <= 0)
                                MiscHelper.getWarpTag(player).removeTag("bats");
                            break;
                        }
                    }
                }
            }
        }
    }
}
