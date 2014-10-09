package shukaro.warptheory.handlers.warpevents;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.net.packets.PacketDispatcher;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

public class WarpBlink implements IWarpEvent
{
    @Override
    public String getName()
    {
        return "blink";
    }

    @Override
    public int getCost()
    {
        return 5;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.blinkstart"));
        MiscHelper.addToTag(player, "blink", 10 + world.rand.nextInt(20));
        return true;
    }

    public static void onTick(World world, EntityPlayer player) {

        if (MiscHelper.getTag(player, "blink") > 0 && world.getTotalWorldTime() % 20 == 0) {
            int blink = MiscHelper.getTag(player, "blink");

            for (int i=0; i<8; i++)
            {
                int targetX = (int)player.posX + world.rand.nextInt(16) - world.rand.nextInt(16);
                int targetY = (int)player.posY + world.rand.nextInt(16) - world.rand.nextInt(16);
                int targetZ = (int)player.posZ + world.rand.nextInt(16) - world.rand.nextInt(16);
                BlockCoord target = new BlockCoord(targetX, targetY, targetZ);
                if (target.isAir(world) && target.copy().offset(1).isAir(world) && !target.copy().offset(0).isAir(world))
                {
                    player.rotationPitch = (world.rand.nextInt(90) + world.rand.nextFloat()) - (world.rand.nextInt(90) + world.rand.nextFloat());
                    player.rotationYaw = (world.rand.nextInt(360) + world.rand.nextFloat()) - (world.rand.nextInt(360) + world.rand.nextFloat());
                    double dX = target.x + world.rand.nextDouble();
                    double dY = target.y + world.rand.nextDouble();
                    double dZ = target.z + world.rand.nextDouble();
                    player.setPositionAndUpdate(dX, dY, dZ);
                    PacketDispatcher.sendBlinkEvent(world, dX, dY, dZ);
                    world.playSoundEffect(dX, dY, dZ, "mob.endermen.portal", 1.0F, 1.0F);
                    MiscHelper.subFromTag(player, "blink", 1);
                    if (blink <= 0)
                    {
                        MiscHelper.removeTag(player, "blink");
                        ChatHelper.sendToPlayer(Minecraft.getMinecraft().thePlayer, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.blinkend"));
                    }
                    break;
                }
            }
        }
    }
}
