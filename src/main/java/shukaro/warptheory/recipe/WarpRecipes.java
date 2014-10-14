package shukaro.warptheory.recipe;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import shukaro.warptheory.items.WarpItems;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;

public class WarpRecipes
{
    private static ArrayList<ItemStack> meats = new ArrayList<ItemStack>();

    private static void initMeats()
    {
        meats.add(new ItemStack(Items.chicken));
        meats.add(new ItemStack(Items.beef));
        meats.add(new ItemStack(Items.porkchop));
        meats.add(new ItemStack(Items.fish));
    }

    public static void initRecipes()
    {
        initMeats();
        ThaumcraftApi.addInfusionCraftingRecipe("WARPCLEANSER", new ItemStack(WarpItems.itemCleanser), 5, new AspectList().add(Aspect.ELDRITCH, 15).add(Aspect.EXCHANGE, 15), new ItemStack(Items.ghast_tear),
                new ItemStack[]{ItemApi.getItem("itemResource", 14), ItemApi.getItem("itemResource", 14), new ItemStack(Items.quartz), new ItemStack(Items.quartz)});
        ThaumcraftApi.addInfusionCraftingRecipe("WARPAMULET", new ItemStack(WarpItems.itemAmulet), 10, new AspectList().add(Aspect.ELDRITCH, 15).add(Aspect.EXCHANGE, 15).add(Aspect.MAGIC, 30), new ItemStack(WarpItems.itemCleanser),
                new ItemStack[]{ItemApi.getItem("itemResource", 2), new ItemStack(Items.gold_ingot), ItemApi.getItem("itemResource", 2), new ItemStack(Items.gold_ingot), new ItemStack(Items.diamond), new ItemStack(Items.diamond), new ItemStack(Items.diamond), new ItemStack(Items.diamond)});
        for (ItemStack meat : meats)
            ThaumcraftApi.addCrucibleRecipe("WARPSOMETHING", new ItemStack(WarpItems.itemSomething), meat, new AspectList().add(Aspect.ELDRITCH, 8));
    }
}
