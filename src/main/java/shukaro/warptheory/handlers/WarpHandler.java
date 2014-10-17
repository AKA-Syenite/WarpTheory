package shukaro.warptheory.handlers;

import baubles.api.BaublesApi;
import gnu.trove.map.hash.THashMap;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.warpevents.*;
import shukaro.warptheory.util.NameMetaPair;
import thaumcraft.api.IWarpingGear;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class WarpHandler
{
    public static Map<String, Integer> warpNormal;
    public static Map<String, Integer> warpTemp;
    public static Map<String, Integer> warpPermanent;
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
        warpEvents.add(new WarpBuff("poison", 16, new PotionEffect(Potion.poison.id, 20 * 20)));
        warpEvents.add(new WarpBuff("nausea", 25, new PotionEffect(Potion.confusion.id, 20 * 20)));
        warpEvents.add(new WarpBuff("jump", 20, new PotionEffect(Potion.jump.id, 20 * 20, 40)));
        warpEvents.add(new WarpBuff("blind", 28, new PotionEffect(Potion.blindness.id, 20 * 20)));
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
            wuss = Class.forName("thaumcraft.common.config.Config").getField("wuss").getBoolean(null);
            potionWarpWardID = Class.forName("thaumcraft.common.config.Config").getField("potionWarpWardID").getInt(null);
        }
        catch (Exception e)
        {
            WarpTheory.logger.warn("Could not reflect into thaumcraft.common.Config to get config settings");
            e.printStackTrace();
        }
        try
        {
            Class tc = Class.forName("thaumcraft.common.Thaumcraft");
            Object proxy = tc.getField("proxy").get(null);
            Object pK = proxy.getClass().getField("playerKnowledge").get(proxy);
            warpNormal = (Map<String, Integer>)pK.getClass().getDeclaredField("warpSticky").get(pK);
            warpTemp = (Map<String, Integer>)pK.getClass().getField("warpTemp").get(pK);
            warpPermanent = (Map<String, Integer>)pK.getClass().getField("warp").get(pK);
        }
        catch (Exception e)
        {
            WarpTheory.logger.warn("Could not reflect into thaumcraft.common.Thaumcraft to get warpNormal mappings, attempting older reflection");
            e.printStackTrace();
            try
            {
                Class tc = Class.forName("thaumcraft.common.Thaumcraft");
                Object proxy = tc.getField("proxy").get(null);
                Object pK = proxy.getClass().getField("playerKnowledge").get(proxy);
                warpNormal = (Map<String, Integer>)pK.getClass().getDeclaredField("warp").get(pK);
                warpTemp = (Map<String, Integer>)pK.getClass().getField("warpTemp").get(pK);
            }
            catch (Exception x)
            {
                WarpTheory.logger.warn("Failed to reflect into thaumcraft.common.Thaumcraft to get warpNormal mapping");
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static void purgeWarp(EntityPlayer player)
    {
        doMultipleEvents(player, getTotalWarp(player));
        removeWarp(player, getTotalWarp(player));
    }

    public static void removeWarp(EntityPlayer player, int amount)
    {
        if (amount <= 0)
            return;
        if ((warpNormal != null && warpTemp != null) || tcReflect())
        {
            String name = player.getDisplayName();
            int wp = warpPermanent != null ? warpPermanent.get(name) : 0;
            int wn = warpNormal.get(name);
            int wt = warpTemp.get(name);
            if (amount <= wt)
            {
                warpTemp.put(name, wt - amount);
                return;
            }
            else
            {
                warpTemp.put(name, 0);
                amount -= wt;
            }
            if (amount <= wn)
            {
                warpNormal.put(name, wn - amount);
                return;
            }
            else
            {
                warpNormal.put(name, 0);
                amount -= wn;
            }
            if ((int)Math.ceil(amount / 2) <= wp)
                warpPermanent.put(name, wp - (int)Math.ceil(amount / 2));
            else
                warpPermanent.put(name, 0);
        }
    }

    public static int getTotalWarp(EntityPlayer player)
    {
        if ((warpNormal != null && warpTemp != null) || tcReflect())
            return (warpPermanent != null ? warpPermanent.get(player.getDisplayName()) : 0) * 2 + warpNormal.get(player.getDisplayName()) + warpTemp.get(player.getDisplayName()) + getWarpFromGear(player);
        return 0;
    }

    public static int[] getIndividualWarps(EntityPlayer player)
    {
        int[] totals = new int[3];
        if ((warpNormal != null && warpTemp != null) || tcReflect())
        {
            totals[0] = warpPermanent != null ? warpPermanent.get(player.getDisplayName()) : 0;
            totals[1] = warpNormal.get(player.getDisplayName());
            totals[2] = warpTemp.get(player.getDisplayName());
        }
        return totals;
    }

    public static int doMultipleEvents(EntityPlayer player, int amount)
    {
        int w = amount;
        while (w > 0)
        {
            IWarpEvent event = doOneEvent(player, w);
            if (event == null)
                return w;
            w -= event.getCost();
        }
        return w;
    }

    public static IWarpEvent doOneEvent(EntityPlayer player, int maxSeverity)
    {
        IWarpEvent event = getAppropriateEvent(maxSeverity);
        if (event != null)
            event.doEvent(player.worldObj, player);
        return event;
    }

    public static IWarpEvent getAppropriateEvent(int maxSeverity)
    {
        ArrayList<IWarpEvent> shuffled = (ArrayList<IWarpEvent>)warpEvents.clone();
        Collections.shuffle(shuffled);
        for (IWarpEvent e : shuffled)
        {
            if (e.getSeverity() <= maxSeverity)
                return e;
        }
        return null;
    }

    public static int getWarpFromGear(EntityPlayer player)
    {
        int w = 0;
        if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof IWarpingGear)
            w += ((IWarpingGear)player.getCurrentEquippedItem().getItem()).getWarp(player.getCurrentEquippedItem(), player);
        IInventory baubles = BaublesApi.getBaubles(player);
        for (int i = 0; i < 4l; i++)
        {
            if (player.inventory.getStackInSlot(i) != null && player.inventory.getStackInSlot(i).getItem() instanceof IWarpingGear)
                w += ((IWarpingGear)player.inventory.getStackInSlot(i).getItem()).getWarp(player.inventory.getStackInSlot(i), player);
            if (baubles != null && baubles.getStackInSlot(i) != null && baubles.getStackInSlot(i).getItem() instanceof IWarpingGear)
                w += ((IWarpingGear)baubles.getStackInSlot(i).getItem()).getWarp(baubles.getStackInSlot(i), player);
        }
        return w;
    }
}
