package net.rapust.blockreset.command;

import net.rapust.blockreset.BlockReset;
import net.rapust.blockreset.block.BlocksManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BlockResetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.isOp()) {
            sender.sendMessage(BlockReset.getInstance().getMessage("noPerm"));
            return true;
        }

        if (args.length != 1) return false;
        if (!args[0].equalsIgnoreCase("reload")) return false;

        try {
            BlockReset.getInstance().reloadConfig();
            BlocksManager.getManager().clear();
            BlocksManager.getManager().reload();
        } catch (Exception exception) {
            sender.sendMessage(BlockReset.getInstance().getMessage("error"));
            return true;
        }

        sender.sendMessage(BlockReset.getInstance().getMessage("success"));

        return true;
    }
}
