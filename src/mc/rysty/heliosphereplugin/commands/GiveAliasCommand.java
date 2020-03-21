package mc.rysty.heliosphereplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class GiveAliasCommand implements CommandExecutor {

	private HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	private FileConfiguration config = plugin.getConfig();

	public GiveAliasCommand(HelioSpherePlugin plugin) {
		plugin.getCommand("i").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("i")) {
			if (sender.hasPermission("hs.i")) {
				Player target = null;

				if (sender instanceof Player)
					target = (Player) sender;
				else
					MessageUtils.message(sender, config.getString("console_error_message"));

				if (args.length > 0) {
					String commandArguments = "";

					for (int i = 0; i < args.length; i++) {
						commandArguments = args[i] + " ";
					}
					ConsoleCommandSender console = Bukkit.getConsoleSender();

					try {
						Bukkit.dispatchCommand(console, "give " + target.getName() + " " + commandArguments);
					} catch (Exception exception) {
						MessageUtils.message(sender, ChatColor.RED + "A valid item must be provided.");
						exception.printStackTrace();
					}
				} else {
					MessageUtils.message(sender, config.getString("not_enough_args_error"));
				}
			} else {
				MessageUtils.message(sender, config.getString("no_perm_message"));
			}
		}
		return false;
	}
}
