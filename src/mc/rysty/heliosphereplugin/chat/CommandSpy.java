package mc.rysty.heliosphereplugin.chat;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.SettingsManager;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class CommandSpy implements Listener {

	HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	FileConfiguration config = plugin.getConfig();
	SettingsManager settings = SettingsManager.getInstance();
	FileConfiguration data = settings.getData();

	public static boolean CommandSpy = true;

	@EventHandler
	public void commandSpy(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		UUID playerId = player.getUniqueId();
		String pDName = player.getDisplayName();
		String msg = event.getMessage();

		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			UUID onlinePlayerId = onlinePlayer.getUniqueId();

			if (CommandSpy) {
				if (onlinePlayer.hasPermission("hs.commandspy")) {
					if (data.getConfigurationSection("users." + onlinePlayerId + ".cmdspy.toggle") != null) {
						if (data.getConfigurationSection("users." + playerId + ".cmdspy.bypass") == null) {
							if (msg.startsWith("/")) {
								onlinePlayer.sendMessage(MessageUtils.chat(config.getString("CommandSpy.message"))
										.replaceAll("<player>", pDName).replaceAll("<cmd>", msg));
							}
						}
					}
				}
			}
		}
	}
}
