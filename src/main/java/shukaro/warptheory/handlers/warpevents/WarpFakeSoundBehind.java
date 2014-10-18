package shukaro.warptheory.handlers.warpevents;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

import java.util.ArrayList;

public class WarpFakeSoundBehind extends IWarpEvent {

    private final String sound;
    private int distance = 16; //approximate distance behind player in blocks to play sound

    public WarpFakeSoundBehind(String sound) {
        this.sound = sound;
        FMLCommonHandler.instance().bus().register(this);
    }
    public WarpFakeSoundBehind(String sound, int distance) {
        this.sound = sound;
        this.distance = distance;
        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public String getName() { return "fakesoundbehind"; }

    @Override
    public int getSeverity() { return 10; }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        //No message
        //ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.fakesoundbehind"));
        MiscHelper.modTag(player, getName(), 1);
        return true;
    }


    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e) {
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
            return;
        for (EntityPlayer player : (ArrayList<EntityPlayer>) e.world.playerEntities) {

            int fakesound = MiscHelper.getTag(player, getName());
            if (fakesound > 0 && e.world.getTotalWorldTime() % 20 == 0) {

                double yaw = player.getRotationYawHead();
                double targetX = player.posX - (distance * Math.sin( Math.toRadians(90 - yaw))) * (Math.sin( Math.toRadians(yaw)));
                double targetZ = player.posZ - (distance * Math.sin( Math.toRadians(90 - yaw))) * (Math.cos( Math.toRadians(yaw)));

                //wtf do last two parameters do?  Documentation appears non-existent.
                //TODO: replace with something not a guess
                e.world.playSoundEffect(targetX, player.posY, targetZ, sound, 1.0F, 1.0F);

                MiscHelper.setTag(player, getName(), --fakesound);
            }
        }
    }
}
