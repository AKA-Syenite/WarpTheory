package shukaro.warptheory.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import shukaro.warptheory.WarpTheory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class WarpTickHandler
{
    private static Set<IWarpEvent> callbacks = new HashSet<>();
    //Add IWarpEvent to list of events to call on a per-tick basis.
    public static void register(IWarpEvent event) {
        if (!callbacks.contains(event)) {
            callbacks.add(event);
            return;
        }
        //TODO: proper error reporting
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END)
            return;
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (!player.getEntityData().hasKey(WarpTheory.modID))
                return;

            for (IWarpEvent event : callbacks) {
                event.onTick(e.world, player);
            }
        }
    }
}
