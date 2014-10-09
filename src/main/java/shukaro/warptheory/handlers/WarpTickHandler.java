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
import shukaro.warptheory.handlers.warpevents.*; //all warp events
import shukaro.warptheory.net.packets.PacketDispatcher;
import shukaro.warptheory.util.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

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

            //WarpTickHandler will act as a dispatch to the actual warp events.
            //TODO: add configuration options or something to enable/disable individual events.
            //Should probably replace this with some kind of map of callbacks or something.
            //Each effect registers a tagname and callback func, iterate through that instead.
            //TODO: tagname / callback structure
            Set<String> keyset =  player.getEntityData().getCompoundTag(WarpTheory.modID).func_150296_c();
            for (String key : keyset) { //for-each entry in the tag compound...
                switch(key) {
                    case "bats": WarpBats.onTick(e.world, player); break; //call static method of the effect w/ player as arg
                    case "blink": WarpBlink.onTick(e.world, player); break;
                    case "biomeDecay": WarpDecay.onTick(e.world, player); break;
                    case "biomeSwamp": WarpSwamp.onTick(e.world, player); break;
                    case "foo": break; //do foo
                    case "bar": break; //do bar

                    default:
                        //Unimplemented tag key.
                        //TODO: log warning here
                        break;
                }
            }
        }
    }
}
