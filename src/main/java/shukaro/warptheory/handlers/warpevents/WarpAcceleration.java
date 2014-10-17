package shukaro.warptheory.handlers.warpevents;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

import java.util.ArrayList;

public class WarpAcceleration extends IWarpEvent
{
    public WarpAcceleration() { FMLCommonHandler.instance().bus().register(this); }

    @Override
    public String getName()
    {
        return "acceleration";
    }

    @Override
    public int getSeverity()
    {
        return 27;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.acceleration"));
        MiscHelper.modTag(player, "acceleration", 6000 + world.rand.nextInt(12000));
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
            return;
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (MiscHelper.getTag(player, "acceleration") > 0)
            {
                int acceleration = MiscHelper.getTag(player, "acceleration");
                e.world.setWorldTime(e.world.getWorldTime() + 2);
                MiscHelper.setTag(player, "acceleration", --acceleration);
                if (acceleration <= 0)
                    MiscHelper.removeTag(player, "acceleration");
            }
        }
    }
}
