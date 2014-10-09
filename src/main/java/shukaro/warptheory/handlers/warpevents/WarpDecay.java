package shukaro.warptheory.handlers.warpevents;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.*;

public class WarpDecay implements IWarpEvent
{
    @Override
    public String getName()
    {
        return "biomeDecay";
    }

    @Override
    public int getCost()
    {
        return 10;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        if (!WarpHandler.canDoBiomeEvent(player, getName())) {
            return false;
        }
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.decaystart"));
        MiscHelper.addToTag(player, getName(), 256*2 + world.rand.nextInt(256));

        return true;
    }

    public void onTick(World world, EntityPlayer player) {
        if (MiscHelper.getTag(player, "biomeDecay") > 0)
        {
            int decay = MiscHelper.getTag(player, "biomeDecay");
            int targetX = (int)player.posX + world.rand.nextInt(8) - world.rand.nextInt(8);
            int targetY = (int)player.posY + world.rand.nextInt(8) - world.rand.nextInt(8);
            int targetZ = (int)player.posZ + world.rand.nextInt(8) - world.rand.nextInt(8);
            BlockCoord target = new BlockCoord(targetX, targetY, targetZ);
            NameMetaPair pair = new NameMetaPair(target.getBlock(world), target.getMeta(world));
            if (!MiscHelper.hasNonSolidNeighbor(world, target))
                return;
            if (WarpHandler.decayMappings.containsKey(pair) || pair.getBlock() instanceof IPlantable || pair.getBlock().getMaterial() == Material.leaves)
            {
                NameMetaPair decayed = WarpHandler.decayMappings.get(pair);
                if (decayed == null)
                    decayed = new NameMetaPair(Blocks.air, 0);
                if (world.setBlock(target.x, target.y, target.z, decayed.getBlock(), decayed.getMetadata(), 3))
                {
                    if (target.isAir(world))
                        world.playAuxSFXAtEntity(null, 2001, target.x, target.y, target.z, Block.getIdFromBlock(pair.getBlock()) + (pair.getMetadata() << 12));
                    MiscHelper.subFromTag(player, "biomeDecay", 1);
                    if (decay <= 0)
                    {
                        MiscHelper.removeTag(player, "biomeDecay");
                        ChatHelper.sendToPlayer(Minecraft.getMinecraft().thePlayer, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.decayend"));
                    }
                }
            }
        }
    }
}
