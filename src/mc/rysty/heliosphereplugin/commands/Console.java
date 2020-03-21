package mc.rysty.heliosphereplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class Console implements CommandExecutor {

	HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	FileConfiguration config = plugin.getConfig();

	public Console(HelioSpherePlugin plugin) {
		plugin.getCommand("console").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("console")) {
			if (sender.hasPermission("hs.console")) {
				if (args.length == 0) {
					sender.sendMessage(MessageUtils.chat(config.getString("ConsoleCommand.argument_error")));
				} else {
					try {
						ConsoleCommandSender console = Bukkit.getConsoleSender();
						String inputCommand = "";

						for (int i = 0; i < args.length; i++) {
							inputCommand += args[i] + " ";
						}
						Bukkit.dispatchCommand(console, inputCommand);
						sender.sendMessage(MessageUtils.chat(config.getString("ConsoleCommand.console_command_message")));
					} catch (Exception exception) {
						sender.sendMessage(ChatColor.RED
								+ "An internal error occurred. Please contact an administrator for help.");
						exception.printStackTrace();
					}
				}
			} else
				sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
		}
		return false;
	}
}
