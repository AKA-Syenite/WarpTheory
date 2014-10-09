package shukaro.warptheory;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;
import shukaro.warptheory.gui.WarpTab;
import shukaro.warptheory.handlers.WarpEventHandler;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.handlers.WarpTickHandler;
import shukaro.warptheory.items.WarpItems;
import shukaro.warptheory.net.CommonProxy;
import shukaro.warptheory.recipe.WarpRecipes;
import shukaro.warptheory.research.WarpResearch;
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
    public void preInit(FMLPreInitializationEvent evt)
    {
        logger = evt.getModLog();
        try
        {
            ResourceLocation normalNameResource = new ResourceLocation(modID, "normalNames");
            logger.info(normalNameResource);
            normalNames = new NameGenerator("assets/warptheory/lang/normalNames_en_US.txt");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        WarpItems.initItems();
        MinecraftForge.EVENT_BUS.register(new WarpEventHandler());
        FMLCommonHandler.instance().bus().register(new WarpTickHandler());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent evt)
    {
        WarpRecipes.initRecipes();
        WarpHandler.initEvents();
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent evt)
    {
        WarpHandler.tcReflect();
        WarpResearch.initResearch();
    }
}
