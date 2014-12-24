package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;

public class WarpRain extends IWarpEvent
{
    @Override
    public String getName()
    {
        return "rain";
    }

    @Override
    public int getSeverity()
    {
        return 12;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
    	if(WarpTheory.allowGlobalWarpEffects == false)
    		return false;
    	
        if (!world.getWorldInfo().isThundering())
        {
            ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.rain"));
            world.getWorldInfo().setRaining(true);
            world.getWorldInfo().setThundering(true);
            return true;
        }
        return false;
    }
}
