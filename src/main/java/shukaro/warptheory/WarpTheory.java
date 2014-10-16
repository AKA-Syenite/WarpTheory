package shukaro.warptheory;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;
import shukaro.warptheory.block.WarpBlocks;
import shukaro.warptheory.entity.EntityPassiveCreeper;
import shukaro.warptheory.gui.WarpTab;
import shukaro.warptheory.handlers.WarpCommand;
import shukaro.warptheory.handlers.WarpEventHandler;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.items.WarpItems;
import shukaro.warptheory.net.CommonProxy;
import shukaro.warptheory.recipe.WarpRecipes;
import shukaro.warptheory.research.WarpResearch;
import shukaro.warptheory.tile.TileEntityVanish;
import shukaro.warptheory.util.NameGenerator;

import java.io.IOException;

@Mod(modid = WarpTheory.modID, name = WarpTheory.modName, version = WarpTheory.modVersion,
        dependencies = "")
//required-after:Thaumcraft
public class WarpTheory
{
    @SidedProxy(clientSide = "shukaro.warptheory.net.ClientProxy", serverSide = "shukaro.warptheory.net.CommonProxy")
    public static CommonProxy proxy;

    public static final String modID = "WarpTheory";
    public static final String modName = "WarpTheory";
    public static final String modVersion = "1.7.10R1.0";

    public static Logger logger;

    public static CreativeTabs mainTab = new WarpTab(StatCollector.translateToLocal("warptheory.tab"));

    public static NameGenerator normalNames;

    @Mod.Instance(modID)
    public static WarpTheory instance;

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent evt)
    {
        evt.registerServerCommand(new WarpCommand());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent evt)
    {
        logger = evt.getModLog();
        try
        {
            normalNames = new NameGenerator(WarpTheory.class.getResource("/assets/warptheory/names/normal.txt").getPath());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        WarpBlocks.initBlocks();
        WarpItems.initItems();
        MinecraftForge.EVENT_BUS.register(new WarpEventHandler());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent evt)
    {
        WarpRecipes.initRecipes();
        WarpHandler.initEvents();
        EntityRegistry.registerModEntity(EntityPassiveCreeper.class, "creeperPassive", 0, this, 160, 4, true);
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent evt)
    {
        WarpHandler.tcReflect();
        WarpResearch.initResearch();
    }
}
