package shukaro.warptheory.handlers;

import gnu.trove.map.hash.THashMap;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.warpevents.*;
import shukaro.warptheory.util.NameMetaPair;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class WarpHandler
{
    public static Map<String, Integer> warp;
    public static Map<String, Integer> warpTemp;
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
        // add WarpBuffs
        warpEvents.add(new WarpDecay());
        warpEvents.add(new WarpEars());
        warpEvents.add(new WarpSwamp());
        warpEvents.add(new WarpTongue());

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
            Class proxyClass = proxy.getClass();
            warp = (Map<String, Integer>)proxyClass.getField("warp").get(proxy);
            warpTemp = (Map<String, Integer>)proxyClass.getField("warpTemp").get(proxy);
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

    public static void removeAllWarp(World world, EntityPlayer player)
    {
        removeWarp(world, player, getWarp(player));
    }

    public static void removeWarp(World world, EntityPlayer player, int amount)
    {
        if (amount == 0)
            return;
        if ((warp != null && warpTemp != null) || tcReflect())
        {
            String name = player.getDisplayName();
            int w = warp.get(name);
            int wt = warpTemp.get(name);
            if (amount <= wt)
                warpTemp.put(name, wt - amount);
            else
            {
                warpTemp.put(name, 0);
                if (w - (amount - wt) >= 0)
                    warp.put(name, w - (amount - wt));
                else
                    warp.put(name, 0);
            }
            doWarp(world, player, amount);
        }
    }

    public static int getWarp(EntityPlayer player)
    {
        if ((warp != null && warpTemp != null) || tcReflect())
            return warp.get(player.getDisplayName()) + warpTemp.get(player.getDisplayName());
        return 0;
    }

    public static void doWarp(World world, EntityPlayer player, int amount)
    {
        int w = amount;
        while (w > 0)
        {
            IWarpEvent event = warpEvents.get(world.rand.nextInt(warpEvents.size()));
            while (event.getCost() > w)
                event = warpEvents.get(world.rand.nextInt(warpEvents.size()));
            if (event.doEvent(world, player))
                w -= event.getCost();
        }
    }

    public static void doOneWarp(World world, EntityPlayer player, int maxAmount)
    {
        boolean repeat = true;
        IWarpEvent event = warpEvents.get(world.rand.nextInt(warpEvents.size()));
        while (repeat)
        {
            while (event.getCost() > maxAmount)
                event = warpEvents.get(world.rand.nextInt(warpEvents.size()));
            repeat = !event.doEvent(world, player);
        }
    }

    public static boolean canDoBiomeEvent(EntityPlayer player, String biomeEvent)
    {
        NBTTagCompound tag = player.getEntityData().getCompoundTag(WarpTheory.modID);
        String currentBiome = "";
        for (String key : (Set<String>)tag.func_150296_c())
        {
            if (key.contains("biome"))
                currentBiome = key;
        }
        if (currentBiome.length() == 0)
            return true;
        if (!currentBiome.equals(biomeEvent))
            return false;
        return true;
    }
}
