package mc.rysty.heliosphereplugin.chat;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.SettingsManager;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class AFK implements CommandExecutor, Listener {

	private HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	private FileConfiguration config = plugin.getConfig();
	private SettingsManager settings = SettingsManager.getInstance();
	private FileConfiguration data = settings.getData();

	public AFK(HelioSpherePlugin plugin) {
		plugin.getCommand("afk").setExecutor(this);
	}

	private Server server = Bukkit.getServer();
	private String afkDisabledMessage = MessageUtils.chat(config.getString("AFK.afk_disabled"));
	private String afkEnabledMessage = MessageUtils.chat(config.getString("AFK.afk_enabled"));

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("afk")) {
			if (sender.hasPermission("hs.afk")) {
				if (!(sender instanceof Player)) {
					MessageUtils.message(sender, config.getString("console_error_message"));
					return false;
				}
				Player player = (Player) sender;
				UUID playerId = player.getUniqueId();
				String displayName = player.getDisplayName();

				if (args.length == 0) {
					if (data.getString("users." + playerId + ".afk") == null) {
						data.set("users." + playerId + ".afk", "true");
						settings.saveData();
						server.broadcastMessage(afkEnabledMessage.replaceAll("<player>", displayName));
					} else if (data.getString("users." + playerId + ".afk") != null) {
						data.set("users." + playerId + ".afk", null);
						settings.saveData();
						server.broadcastMessage(afkDisabledMessage.replaceAll("<player>", displayName));
					}
				} else {
					MessageUtils.message(player, config.getString("AFK.too_many_args"));
				}
			} else {
				MessageUtils.message(sender, config.getString("no_perm_message"));
			}
		}
		return false;
	}

	@EventHandler
	public void onPlayerUnactive(PlayerMoveEvent event) {
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		UUID playerId = player.getUniqueId();
		String displayName = player.getDisplayName();

		if (data.getString("users." + playerId + ".afk") != null) {
			data.set("users." + playerId + ".afk", null);
			settings.saveData();
			server.broadcastMessage(afkDisabledMessage.replaceAll("<player>", displayName));
		}
	}

	@EventHandler
	public void onChatEvent(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		UUID pId = player.getUniqueId();
		String pDName = player.getDisplayName();

		if (data.getString("users." + pId + ".afk") != null) {
			data.set("users." + pId + ".afk", null);
			settings.saveData();
			server.broadcastMessage(afkDisabledMessage.replaceAll("<player>", pDName));
		}
	}

	@EventHandler
	public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		UUID playerId = player.getUniqueId();
		String displayName = player.getDisplayName();

		if (event.getMessage().startsWith("/")) {
			if (data.getString("users." + playerId + ".afk") != null) {
				data.set("users." + playerId + ".afk", null);
				settings.saveData();
				server.broadcastMessage(afkDisabledMessage.replaceAll("<player>", displayName));
			}
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		UUID playerId = player.getUniqueId();

		if (data.getString("users." + playerId + ".afk") != null) {
			data.set("users." + playerId + ".afk", null);
			settings.saveData();
		}
	}
}
