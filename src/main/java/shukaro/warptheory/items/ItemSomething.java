package shukaro.warptheory.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.util.FormatCodes;
import thaumcraft.api.ThaumcraftApiHelper;

import java.util.List;
import java.util.Locale;

public class ItemSomething extends Item
{
    private IIcon icon;

    public ItemSomething()
    {
        super();
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        this.setCreativeTab(WarpTheory.mainTab);
        this.setUnlocalizedName("warptheory.something");
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return this.getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs tab, List list)
    {
        list.add(new ItemStack(id, 1, 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        this.icon = reg.registerIcon(WarpTheory.modID.toLowerCase(Locale.ENGLISH) + ":" + "itemSomething");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.uncommon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta)
    {
        return this.icon;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
        return this.icon;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        return stack;
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            player.addChatMessage(new ChatComponentText(FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.addwarp")));
            ThaumcraftApiHelper.addWarpToPlayer(player, 10 + world.rand.nextInt(11), false);
        }

        if (!player.capabilities.isCreativeMode)
            stack.stackSize--;

        return stack.stackSize <= 0 ? null : stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.eat;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List infoList, boolean advanced)
    {
        infoList.add(FormatCodes.DarkGrey.code + FormatCodes.Italic.code + StatCollector.translateToLocal("tooltip.warptheory.something"));
    }
}
