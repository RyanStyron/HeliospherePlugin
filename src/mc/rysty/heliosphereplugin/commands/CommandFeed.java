package mc.rysty.heliosphereplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class CommandFeed implements CommandExecutor {

    public CommandFeed(HelioSpherePlugin plugin) {
        plugin.getCommand("feed").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("feed")) {
            if (sender.hasPermission("hs.feed")) {
                Player target = null;

                if (args.length > 0)
                    target = Bukkit.getPlayer(args[0]);
                else if (sender instanceof Player)
                    target = (Player) sender;

                if (target == null)
                    MessageUtils.validPlayerError(sender);
                else {
                    String displayName = target.getDisplayName();

                    target.setFoodLevel(20);
                    MessageUtils.configStringMessage(sender, "FeedCommand.feed-message", "<player>", displayName);
                    if (target != sender)
                        MessageUtils.configStringMessage(target, "FeedCommand.feed-player-message");
                }
            } else
                MessageUtils.noPermissionError(sender);
        }
        return false;
    }
}