package mc.rysty.heliosphereplugin.chat;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import mc.rysty.heliosphereplugin.utils.SettingsManager;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class CommandSpy implements Listener {

	private SettingsManager settings = SettingsManager.getInstance();
	private FileConfiguration data = settings.getData();

	public static boolean commandSpyEnabled = true;

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		UUID playerId = player.getUniqueId();
		String playerDisplayName = player.getDisplayName();
		String message = event.getMessage();

		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			UUID onlinePlayerId = onlinePlayer.getUniqueId();

			if (commandSpyEnabled && onlinePlayer.hasPermission("hs.commandspy") && message.startsWith("/"))
				if (data.getBoolean("users." + onlinePlayerId + ".commandspy.enabled"))
					if (data.getBoolean("users." + playerId + ".commandspy.bypass") != true)
						MessageUtils.configStringMessage(onlinePlayer, "CommandSpy.message", "<player>",
								playerDisplayName, "<cmd>", message);
		}
	}
}
