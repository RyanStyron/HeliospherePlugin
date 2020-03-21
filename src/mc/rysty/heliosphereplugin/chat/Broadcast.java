package mc.rysty.heliosphereplugin.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class Broadcast implements CommandExecutor {

	HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	FileConfiguration config = plugin.getConfig();

	public Broadcast(HelioSpherePlugin plugin) {
		plugin.getCommand("broadcast").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("broadcast")) {
			if (sender.hasPermission("hs.bcast")) {
				if (args.length >= 1) {
					String msg = "";

					for (int i = 0; i < args.length; i++) {
						msg = msg + args[i] + " ";
					}
					Bukkit.broadcastMessage(MessageUtils.chat(msg.replaceAll("&", "§")));
					Bukkit.getOnlinePlayers().forEach(players -> players.playSound(players.getLocation(), "block.note_block.harp", 2, 1));
				} else {
					sender.sendMessage(MessageUtils.chat(config.getString("BroadcastCommand.args_error")));
				}
			} else {
				sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
			}
		}
		return false;
	}
}
