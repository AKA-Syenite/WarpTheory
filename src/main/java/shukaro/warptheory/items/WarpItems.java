package shukaro.warptheory.items;

import cpw.mods.fml.common.registry.GameRegistry;

public class WarpItems
{
    public static ItemCleanser itemCleanser;
    public static ItemAmulet itemAmulet;

    public static void initItems()
    {
        itemCleanser = new ItemCleanser();
        itemAmulet = new ItemAmulet();
        GameRegistry.registerItem(itemCleanser, itemCleanser.getUnlocalizedName());
        GameRegistry.registerItem(itemAmulet, itemAmulet.getUnlocalizedName());
    }
}
