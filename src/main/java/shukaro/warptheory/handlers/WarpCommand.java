package shukaro.warptheory.handlers;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand implements ICommand
{
    @Override
    public String getCommandName()
    {
        return "warptheory";
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "/warptheory <event> [player]";
    }

    @Override
    public List getCommandAliases()
    {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length == 0 || args.length > 2)
        {
            String events = "";
            for (IWarpEvent event : WarpHandler.warpEvents)
                events += event.getName();
            sender.addChatMessage(new ChatComponentText("Invalid Syntax, available events are: " + FormatCodes.Italic.code + events));
        }
        else if (args.length == 1)
        {
            EntityPlayer player = MiscHelper.getPlayerByName(sender.getCommandSenderName());
            for (IWarpEvent e : WarpHandler.warpEvents)
            {
                if (e.getName().equals(args[0]))
                {
                    e.doEvent(sender.getEntityWorld(), player);
                    break;
                }
            }
        }
        else if (args.length == 2)
        {
            EntityPlayer player = MiscHelper.getPlayerByName(args[1]);
            for (IWarpEvent e : WarpHandler.warpEvents)
            {
                if (e.getName().equals(args[0]))
                {
                    e.doEvent(sender.getEntityWorld(), player);
                    break;
                }
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        EntityPlayer player = MiscHelper.getPlayerByName(sender.getCommandSenderName());
        return player.capabilities.isCreativeMode || MiscHelper.isOp(sender.getCommandSenderName());
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args.length == 1)
        {
            ArrayList<String> completions = new ArrayList<String>();
            for (IWarpEvent event : WarpHandler.warpEvents)
            {
                if (event.getName().startsWith(args[0]))
                    completions.add(event.getName());
            }
            return completions;
        }
        else if (args.length == 2)
        {
            ArrayList<String> completions = new ArrayList<String>();
            for (EntityPlayer serverPlayer : (ArrayList<EntityPlayer>)MinecraftServer.getServer().getConfigurationManager().playerEntityList)
            {
                if (serverPlayer.getCommandSenderName().startsWith(args[1]))
                    completions.add(serverPlayer.getCommandSenderName());
            }
            return completions;
        }
        else
            return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i)
    {
        return args.length == 2 && i == 1;
    }

    @Override
    public int compareTo(Object o)
    {
        if (o instanceof ICommand)
        {
            ICommand other = (ICommand)o;
            return this.getCommandName().compareTo(other.getCommandName());
        }
        return 0;
    }
}
