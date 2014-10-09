package shukaro.warptheory.items;

import cpw.mods.fml.common.registry.GameRegistry;

public class WarpItems
{
    public static ItemCleanser itemCleanser;
    public static ItemActivator itemActivator;

    public static void initItems()
    {
        itemCleanser = new ItemCleanser();
        GameRegistry.registerItem(itemCleanser, itemCleanser.getUnlocalizedName());

        itemActivator = new ItemActivator();
        GameRegistry.registerItem(itemActivator, itemActivator.getUnlocalizedName());
    }
}
