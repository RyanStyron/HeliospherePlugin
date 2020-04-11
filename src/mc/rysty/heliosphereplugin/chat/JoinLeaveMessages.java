package mc.rysty.heliosphereplugin.chat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class JoinLeaveMessages implements Listener {

	private HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	private FileConfiguration config = plugin.getConfig();

	public JoinLeaveMessages(HelioSpherePlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (event.getJoinMessage().contains("joined the game"))
			event.setJoinMessage(null);

		Player player = event.getPlayer();
		String displayName = player.getDisplayName();
		String firstJoinMessage = config.getString("firstJoin_message").replace("<player>", displayName)
				.replaceAll("<server>", plugin.getServer().getName());
		String joinMessage = config.getString("join_message").replace("<player>", displayName);

		if (!player.hasPlayedBefore())
			MessageUtils.broadcastMessage(firstJoinMessage, null);
		else
			MessageUtils.broadcastMessage(joinMessage, null);
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		if (event.getQuitMessage().contains("left the game"))
			event.setQuitMessage(null);

		Player player = event.getPlayer();
		String displayName = player.getDisplayName();
		String message = config.getString("leave_message").replace("<player>", displayName);

		MessageUtils.broadcastMessage(message, null);
	}
}
