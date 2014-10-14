package shukaro.warptheory.research;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import shukaro.warptheory.items.WarpItems;
import thaumcraft.api.ItemApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

public class WarpResearch
{
    public static ResearchItem researchCleanser;
    public static ResearchItem researchAmulet;
    public static ResearchItem researchSomething;

    public static void initResearch()
    {
        researchCleanser = new ResearchItem("WARPCLEANSER", "ELDRITCH", new AspectList().add(Aspect.ELDRITCH, 6).add(Aspect.EXCHANGE, 3), -2, 2, 2, new ItemStack(WarpItems.itemCleanser))
                .setPages(new ResearchPage[]{
                        new ResearchPage(StatCollector.translateToLocal("warptheory.warpcleanser.research")),
                        new ResearchPage(new InfusionRecipe("WARPCLEANSER", new ItemStack(WarpItems.itemCleanser), 5, new AspectList().add(Aspect.ELDRITCH, 15).add(Aspect.EXCHANGE, 15), new ItemStack(Items.ghast_tear),
                                new ItemStack[]{new ItemStack(Items.quartz), ItemApi.getItem("itemResource", 14), new ItemStack(Items.quartz), ItemApi.getItem("itemResource", 14)}))
                }).setParents("ELDRITCHMAJOR");
        researchCleanser.registerResearchItem();

        researchAmulet = new ResearchItem("WARPAMULET", "ELDRITCH", new AspectList().add(Aspect.ELDRITCH, 6).add(Aspect.EXCHANGE, 3).add(Aspect.MAGIC, 3), -3, 4, 3, new ItemStack(WarpItems.itemAmulet))
                .setPages(new ResearchPage[]{
                        new ResearchPage(StatCollector.translateToLocal("warptheory.warpamulet.research")),
                        new ResearchPage(new InfusionRecipe("WARPAMULET", new ItemStack(WarpItems.itemAmulet), 10, new AspectList().add(Aspect.ELDRITCH, 15).add(Aspect.EXCHANGE, 15).add(Aspect.MAGIC, 30), new ItemStack(WarpItems.itemCleanser),
                                new ItemStack[]{ItemApi.getItem("itemResource", 2), new ItemStack(Items.gold_ingot), ItemApi.getItem("itemResource", 2), new ItemStack(Items.gold_ingot), new ItemStack(Items.diamond), new ItemStack(Items.diamond), new ItemStack(Items.diamond), new ItemStack(Items.diamond)}))
                }).setParents("WARPCLEANSER");
        researchAmulet.registerResearchItem();

        researchSomething = new ResearchItem("WARPSOMETHING", "ALCHEMY", new AspectList().add(Aspect.ELDRITCH, 3), -2, -5, 2, new ItemStack(WarpItems.itemSomething))
                .setPages(new ResearchPage[]{
                        new ResearchPage(StatCollector.translateToLocal("warptheory.warpsomething.research")),
                        new ResearchPage(new CrucibleRecipe("WARPSOMETHING", new ItemStack(WarpItems.itemSomething), new ItemStack(Items.beef), new AspectList().add(Aspect.ELDRITCH, 8)))
                }).setHidden().setAspectTriggers(Aspect.ELDRITCH);
        researchSomething.registerResearchItem();
    }
}
