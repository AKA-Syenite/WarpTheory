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
            if (player.ticksExisted % 1200 == 0 && WarpHandler.getWarp(player) > 0 && player.worldObj.rand.nextBoolean() && !player.capabilities.isCreativeMode && !WarpHandler.wuss && !player.isPotionActive(WarpHandler.potionWarpWardID))
                WarpHandler.doOneWarp(player, WarpHandler.getWarp(player));
        }
    }
}
