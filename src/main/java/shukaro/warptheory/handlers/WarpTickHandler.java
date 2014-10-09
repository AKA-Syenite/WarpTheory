package shukaro.warptheory.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.Facing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.net.packets.PacketDispatcher;
import shukaro.warptheory.util.*;

import java.util.ArrayList;

public class WarpTickHandler
{
    @SubscribeEvent
    public void worldTick(TickEvent.WorldTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END)
            return;
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (!player.getEntityData().hasKey(WarpTheory.modID))
                return;

            // Spawning bats
            if (player.getEntityData().getCompoundTag(WarpTheory.modID).getInteger("bats") > 0)
            {
                int bats = player.getEntityData().getCompoundTag(WarpTheory.modID).getInteger("bats");
                for (int i=0; i<6; i++)
                {
                    int targetX = (int)player.posX + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                    int targetY = (int)player.posY + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                    int targetZ = (int)player.posZ + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                    if (e.world.isAirBlock(targetX, targetY, targetZ))
                    {
                        EntityBat bat = new EntityBat(e.world);
                        bat.playLivingSound();
                        bat.setLocationAndAngles((double)targetX + e.world.rand.nextDouble(), (double)targetY + e.world.rand.nextDouble(), (double)targetZ + e.world.rand.nextDouble(), e.world.rand.nextFloat(), e.world.rand.nextFloat());
                        if (e.world.spawnEntityInWorld(bat))
                        {
                            player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger("bats", --bats);
                            if (bats <= 0)
                            {
                                player.getEntityData().getCompoundTag(WarpTheory.modID).removeTag("bats");
                                ChatHelper.sendToPlayer(Minecraft.getMinecraft().thePlayer, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.batsend"));
                            }
                            break;
                        }
                    }
                }
            }

            // Decaying terrain
            if (player.getEntityData().getCompoundTag(WarpTheory.modID).getInteger("biomeDecay") > 0)
            {
                int decay = player.getEntityData().getCompoundTag(WarpTheory.modID).getInteger("biomeDecay");
                int targetX = (int)player.posX + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                int targetY = (int)player.posY + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                int targetZ = (int)player.posZ + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                BlockCoord target = new BlockCoord(targetX, targetY, targetZ);
                NameMetaPair pair = new NameMetaPair(target.getBlock(e.world), target.getMeta(e.world));
                if (!MiscHelper.hasNonSolidNeighbor(e.world, target))
                    break;
                if (WarpHandler.decayMappings.containsKey(pair) || pair.getBlock() instanceof IPlantable || pair.getBlock().getMaterial() == Material.leaves)
                {
                    NameMetaPair decayed = WarpHandler.decayMappings.get(pair);
                    if (decayed == null)
                        decayed = new NameMetaPair(Blocks.air, 0);
                    if (e.world.setBlock(target.x, target.y, target.z, decayed.getBlock(), decayed.getMetadata(), 3))
                    {
                        if (target.isAir(e.world))
                            e.world.playAuxSFXAtEntity(null, 2001, target.x, target.y, target.z, Block.getIdFromBlock(pair.getBlock()) + (pair.getMetadata() << 12));
                        player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger("biomeDecay", --decay);
                        if (decay <= 0)
                        {
                            player.getEntityData().getCompoundTag(WarpTheory.modID).removeTag("biomeDecay");
                            ChatHelper.sendToPlayer(Minecraft.getMinecraft().thePlayer, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.decayend"));
                        }
                    }
                }
            }

            /*// Growing swamp
            if (player.getEntityData().getCompoundTag(WarpTheory.modID).getInteger("biomeSwamp") > 0)
            {
                int biomeSwamp = player.getEntityData().getCompoundTag(WarpTheory.modID).getInteger("biomeSwamp");
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
                    for (int j=0; j<6; j++)
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
                    player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger("biomeSwamp", --biomeSwamp);
                    if (biomeSwamp <= 0)
                    {
                        player.getEntityData().getCompoundTag(WarpTheory.modID).removeTag("biomeSwamp");
                        ChatHelper.sendToPlayer(Minecraft.getMinecraft().thePlayer, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.swampend"));
                    }
                }
            }*/

            // Blinking
            if (player.getEntityData().getCompoundTag(WarpTheory.modID).getInteger("blink") > 0 && e.world.getTotalWorldTime() % 20 == 0)
            {
                int blink = player.getEntityData().getCompoundTag(WarpTheory.modID).getInteger("blink");
                for (int i=0; i<8; i++)
                {
                    int targetX = (int)player.posX + e.world.rand.nextInt(16) - e.world.rand.nextInt(16);
                    int targetY = (int)player.posY + e.world.rand.nextInt(16) - e.world.rand.nextInt(16);
                    int targetZ = (int)player.posZ + e.world.rand.nextInt(16) - e.world.rand.nextInt(16);
                    BlockCoord target = new BlockCoord(targetX, targetY, targetZ);
                    if (target.isAir(e.world) && target.copy().offset(1).isAir(e.world) && !target.copy().offset(0).isAir(e.world))
                    {
                        player.rotationPitch = (e.world.rand.nextInt(90) + e.world.rand.nextFloat()) - (e.world.rand.nextInt(90) + e.world.rand.nextFloat());
                        player.rotationYaw = (e.world.rand.nextInt(360) + e.world.rand.nextFloat()) - (e.world.rand.nextInt(360) + e.world.rand.nextFloat());
                        double dX = target.x + e.world.rand.nextDouble();
                        double dY = target.y + e.world.rand.nextDouble();
                        double dZ = target.z + e.world.rand.nextDouble();
                        player.setPositionAndUpdate(dX, dY, dZ);
                        PacketDispatcher.sendBlinkEvent(e.world, dX, dY, dZ);
                        e.world.playSoundEffect(dX, dY, dZ, "mob.endermen.portal", 1.0F, 1.0F);
                        player.getEntityData().getCompoundTag(WarpTheory.modID).setInteger("blink", --blink);
                        if (blink <= 0)
                        {
                            player.getEntityData().getCompoundTag(WarpTheory.modID).removeTag("blink");
                            ChatHelper.sendToPlayer(Minecraft.getMinecraft().thePlayer, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.blinkend"));
                        }
                        break;
                    }
                }
            }


        }
    }
}
