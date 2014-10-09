package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWarpEvent;

import java.util.ArrayList;

public class WarpBuff implements IWarpEvent
{
    private String name;
    private int cost;
    private ArrayList<PotionEffect> effects;

    public WarpBuff(String name, int cost, PotionEffect... effects)
    {
        this.name = name;
        this.cost = cost;
        this.effects = new ArrayList<PotionEffect>();
        for (PotionEffect e : effects)
            this.effects.add(e);
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
        for (PotionEffect effect : effects)
            player.addPotionEffect(effect);
        return true;
    }
}
