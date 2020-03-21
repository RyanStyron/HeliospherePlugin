package mc.rysty.heliosphereplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class Ping implements CommandExecutor {

	private HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	private FileConfiguration config = plugin.getConfig();

	public Ping(HelioSpherePlugin plugin) {
		plugin.getCommand("ping").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("ping")) {
			if (sender.hasPermission("hs.ping")) {
				Player target = null;

				if (args.length > 0)
					target = Bukkit.getPlayer(args[0]);
				else if (sender instanceof Player)
					target = (Player) sender;

				if (target == null)
					sender.sendMessage(MessageUtils.chat(config.getString("PingCommand.argument_error")));
				else
					MessageUtils.message(sender, config.getString("PingCommand.ping_message")
							.replaceAll("<player>", target.getDisplayName()).replaceAll("<ping>", "N/A"));
			}
		}
		return false;
	}
}
