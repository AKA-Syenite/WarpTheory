package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;

import java.util.Collection;

public class WarpBuff extends IWarpEvent
{
    private String name;
    private int severity;
    private int id;
    private int duration;
    private int level;

    public WarpBuff(String name, int severity, PotionEffect effect)
    {
        this.name = name;
        this.severity = severity;
        this.id = effect.getPotionID();
        this.duration = effect.getDuration();
        this.level = effect.getAmplifier();
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public int getSeverity()
    {
        return severity;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        if (world.isRemote)
            return true;
        PotionEffect effect = null;
        if (player.isPotionActive(id))
        {
            for (PotionEffect e : (Collection<PotionEffect>)player.getActivePotionEffects())
            {
                if (e.getPotionID() == id)
                {
                    effect = new PotionEffect(id, duration + e.getDuration(), level);
                    break;
                }
            }
        }
        else
            effect = new PotionEffect(id, duration, level);
        if (effect != null)
        {
            effect.getCurativeItems().clear();
            player.addPotionEffect(effect);
            ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory." + getName()));
        }
        return true;
    }
}
