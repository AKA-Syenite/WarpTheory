package shukaro.warptheory.handlers.warpevents;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
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

public class WarpBlink implements IWarpEvent
{
    public WarpBlink() { FMLCommonHandler.instance().bus().register(this); }

    @Override
    public String getName()
    {
        return "blink";
    }

    @Override
    public int getCost()
    {
        return 10;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.blink"));
        MiscHelper.modTag(player, "blink", 10 + world.rand.nextInt(20));
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
            return;
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (MiscHelper.getTag(player, "blink") > 0 && e.world.getTotalWorldTime() % 20 == 0)
            {
                int blink = MiscHelper.getTag(player, "blink");
                for (int i = 0; i < 8; i++)
                {
                    int targetX = (int)player.posX + e.world.rand.nextInt(16) - e.world.rand.nextInt(16);
                    int targetY = (int)player.posY + e.world.rand.nextInt(16) - e.world.rand.nextInt(16);
                    int targetZ = (int)player.posZ + e.world.rand.nextInt(16) - e.world.rand.nextInt(16);
                    BlockCoord target = new BlockCoord(targetX, targetY, targetZ);
                    if (target.isAir(e.world) && target.copy().offset(1).isAir(e.world) && !target.copy().offset(0).isAir(e.world))
                    {
                        player.rotationPitch = (e.world.rand.nextInt(90) + e.world.rand.nextFloat()) - (e.world.rand.nextInt(90) + e.world.rand.nextFloat());
                        player.rotationYaw = (e.world.rand.nextInt(360) + e.world.rand.nextFloat()) - (e.world.rand.nextInt(360) + e.world.rand.nextFloat());
                        double dX = target.x + e.world.rand.nextDouble();
                        double dY = target.y + e.world.rand.nextDouble();
                        double dZ = target.z + e.world.rand.nextDouble();
                        player.setPositionAndUpdate(dX, dY, dZ);
                        PacketDispatcher.sendBlinkEvent(e.world, dX, dY, dZ);
                        e.world.playSoundEffect(dX, dY, dZ, "mob.endermen.portal", 1.0F, 1.0F);
                        MiscHelper.setTag(player, "blink", --blink);
                        if (blink <= 0)
                            MiscHelper.removeTag(player, "blink");
                        break;
                    }
                }
            }
        }
    }
}
