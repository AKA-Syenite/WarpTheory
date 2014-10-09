package shukaro.warptheory.handlers.warpevents;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.handlers.WarpTickHandler;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

public class WarpBats implements IWarpEvent
{
    public WarpBats() {
        WarpTickHandler.register(this);
    }
    @Override
    public String getName()
    {
        return "bats";
    }

    @Override
    public int getCost()
    {
        return 10;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.batsstart"));

        //Set tag on player inside the WarpTheory compound tag.
        //Creates compound if it does not exist.
        //Creates tag if it does not exist and sets value.
        //If tag exists, value is added.
        MiscHelper.addToTag(player, "bats", 15 + world.rand.nextInt(30));
        return true;
    }

    //Called from WarpTickHandler every tick this effect is live.
    //Do actual effect here.
    //World is world the event fired in.
    //Player is the player it's affecting.
    public void onTick(World world, EntityPlayer player) {

        //Spawning bats
        if (MiscHelper.getTag(player, "bats") > 0) {
            int bats = MiscHelper.getTag(player, "bats");

            for (int i=0; i<6; i++)
            {
                int targetX = (int)player.posX + world.rand.nextInt(8) - world.rand.nextInt(8);
                int targetY = (int)player.posY + world.rand.nextInt(8) - world.rand.nextInt(8);
                int targetZ = (int)player.posZ + world.rand.nextInt(8) - world.rand.nextInt(8);
                if (world.isAirBlock(targetX, targetY, targetZ))
                {
                    EntityBat bat = new EntityBat(world);
                    bat.playLivingSound();
                    bat.setLocationAndAngles((double)targetX + world.rand.nextDouble(), (double)targetY + world.rand.nextDouble(), (double)targetZ + world.rand.nextDouble(), world.rand.nextFloat(), world.rand.nextFloat());
                    if (world.spawnEntityInWorld(bat))
                    {
                        MiscHelper.subFromTag(player, "bats", 1);
                        if (bats <= 0)
                        {
                            MiscHelper.removeTag(player, "bats");
                            ChatHelper.sendToPlayer(Minecraft.getMinecraft().thePlayer, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.batsend"));
                        }
                        break;
                    }
                }
            }
        }

        /*
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
        */

    }
}
