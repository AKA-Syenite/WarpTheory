package shukaro.warptheory.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.FormatCodes;

import java.util.List;
import java.util.Locale;

public class ItemActivator extends Item
{
    private IIcon icon;

    int index = 0;

    public ItemActivator()
    {
        super();
        this.setHasSubtypes(false);
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        this.setCreativeTab(WarpTheory.mainTab);
        this.setUnlocalizedName("warptheory.activator");
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return this.getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        this.icon = reg.registerIcon(WarpTheory.modID.toLowerCase(Locale.ENGLISH) + ":" + "itemActivator");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.uncommon;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
        return this.icon;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (world.isRemote) { return null; }
        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
        {
            if (index < WarpHandler.warpEvents.size() - 1) { index++;}
            else { index = 0; }

            return itemStack;
        }

        //Do warp event based on index.
        IWarpEvent event = WarpHandler.warpEvents.get(index);
        event.doEvent(world, player);

        return itemStack;
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List infoList, boolean advanced)
    {

        infoList.add(FormatCodes.DarkGrey.code + FormatCodes.Italic.code + WarpHandler.warpEvents.get(index).getName() + " (" + index + " / " + WarpHandler.warpEvents.size());
    }
}
