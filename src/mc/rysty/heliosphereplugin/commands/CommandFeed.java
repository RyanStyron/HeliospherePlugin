package mc.rysty.heliosphereplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;

public class CommandFeed implements CommandExecutor {

    public CommandFeed(HelioSpherePlugin plugin) {
        plugin.getCommand("feed").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("feed")) {
            
        }
        return false;
    }
}