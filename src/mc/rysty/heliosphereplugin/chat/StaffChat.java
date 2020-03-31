package mc.rysty.heliosphereplugin.chat;

import java.util.UUID;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.SettingsManager;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class StaffChat implements CommandExecutor, Listener {

	HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	FileConfiguration config = plugin.getConfig();
	SettingsManager settings = SettingsManager.getInstance();
	FileConfiguration data = settings.getData();

	public StaffChat(HelioSpherePlugin plugin) {
		plugin.getCommand("staffchat").setExecutor(this);
	}

	private Server server = plugin.getServer();
	private CommandSender console = server.getConsoleSender();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("staffchat")) {
			if (sender instanceof Player) {
				if (sender.hasPermission("hs.staffchat")) {
					Player player = (Player) sender;
					UUID playerId = player.getUniqueId();
					String scDisabled = MessageUtils.chat(config.getString("StaffChat.sc_disabled"));
					String scEnabled = MessageUtils.chat(config.getString("StaffChat.sc_enabled"));
					ConfigurationSection staffChatConfigurationSection = data
							.getConfigurationSection("users." + playerId + ".staffchat");

					if (args.length == 0) {
						if (staffChatConfigurationSection != null) {
							data.set(("users." + playerId + ".staffchat"), null);
							settings.saveData();
							player.sendMessage(scDisabled);
							return true;
						} else if (staffChatConfigurationSection == null) {
							data.set("users." + playerId + ".staffchat.enabled", true);
							settings.saveData();
							player.sendMessage(scEnabled);
							return true;
						}
					} else if (args.length >= 1) {
						player.sendMessage(MessageUtils.chat(config.getString("StaffChat.arg_error")));
						return true;
					}
				} else {
					sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
				}
			} else {
				sender.sendMessage(MessageUtils.chat(config.getString("console_error_message")));
			}
		}
		return false;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String m = event.getMessage();
		UUID playerId = player.getUniqueId();
		String dName = player.getDisplayName();
		String scMessage = MessageUtils.chat(config.getString("StaffChat.sc_message"));

		if (data.getConfigurationSection("users." + playerId + ".staffchat") != null) {
			if (player.hasPermission("hs.staffchat")) {
				event.setCancelled(true);
				for (Player s : server.getOnlinePlayers()) {
					if (s.hasPermission("hs.sc.see")) {
						s.sendMessage(scMessage.replaceAll("<msg>", MessageUtils.chat(m)).replaceAll("<player>",
								MessageUtils.chat(dName)));
					}
				}
				console.sendMessage(scMessage.replaceAll("<msg>", MessageUtils.chat(m)).replaceAll("<player>",
						MessageUtils.chat(dName)));
			} else {
				data.set("users." + playerId + ".staffchat", null);
				settings.saveData();
			}
		}
	}
}
