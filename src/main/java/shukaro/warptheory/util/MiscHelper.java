package shukaro.warptheory.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import shukaro.warptheory.WarpTheory;
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

    public static boolean hasTag(EntityPlayer player, String tagName) {
        if (!player.getEntityData().hasKey(WarpTheory.modID)) {
            player.getEntityData().setTag(WarpTheory.modID, new NBTTagCompound());
            return false; //Can't have tag, didn't even have the compound.
        }
        return player.getEntityData().getCompoundTag(WarpTheory.modID).hasKey(tagName);
    }
    public static int getTag(EntityPlayer player, String tagName) {
        if (!(hasTag(player, tagName))) { return 0; } //Tag DNE
        return player.getEntityData().getCompoundTag(WarpTheory.modID).getInteger(tagName);
    }
    public static void setTag(EntityPlayer player, String tagName, int value) {
        //Right now hasTag enforces existence of the mod compound tag.
        //So this hasTag check is needed even though both sides of the if-else are the same.
        //TODO: re-write to not be stupid like this.
        if (!(hasTag(player, tagName))) {
            //Tag DNE, create and set value
            player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger(tagName, value);
        } else {
            //Tag exists, overwrite
            player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger(tagName, value);
        }
    }
    public static void addToTag(EntityPlayer player, String tagName, int value) {
        //max is to handle possibly negative tag values.
        //clamp to zero.
        setTag(player, tagName, Math.max(0,getTag(player, tagName) + value));
    }
    public static void subFromTag(EntityPlayer player, String tagName, int value) {
        addToTag(player, tagName, -value);
    }
    public static void removeTag(EntityPlayer player, String tagName) {
        if (hasTag(player, tagName)) {
            player.getEntityData().getCompoundTag(WarpTheory.modID).removeTag(tagName);
        }
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
