package shukaro.warptheory.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import shukaro.warptheory.handlers.WarpHandler;

import java.util.ArrayList;

public class MiscHelper
{
    public static EntityPlayer getServerSidePlayer()
    {
        int id = Minecraft.getMinecraft().thePlayer.getEntityId();
        for (EntityPlayer serverPlayer : (ArrayList<EntityPlayer>) MinecraftServer.getServer().getConfigurationManager().playerEntityList)
        {
            if (serverPlayer.getEntityId() == id)
                return serverPlayer;
        }
        return null;
    }

    public static boolean hasNonSolidNeighbor(World world, BlockCoord coord)
    {
        for (BlockCoord n : coord.getNearby())
        {
            if (n.isAir(world) || !n.getBlock(world).isOpaqueCube())
                return true;
        }
        return false;
    }

    public static boolean canTurnToSwampWater(World world, BlockCoord coord)
    {
        NameMetaPair pair = new NameMetaPair(coord.getBlock(world), coord.getMeta(world));
        boolean contained = true;
        for (int i=0; i<6; i++)
        {
            if (i != 1 && (!coord.copy().offset(i).getBlock(world).isOpaqueCube() && coord.copy().offset(i).getBlock(world) != Blocks.water))
                contained = false;
        }
        BlockCoord cover = coord.copy().offset(1);
        return contained && (coord.isAir(world) || WarpHandler.decayMappings.containsKey(pair)) && (cover.isAir(world) || cover.getBlock(world) == Blocks.log || cover.getBlock(world) == Blocks.log2 || cover.getBlock(world) instanceof IPlantable);
    }
}
