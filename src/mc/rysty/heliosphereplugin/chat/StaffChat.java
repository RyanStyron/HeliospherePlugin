package mc.rysty.heliosphereplugin.chat;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.SettingsManager;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class StaffChat implements CommandExecutor, Listener {

	private FileConfiguration config = HelioSpherePlugin.getInstance().getConfig();
	private SettingsManager settings = SettingsManager.getInstance();
	private FileConfiguration data = settings.getData();

	public StaffChat(HelioSpherePlugin plugin) {
		plugin.getCommand("staffchat").setExecutor(this);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("staffchat")) {
			if (sender instanceof Player) {
				if (sender.hasPermission("hs.staffchat")) {
					Player player = (Player) sender;
					UUID playerId = player.getUniqueId();
					String staffChatConfigString = data.getString("users." + playerId + ".staffchat");

					if (args.length == 0) {
						if (staffChatConfigString != null) {
							data.set("users." + playerId + ".staffchat", null);
							MessageUtils.configStringMessage(sender, "StaffChat.sc_disabled");
						} else {
							data.set("users." + playerId + ".staffchat.enabled", true);
							MessageUtils.configStringMessage(sender, "StaffChat.sc_enabled");
						}
						settings.saveData();
					} else if (args.length >= 1) {
						MessageUtils.configStringMessage(sender, "StaffChat.arg_error");
					}
				} else
					MessageUtils.noPermissionError(sender);
			} else
				MessageUtils.consoleError();
		}
		return false;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage();
		UUID playerId = player.getUniqueId();
		String displayName = player.getDisplayName();

		if (message.startsWith("@sc")) {
			message = message.replace("@sc", "");

			event.setCancelled(true);
			MessageUtils.broadcastMessage(config.getString("StaffChat.sc_message").replaceAll("<msg>", message)
					.replaceAll("<player>", displayName), "hs.staffchat");
		} else if (!message.startsWith("@ac")) {
			if (data.getString("users." + playerId + ".staffchat") != null) {
				if (player.hasPermission("hs.staffchat")) {
					event.setCancelled(true);
					MessageUtils.broadcastMessage(config.getString("StaffChat.sc_message").replaceAll("<msg>", message)
							.replaceAll("<player>", displayName), "hs.staffchat");
				} else {
					data.set("users." + playerId + ".staffchat", null);
					settings.saveData();
				}
			}
		}
	}
}
