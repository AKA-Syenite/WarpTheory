package shukaro.warptheory.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
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
        for (EntityPlayer serverPlayer : (ArrayList<EntityPlayer>)MinecraftServer.getServer().getConfigurationManager().playerEntityList)
        {
            if (serverPlayer.getEntityId() == id)
                return serverPlayer;
        }
        return null;
    }

    public static EntityPlayer getPlayerByName(String name)
    {
        for (EntityPlayer serverPlayer : (ArrayList<EntityPlayer>)MinecraftServer.getServer().getConfigurationManager().playerEntityList)
        {
            if (serverPlayer.getCommandSenderName().equals(name))
                return serverPlayer;
        }
        return null;
    }

    public static boolean isOp(String name)
    {
        for (String n : MinecraftServer.getServer().getConfigurationManager().func_152606_n())
        {
            if (n.equals(name))
                return true;
        }
        return false;
    }

    public static boolean hasTag(EntityPlayer player, String tagName)
    {
        if (!player.getEntityData().hasKey(WarpTheory.modID))
        {
            player.getEntityData().setTag(WarpTheory.modID, new NBTTagCompound());
            return false;
        }
        return player.getEntityData().getCompoundTag(WarpTheory.modID).hasKey(tagName);
    }

    public static int getTag(EntityPlayer player, String tagName)
    {
        if (!(hasTag(player, tagName))) { return 0; }
        return player.getEntityData().getCompoundTag(WarpTheory.modID).getInteger(tagName);
    }

    public static void setTag(EntityPlayer player, String tagName, int value)
    {
        if (!player.getEntityData().hasKey(WarpTheory.modID))
            player.getEntityData().setTag(WarpTheory.modID, new NBTTagCompound());
        player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger(tagName, value);
    }

    public static void modTag(EntityPlayer player, String tagName, int value)
    {
        setTag(player, tagName, getTag(player, tagName) + value);
    }

    public static void removeTag(EntityPlayer player, String tagName)
    {
        if (hasTag(player, tagName))
            player.getEntityData().getCompoundTag(WarpTheory.modID).removeTag(tagName);
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
        for (int i = 0; i < 6; i++)
        {
            if (i != 1 && (!coord.copy().offset(i).getBlock(world).isOpaqueCube() && coord.copy().offset(i).getBlock(world) != Blocks.water))
                contained = false;
        }
        BlockCoord cover = coord.copy().offset(1);
        return contained && (coord.isAir(world) || WarpHandler.decayMappings.containsKey(pair)) && (cover.isAir(world) || cover.getBlock(world) == Blocks.log || cover.getBlock(world) == Blocks.log2 || cover.getBlock(world) instanceof IPlantable);
    }

    public static ArrayList<IInventory> getNearbyTileInventories(EntityPlayer player, int range)
    {
        ArrayList<IInventory> nearby = new ArrayList<IInventory>();
        for (TileEntity te : (ArrayList<TileEntity>)player.worldObj.loadedTileEntityList)
        {
            BlockCoord teCoord = new BlockCoord(te.xCoord, te.yCoord, te.zCoord);
            BlockCoord playerCoord = new BlockCoord((int)player.posX, (int)player.posY, (int)player.posZ);
            if (te instanceof IInventory && teCoord.getDistance(playerCoord) <= range)
                nearby.add((IInventory)te);
        }
        return nearby;
    }
}
