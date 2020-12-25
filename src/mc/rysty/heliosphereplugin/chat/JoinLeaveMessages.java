package mc.rysty.heliosphereplugin.chat;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageBuilder;
import mc.rysty.heliosphereplugin.utils.MessageUtils;
import mc.rysty.heliosphereplugin.utils.SettingsManager;
import net.md_5.bungee.api.chat.HoverEvent;

public class JoinLeaveMessages implements Listener {

	private HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	private FileConfiguration config = plugin.getConfig();
	private static FileConfiguration dataFile = SettingsManager.getInstance().getData();

	public JoinLeaveMessages(HelioSpherePlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (event.getJoinMessage().contains("joined the game"))
			event.setJoinMessage(null);

		Player player = event.getPlayer();
		String displayName = dataFile.getString("users." + player.getUniqueId() + ".displayname");

		if (!player.hasPlayedBefore())
			MessageUtils.broadcastMessage(config.getString("firstJoin_message").replace("<player>", displayName), null);
		else {
			MessageBuilder builder = new MessageBuilder();

			builder.append(config.getString("join_message").replace("<player>", displayName))
					.hoverEvent(HoverEvent.Action.SHOW_TEXT, "&6" + player.getName());

			for (Player onlinePlayers : Bukkit.getOnlinePlayers())
				MessageUtils.message(onlinePlayers, builder.build());
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		if (event.getQuitMessage().contains("left the game"))
			event.setQuitMessage(null);

		Player player = event.getPlayer();
		String displayName = player.getDisplayName();

		MessageUtils.broadcastMessage(config.getString("leave_message").replace("<player>", displayName), null);
	}
}
