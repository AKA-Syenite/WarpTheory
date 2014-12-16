package shukaro.warptheory;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
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
import shukaro.warptheory.util.NameGenerator;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

@Mod(modid = WarpTheory.modID, name = WarpTheory.modName, version = WarpTheory.modVersion,
        dependencies = "required-after:Thaumcraft")
public class WarpTheory
{
    @SidedProxy(clientSide = "shukaro.warptheory.net.ClientProxy", serverSide = "shukaro.warptheory.net.CommonProxy")
    public static CommonProxy proxy;

    public static final String modID = "WarpTheory";
    public static final String modName = "WarpTheory";
    public static final String modVersion = "1.7.10R1.0";

    public static boolean wussMode = false;
    public static int permWarpMult = 2;
    public static boolean allowPermWarpRemoval = true;
    public static boolean allowGlobalWarpEffects = false;
    
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
            File folder = new File(evt.getModConfigurationDirectory() + "/warptheory/");
            if (!folder.exists())
                folder.mkdirs();
            File normalFile = new File(evt.getModConfigurationDirectory() + "/warptheory/normal.txt");
            if (!normalFile.exists())
            {
                InputStream in = WarpTheory.class.getResourceAsStream("/assets/warptheory/names/normal.txt");
                Files.copy(in, normalFile.getAbsoluteFile().toPath());
            }
            normalNames = new NameGenerator(normalFile.getAbsolutePath());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        WarpBlocks.initBlocks();
        WarpItems.initItems();
        MinecraftForge.EVENT_BUS.register(new WarpEventHandler());
        Configuration c = new Configuration(evt.getSuggestedConfigurationFile());
        c.load();
        wussMode = c.getBoolean("wussMode", "general", false, "enables less expensive recipes");
        permWarpMult = c.getInt("permWarpMult", "general", 2, 0, Integer.MAX_VALUE, "how much more 'expensive' permanent warp is compared to normal warp");
        allowPermWarpRemoval = c.getBoolean("allowPermWarpRemoval", "general", true, "whether items can remove permanent warp or not");
        allowGlobalWarpEffects = c.getBoolean("allowGlobalWarpEffects", "general", true, "whether warp effects that envolve the environment are triggered");
        c.save();
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
