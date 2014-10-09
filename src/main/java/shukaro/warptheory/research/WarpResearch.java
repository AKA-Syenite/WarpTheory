package shukaro.warptheory.research;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import shukaro.warptheory.items.WarpItems;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

public class WarpResearch
{
    public static ResearchItem researchCleanser;
    public static ResearchItem researchAmulet;

    public static void initResearch()
    {
        researchCleanser = new ResearchItem("WARPCLEANSER", "ELDRITCH", new AspectList().add(Aspect.ELDRITCH, 6).add(Aspect.EXCHANGE, 3), -2, 2, 2, new ItemStack(WarpItems.itemCleanser))
                .setPages(new ResearchPage[]{
                        new ResearchPage(StatCollector.translateToLocal("warptheory.warpcleanser.research"))
                }).setParents("ELDRITCHMAJOR");
        researchCleanser.registerResearchItem();

        researchAmulet = new ResearchItem("WARPAMULET", "ELDRITCH", new AspectList().add(Aspect.ELDRITCH, 6).add(Aspect.EXCHANGE, 3).add(Aspect.MAGIC, 3), -3, 4, 3, new ItemStack(WarpItems.itemAmulet))
                .setPages(new ResearchPage[]{
                        new ResearchPage(StatCollector.translateToLocal("warptheory.warpamulet.research"))
                }).setParents("WARPCLEANSER");
        researchAmulet.registerResearchItem();
    }
}
