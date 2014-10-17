package shukaro.warptheory.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;

public class WarpEventHandler
{
    @SubscribeEvent
    public void livingUpdate(LivingEvent.LivingUpdateEvent e)
    {
        if (e.entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)e.entity;
            if (player.ticksExisted % 2000 != 0 || WarpHandler.wuss || player.isPotionActive(WarpHandler.potionWarpWardID) || WarpHandler.getTotalWarp(player) <= 0)
                return;
            if (!player.capabilities.isCreativeMode && !player.worldObj.isRemote && player.worldObj.rand.nextInt(100) <= Math.sqrt(WarpHandler.getTotalWarp(player)))
            {
                IWarpEvent event = WarpHandler.doOneEvent(player, WarpHandler.getTotalWarp(player));
                int warpTemp = WarpHandler.getIndividualWarps(player)[2];
                if (warpTemp > 0 && event.getCost() <= warpTemp)
                    WarpHandler.removeWarp(player, event.getCost());
                else if (warpTemp > 0)
                    WarpHandler.removeWarp(player, warpTemp);
            }
        }
    }
}
