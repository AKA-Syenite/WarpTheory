package shukaro.warptheory.handlers;

import gnu.trove.map.hash.THashMap;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.warpevents.*;
import shukaro.warptheory.util.NameMetaPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class WarpHandler
{
    public static Map<String, Integer> warp;
    public static Map<String, Integer> warpTemp;
    public static Map<String, Integer> warpSticky;
    public static boolean wuss = false;
    public static int potionWarpWardID = -1;

    public static ArrayList<IWarpEvent> warpEvents = new ArrayList<IWarpEvent>();

    public static Map<NameMetaPair, NameMetaPair> decayMappings = new THashMap<NameMetaPair, NameMetaPair>();

    public static void addDecayMapping(Block start, Block end)
    {
        addDecayMapping(start, 0, end, 0);
    }

    public static void addDecayMapping(Block start, int startMeta, Block end, int endMeta)
    {
        decayMappings.put(new NameMetaPair(start, startMeta), new NameMetaPair(end, endMeta));
    }

    public static void addDecayMapping(Block start, int startMeta, Block end)
    {
        decayMappings.put(new NameMetaPair(start, startMeta), new NameMetaPair(end, 0));
    }

    public static void initEvents()
    {
        warpEvents.add(new WarpBats());
        warpEvents.add(new WarpBlink());
        warpEvents.add(new WarpBuff("poison", 4, new PotionEffect(Potion.poison.id, 20 * 20)));
        warpEvents.add(new WarpBuff("nausea", 8, new PotionEffect(Potion.confusion.id, 20 * 20)));
        warpEvents.add(new WarpBuff("jump", 6, new PotionEffect(Potion.jump.id, 20*20, 40)));
        warpEvents.add(new WarpBuff("blind", 8, new PotionEffect(Potion.blindness.id, 20 * 20)));
        warpEvents.add(new WarpDecay());
        warpEvents.add(new WarpEars());
        warpEvents.add(new WarpSwamp());
        warpEvents.add(new WarpTongue());
        warpEvents.add(new WarpFriend());
        warpEvents.add(new WarpLivestockRain());
        warpEvents.add(new WarpWind());
        warpEvents.add(new WarpChests());
        warpEvents.add(new WarpBlood());
        warpEvents.add(new WarpAcceleration());
        warpEvents.add(new WarpLightning());
        warpEvents.add(new WarpFall());
        warpEvents.add(new WarpRain());
        warpEvents.add(new WarpWither());

        addDecayMapping(Blocks.grass, Blocks.dirt);
        addDecayMapping(Blocks.dirt, 0, Blocks.sand);
        addDecayMapping(Blocks.dirt, 1, Blocks.sand);
        addDecayMapping(Blocks.dirt, 2, Blocks.dirt);
        addDecayMapping(Blocks.stone, Blocks.cobblestone);
        addDecayMapping(Blocks.cobblestone, Blocks.gravel);
        addDecayMapping(Blocks.sandstone, Blocks.sand);
        addDecayMapping(Blocks.gravel, Blocks.sand);
        addDecayMapping(Blocks.sand, Blocks.air);
        addDecayMapping(Blocks.lava, Blocks.cobblestone);
        addDecayMapping(Blocks.flowing_lava, Blocks.cobblestone);
        addDecayMapping(Blocks.water, Blocks.air);
        addDecayMapping(Blocks.snow, Blocks.water);
        addDecayMapping(Blocks.snow_layer, Blocks.air);
        addDecayMapping(Blocks.ice, Blocks.water);
        addDecayMapping(Blocks.clay, Blocks.sand);
        addDecayMapping(Blocks.mycelium, Blocks.grass);
        addDecayMapping(Blocks.stained_hardened_clay, Blocks.hardened_clay);
        addDecayMapping(Blocks.hardened_clay, Blocks.clay);
        addDecayMapping(Blocks.coal_ore, Blocks.stone);
        addDecayMapping(Blocks.diamond_ore, Blocks.stone);
        addDecayMapping(Blocks.emerald_ore, Blocks.stone);
        addDecayMapping(Blocks.gold_ore, Blocks.stone);
        addDecayMapping(Blocks.iron_ore, Blocks.stone);
        addDecayMapping(Blocks.lapis_ore, Blocks.stone);
        addDecayMapping(Blocks.lit_redstone_ore, Blocks.stone);
        addDecayMapping(Blocks.redstone_ore, Blocks.stone);
        addDecayMapping(Blocks.quartz_ore, Blocks.netherrack);
        addDecayMapping(Blocks.netherrack, Blocks.cobblestone);
        addDecayMapping(Blocks.soul_sand, Blocks.sand);
        addDecayMapping(Blocks.glowstone, Blocks.cobblestone);
        addDecayMapping(Blocks.log, Blocks.dirt);
        addDecayMapping(Blocks.log2, Blocks.dirt);
        addDecayMapping(Blocks.brown_mushroom_block, Blocks.dirt);
        addDecayMapping(Blocks.red_mushroom_block, Blocks.dirt);
        addDecayMapping(Blocks.end_stone, Blocks.cobblestone);
        addDecayMapping(Blocks.obsidian, Blocks.cobblestone);
    }

    @SuppressWarnings("unchecked")
    public static boolean tcReflect()
    {
        try
        {
            Class tc = Class.forName("thaumcraft.common.Thaumcraft");
            Object proxy = tc.getField("proxy").get(null);
            Object pK = proxy.getClass().getField("playerKnowledge").get(proxy);
            warp = (Map<String, Integer>)pK.getClass().getDeclaredField("warp").get(pK);
            warpTemp = (Map<String, Integer>)pK.getClass().getField("warpTemp").get(pK);
            warpSticky = (Map<String, Integer>)pK.getClass().getField("warpTemp").get(pK);
            wuss = Class.forName("thaumcraft.common.config.Config").getField("wuss").getBoolean(null);
            potionWarpWardID = Class.forName("thaumcraft.common.config.Config").getField("potionWarpWardID").getInt(null);
        }
        catch (Exception e)
        {
            WarpTheory.logger.warn("Could not reflect into Thaumcraft");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void purgeWarp(EntityPlayer player)
    {
        doWarp(player, getWarp(player));
        removeWarp(player, getWarp(player));
    }

    public static void removeWarp(EntityPlayer player, int amount)
    {
        if (amount <= 0)
            return;
        if ((warp != null && warpTemp != null) || tcReflect())
        {
            String name = player.getDisplayName();
            int ws = warpSticky != null ? warpSticky.get(name) : 0;
            int w = warp.get(name);
            int wt = warpTemp.get(name);
            if (amount <= wt)
                warpTemp.put(name, wt - amount);
            else
            {
                warpTemp.put(name, 0);
                amount -= wt;
            }
            if (amount <= w * 2)
                warp.put(name, w - amount / 2);
            else
            {
                warp.put(name, 0);
                amount -= w * 2;
            }
            if (warpSticky != null && amount <= ws * 4)
                warpSticky.put(name, ws - amount / 4);
            else if (warpSticky != null)
                warpSticky.put(name, 0);
        }
    }

    public static int getWarp(EntityPlayer player)
    {
        if ((warp != null && warpTemp != null) || tcReflect())
            return (warpSticky != null ? warpSticky.get(player.getDisplayName()) : 0) * 4 + warp.get(player.getDisplayName()) * 2 + warpTemp.get(player.getDisplayName());
        return 0;
    }

    public static int[] getWarps(EntityPlayer player)
    {
        int[] totals = new int[3];
        if ((warp != null && warpTemp != null) || tcReflect())
        {
            totals[0] = warpSticky != null ? warpSticky.get(player.getDisplayName()) : 0;
            totals[1] = warp.get(player.getDisplayName());
            totals[2] = warpTemp.get(player.getDisplayName());
        }
        return totals;
    }

    public static int doWarp(EntityPlayer player, int playerWarp)
    {
        int w = playerWarp;
        while (w > 0)
        {
            IWarpEvent event = doOneWarp(player, playerWarp);
            if (event == null)
                return w;
            w -= event.getCost();
        }
        return w;
    }

    public static IWarpEvent doOneWarp(EntityPlayer player, int playerWarp)
    {
        IWarpEvent event = getAppropriateEvent(playerWarp);
        if (event != null)
            event.doEvent(player.worldObj, player);
        return event;
    }

    private static IWarpEvent getAppropriateEvent(int maxCost)
    {
        ArrayList<IWarpEvent> shuffled = (ArrayList<IWarpEvent>)warpEvents.clone();
        Collections.shuffle(shuffled);
        for (IWarpEvent e : shuffled)
        {
            if (e.getCost() <= maxCost)
                return e;
        }
        return null;
    }
}
