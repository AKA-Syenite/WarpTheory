package shukaro.warptheory.items;

import cpw.mods.fml.common.registry.GameRegistry;

public class WarpItems
{
    public static ItemCleanser itemCleanser;
    public static ItemAmulet itemAmulet;
    public static ItemSomething itemSomething;

    public static void initItems()
    {
        itemCleanser = new ItemCleanser();
        itemAmulet = new ItemAmulet();
        itemSomething = new ItemSomething();
        GameRegistry.registerItem(itemCleanser, itemCleanser.getUnlocalizedName());
        GameRegistry.registerItem(itemAmulet, itemAmulet.getUnlocalizedName());
        GameRegistry.registerItem(itemSomething, itemSomething.getUnlocalizedName());
    }
}
