package mc.rysty.heliosphereplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class Reload implements CommandExecutor {

	HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	FileConfiguration config = plugin.getConfig();

	public Reload(HelioSpherePlugin plugin) {
		plugin.getCommand("hs-core").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("hs-core")) {
			CommandSender s = sender;
			ChatColor red = ChatColor.RED;
			
			if (s.hasPermission("hs.core.reload")) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("reload")) {
						plugin.reloadConfig();
						s.sendMessage("HS-Core has been reloaded");
						return true;
					} else {
						s.sendMessage(red + "That argument could not be accepted. Correct format: /hs-core reload");
					}
				} else if (args.length < 1) {
					s.sendMessage(red + "Not enough arguments were provided! Correct format: /hs-core reload");
					return false;
				} else if (args.length > 1) {
					s.sendMessage(red + "Too many arguments were provided! Correct format: /hs-core reload");
					return false;
				}
			} else {
				sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
				return false;
			}
		}
		return false;
	}
}
