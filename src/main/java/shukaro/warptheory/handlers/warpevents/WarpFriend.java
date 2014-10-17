package shukaro.warptheory.handlers.warpevents;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.entity.EntityPassiveCreeper;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

import java.util.ArrayList;

public class WarpFriend extends IWarpEvent
{
    public WarpFriend()
    {
        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public String getName()
    {
        return "friend";
    }

    @Override
    public int getSeverity()
    {
        return 26;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.friend"));
        MiscHelper.modTag(player, "friend", 1);
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
            return;
        // Spawning friend
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (MiscHelper.getTag(player, "friend") > 0)
            {
                int friend = MiscHelper.getTag(player, "friend");
                for (int i = 0; i < 6; i++)
                {
                    int targetX = (int)player.posX + e.world.rand.nextInt(4) - e.world.rand.nextInt(4);
                    int targetY = (int)player.posY + e.world.rand.nextInt(4) - e.world.rand.nextInt(4);
                    int targetZ = (int)player.posZ + e.world.rand.nextInt(4) - e.world.rand.nextInt(4);
                    if (e.world.isAirBlock(targetX, targetY, targetZ) && e.world.isAirBlock(targetX, targetY + 1, targetZ))
                    {
                        EntityPassiveCreeper creeper = new EntityPassiveCreeper(e.world);
                        try { creeper.setCustomNameTag(WarpTheory.normalNames.compose(e.world.rand.nextInt(3) + 2)); }
                        catch (Exception x) { x.printStackTrace(); }
                        creeper.playLivingSound();
                        creeper.setLocationAndAngles((double)targetX + e.world.rand.nextDouble(), (double)targetY + e.world.rand.nextDouble(), (double)targetZ + e.world.rand.nextDouble(), e.world.rand.nextFloat(), e.world.rand.nextFloat());
                        if (e.world.spawnEntityInWorld(creeper))
                        {
                            MiscHelper.setTag(player, "friend", --friend);
                            if (friend <= 0)
                                MiscHelper.removeTag(player, "friend");
                            break;
                        }
                    }
                }
            }
        }
    }
}
