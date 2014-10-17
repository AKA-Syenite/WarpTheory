package shukaro.warptheory.handlers.warpevents;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.net.PacketDispatcher;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WarpBlood implements IWarpEvent
{
    @SideOnly(Side.CLIENT)
    public static Map<Integer, ArrayList<BlockCoord>> bloody = new HashMap<Integer, ArrayList<BlockCoord>>();

    public WarpBlood() { FMLCommonHandler.instance().bus().register(this); }

    @Override
    public String getName()
    {
        return "blood";
    }

    @Override
    public int getCost()
    {
        return 12;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.blood"));
        MiscHelper.modTag(player, "blood", 64 + world.rand.nextInt(128));
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
            return;
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (MiscHelper.getTag(player, "blood") > 0)
            {
                int blood = MiscHelper.getTag(player, "blood");
                for (int i = 0; i < 6; i++)
                {
                    int targetX = (int)player.posX + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                    int targetY = (int)player.posY + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                    int targetZ = (int)player.posZ + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                    if (e.world.isAirBlock(targetX, targetY-1, targetZ) && !e.world.isAirBlock(targetX, targetY, targetZ) && e.world.getBlock(targetX, targetY, targetZ).getMaterial().blocksMovement())
                    {
                        PacketDispatcher.sendBloodEvent(player, targetX, targetY+1, targetZ);
                        MiscHelper.setTag(player, "blood", --blood);
                        if (blood <= 0)
                        {
                            MiscHelper.removeTag(player, "blood");
                            PacketDispatcher.sendBloodClearEvent(player);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END)
            return;
        World world = Minecraft.getMinecraft().theWorld;
        if (world != null && world.getTotalWorldTime() % 5 == 0 && bloody.get(world.provider.dimensionId) != null)
        {
            for (BlockCoord c : bloody.get(world.provider.dimensionId))
                MiscHelper.spawnDripParticle(world, c.x, c.y, c.z, world.rand.nextFloat()+0.2f, 0.0f, 0.0f);
        }
    }
}
