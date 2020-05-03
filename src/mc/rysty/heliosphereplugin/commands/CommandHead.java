package mc.rysty.heliosphereplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class CommandHead implements CommandExecutor {

    public CommandHead(HelioSpherePlugin plugin) {
        plugin.getCommand("head").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("head")) {
            if (sender.hasPermission("hs.head")) {
                if (sender instanceof Player) {
                    try {
                        if (args.length == 1) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:give " + sender.getName()
                                    + " minecraft:player_head{SkullOwner:" + args[0] + "}");
                            MessageUtils.configStringMessage(sender, "CommandHead.head-message", "<player>",
                                    (Bukkit.getPlayer(args[0]) != null ? Bukkit.getPlayer(args[0]).getDisplayName()
                                            : args[0]));
                        } else
                            MessageUtils.argumentError(sender, "/head <player>");
                    } catch (Exception exception) {
                        MessageUtils.validPlayerError(sender);
                    }
                } else
                    MessageUtils.consoleError();
            } else
                MessageUtils.noPermissionError(sender);
        }
        return false;
    }
}