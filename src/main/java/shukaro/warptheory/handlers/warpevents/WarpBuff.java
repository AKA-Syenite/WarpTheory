package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWarpEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class WarpBuff implements IWarpEvent
{
    private String name;
    private int cost;
    private int id;
    private int duration;
    private int level;

    public WarpBuff(String name, int cost, PotionEffect effect)
    {
        this.name = name;
        this.cost = cost;
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
    public int getCost()
    {
        return cost;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
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
            player.addPotionEffect(effect);
        return true;
    }
}
