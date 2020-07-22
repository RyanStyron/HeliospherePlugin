package mc.rysty.heliosphereplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class Sudo implements CommandExecutor {

    public Sudo(HelioSpherePlugin plugin) {
        plugin.getCommand("sudo").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("sudo")) {
            if (sender.hasPermission("hs.sudo")) {
                if (args.length > 1) {
                    Player target = Bukkit.getPlayer(args[0]);

                    if (target != null) {
                        String sudoString = "";

                        for (int i = 1; i < args.length; i++)
                            sudoString += args[i] + " ";

                        if (sudoString.startsWith("/"))
                            target.performCommand(sudoString.replace("/", ""));
                        else
                            target.chat(MessageUtils.chat(sudoString));

                        MessageUtils.configStringMessage(sender, "Sudo.sudo-message", "<player>",
                                target.getDisplayName());
                    } else
                        MessageUtils.configStringMessage(sender, "player_offline_message");
                } else
                    MessageUtils.configStringMessage(sender, "Sudo.argument-error");
            } else
                MessageUtils.configStringMessage(sender, "no_perm_error");
        }
        return false;
    }
}