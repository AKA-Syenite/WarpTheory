package shukaro.warptheory.handlers.warpevents;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

import java.util.ArrayList;

public class WarpLightning extends IWarpEvent
{
    public WarpLightning()
    {
        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public String getName()
    {
        return "lightning";
    }

    @Override
    public int getSeverity()
    {
        return 60;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.lightning"));
        MiscHelper.modEventInt(player, "lightning", 5 + world.rand.nextInt(10));
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
            return;
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (MiscHelper.getWarpTag(player).hasKey("lightning"))
            {
                int lightning = MiscHelper.getWarpTag(player).getInteger("lightning");
                int x = (int)player.posX + e.world.rand.nextInt(3) - e.world.rand.nextInt(3);
                int y = (int)player.posY;
                int z = (int)player.posZ + e.world.rand.nextInt(3) - e.world.rand.nextInt(3);
                if (e.world.rand.nextInt(100) == 0 && e.world.canBlockSeeTheSky(x, y, z))
                {
                    EntityLightningBolt bolt = new EntityLightningBolt(e.world, x, y, z);
                    e.world.addWeatherEffect(bolt);
                    MiscHelper.getWarpTag(player).setInteger("lightning", --lightning);
                    if (lightning <= 0)
                        MiscHelper.getWarpTag(player).removeTag("lightning");
                }
            }
        }
    }
}
