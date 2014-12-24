package shukaro.warptheory.handlers.warpevents;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.Facing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.*;

import java.util.ArrayList;
import java.util.Set;

public class WarpSwamp extends IWarpEvent
{
    public WarpSwamp() { FMLCommonHandler.instance().bus().register(this); }

    @Override
    public String getName()
    {
        return "biomeSwamp";
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
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.swamp"));
        MiscHelper.modEventInt(player, "biomeSwamp", 256 + world.rand.nextInt(256));
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
    	if(WarpTheory.allowGlobalWarpEffects == false)
    		return;
    	
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
            return;
        // Growing swamp
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (MiscHelper.getWarpTag(player).hasKey("biomeSwamp"))
            {
                int biomeSwamp = MiscHelper.getWarpTag(player).getInteger("biomeSwamp");
                int targetX = (int)player.posX + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                int targetY = (int)player.posY + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                int targetZ = (int)player.posZ + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                BlockCoord target = new BlockCoord(targetX, targetY, targetZ);
                if (!MiscHelper.hasNonSolidNeighbor(e.world, target))
                    break;
                boolean grown = false;
                if (target.getBlock(e.world) == Blocks.water)
                {
                    if (target.offset(1).isAir(e.world) && e.world.setBlock(target.x, target.y, target.z, Blocks.waterlily, 0, 3))
                        grown = true;
                }
                else if (target.getBlock(e.world) == Blocks.sapling)
                {
                    // Function that grows sapling into a tree
                    ((BlockSapling)target.getBlock(e.world)).func_149878_d(e.world, target.x, target.y, target.z, e.world.rand);
                    grown = true;
                }
                else if (target.getBlock(e.world).getMaterial() == Material.leaves || target.getBlock(e.world) == Blocks.log || target.getBlock(e.world) == Blocks.log2)
                {
                    for (int j = 0; j < 6; j++)
                    {
                        int side = 2 + e.world.rand.nextInt(4);
                        if (Blocks.vine.canPlaceBlockOnSide(e.world, target.x, target.y, target.z, side) && target.offset(side).isAir(e.world))
                        {
                            e.world.setBlock(target.x, target.y, target.z, Blocks.vine, 1 << Direction.facingToDirection[Facing.oppositeSide[side]], 3);
                            grown = true;
                            break;
                        }
                    }
                }
                else
                {
                    if (e.world.rand.nextBoolean() && target.getBlock(e.world).canSustainPlant(e.world, target.x, target.y, target.z, ForgeDirection.UP, (IPlantable)Blocks.sapling))
                    {
                        if (e.world.rand.nextBoolean())
                        {
                            if (target.offset(1).isAir(e.world) || (target.getBlock(e.world) instanceof IPlantable && target.getBlock(e.world) != Blocks.sapling))
                                e.world.setBlock(target.x, target.y, target.z, Blocks.sapling, e.world.rand.nextInt(6), 3);
                        }
                        else if (e.world.rand.nextBoolean())
                        {
                            if (target.offset(1).isAir(e.world) && target.offset(0).getBlock(e.world) instanceof IGrowable)
                                ((IGrowable)target.getBlock(e.world)).func_149853_b(e.world, e.world.rand, target.x, target.y, target.z); // Bonemealing
                        }
                        else
                        {
                            if (target.offset(1).isAir(e.world) && target.offset(0).getBlock(e.world) == Blocks.grass)
                                e.world.setBlock(target.x, target.y, target.z, Blocks.dirt, 2, 3);
                        }
                        grown = true;
                    }
                    else if (e.world.rand.nextBoolean() && MiscHelper.canTurnToSwampWater(e.world, target))
                    {
                        if (target.copy().offset(1).getBlock(e.world) == Blocks.log || target.copy().offset(1).getBlock(e.world) == Blocks.log2)
                            e.world.setBlock(target.x, target.y, target.z, target.copy().offset(1).getBlock(e.world), target.copy().offset(1).getMeta(e.world), 3);
                        else
                            e.world.setBlock(target.x, target.y, target.z, Blocks.water, 0, 3);
                        grown = true;
                    }
                    else if (WarpHandler.decayMappings.containsKey(new NameMetaPair(target.getBlock(e.world), target.getMeta(e.world))) && target.getBlock(e.world).isOpaqueCube() && target.getBlock(e.world) != Blocks.log && target.getBlock(e.world) != Blocks.log2)
                    {
                        if (target.getBlock(e.world) != Blocks.dirt && target.getBlock(e.world) != Blocks.grass)
                        {
                            if (target.copy().offset(1).getBlock(e.world).isOpaqueCube())
                                e.world.setBlock(target.x, target.y, target.z, Blocks.dirt, 0, 3);
                            else if (e.world.rand.nextBoolean())
                                e.world.setBlock(target.x, target.y, target.z, Blocks.grass, 0, 3);
                            else
                                e.world.setBlock(target.x, target.y, target.z, Blocks.dirt, 2, 3);
                            grown = true;
                        }
                    }
                }
                if (grown)
                {
                    MiscHelper.getWarpTag(player).setInteger("biomeSwamp", --biomeSwamp);
                    if (biomeSwamp <= 0)
                        MiscHelper.getWarpTag(player).removeTag("biomeSwamp");
                }
            }
        }
    }
}
