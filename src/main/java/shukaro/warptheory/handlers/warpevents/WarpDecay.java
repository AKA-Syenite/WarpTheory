package shukaro.warptheory.handlers.warpevents;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.*;

import java.util.ArrayList;
import java.util.Set;

public class WarpDecay extends IWarpEvent
{
    public WarpDecay() { FMLCommonHandler.instance().bus().register(this); }

    @Override
    public String getName()
    {
        return "biomeDecay";
    }

    @Override
    public int getSeverity()
    {
        return 50;
    }

    @Override
    public boolean canDo(EntityPlayer player)
    {
        for (String n : (Set<String>)MiscHelper.getWarpTag(player).func_150296_c())
        {
            if (n.startsWith("biome") && !n.equals(getName()))
                return false;
        }
        return true;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.decay"));
        MiscHelper.modEventInt(player, getName(), 256 * 2 + world.rand.nextInt(256));
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
    	if(WarpTheory.allowGlobalWarpEffects == false)
    		return;
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
            return;
        // Decay terrain
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (MiscHelper.getWarpTag(player).hasKey("biomeDecay"))
            {
                int decay = MiscHelper.getWarpTag(player).getInteger("biomeDecay");
                int targetX = (int)player.posX + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                int targetY = (int)player.posY + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                int targetZ = (int)player.posZ + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                BlockCoord target = new BlockCoord(targetX, targetY, targetZ);
                NameMetaPair pair = new NameMetaPair(target.getBlock(e.world), target.getMeta(e.world));
                if (!MiscHelper.hasNonSolidNeighbor(e.world, target))
                    return;
                if (WarpHandler.decayMappings.containsKey(pair) || pair.getBlock() instanceof IPlantable || pair.getBlock().getMaterial() == Material.leaves)
                {
                    NameMetaPair decayed = WarpHandler.decayMappings.get(pair);
                    if (decayed == null)
                        decayed = new NameMetaPair(Blocks.air, 0);
                    if (e.world.setBlock(target.x, target.y, target.z, decayed.getBlock(), decayed.getMetadata(), 3))
                    {
                        if (target.isAir(e.world))
                            e.world.playAuxSFXAtEntity(null, 2001, target.x, target.y, target.z, Block.getIdFromBlock(pair.getBlock()) + (pair.getMetadata() << 12));
                        MiscHelper.getWarpTag(player).setInteger("biomeDecay", --decay);
                        if (decay <= 0)
                            MiscHelper.getWarpTag(player).removeTag("biomeDecay");
                    }
                }
            }
        }
    }
}
