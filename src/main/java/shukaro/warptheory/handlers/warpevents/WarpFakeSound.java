package shukaro.warptheory.handlers.warpevents;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.MiscHelper;

import java.util.ArrayList;

public class WarpFakeSound extends IWarpEvent
{

    private final String name;
    private final String sound;
    private int distance = 16; //radius in blocks about player in which it can occur

    public WarpFakeSound(String name, String sound)
    {
        this.name = name;
        this.sound = sound;
        FMLCommonHandler.instance().bus().register(this);
    }

    public WarpFakeSound(String name, String sound, int distance)
    {
        this.name = name;
        this.sound = sound;
        this.distance = distance;
        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getSeverity() { return 10; }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        //No message. Otherwise kinda spoils the surprise.  Nobody will pay attention if they see "fake explosion happened!" message
        //ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.fakesound"));
        MiscHelper.modEventInt(player, getName(), 1);
        return true;
    }


    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
            return;
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {

            int fakesound = MiscHelper.getWarpTag(player).getInteger(getName());
            if (fakesound > 0 && e.world.getTotalWorldTime() % 20 == 0)
            {

                //(-distance, distance) swing
                // [0, 2*distance] - distance ~= [-distance, distance]  more or less
                int targetX = (int)player.posX + e.world.rand.nextInt(2 * distance) - (distance);
                int targetY = (int)player.posY + e.world.rand.nextInt(2 * distance) - (distance);
                int targetZ = (int)player.posZ + e.world.rand.nextInt(2 * distance) - (distance);

                //wtf do last two parameters do?  Documentation appears non-existent.
                //TODO: replace with something not a guess
                e.world.playSoundEffect(targetX, targetY, targetZ, sound, 1.0F, 1.0F);

                MiscHelper.getWarpTag(player).setInteger(getName(), --fakesound);
            }
        }
    }
}
