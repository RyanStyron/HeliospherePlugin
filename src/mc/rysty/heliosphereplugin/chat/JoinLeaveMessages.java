package mc.rysty.heliosphereplugin.chat;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class JoinLeaveMessages implements Listener {
	
	HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	FileConfiguration config = plugin.getConfig();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (e.getJoinMessage().contains("joined the game")) {
			e.setJoinMessage(null);
		}
		Player p = e.getPlayer();
		String dName = p.getDisplayName();
		
		if (!p.hasPlayedBefore()) {
			Bukkit.broadcastMessage(MessageUtils.chat(config.getString("firstJoin_message").replace("<player>", dName).replaceAll("<server>", plugin.getServer().getName())));
		} else {
			Bukkit.broadcastMessage(MessageUtils.chat(config.getString("join_message").replace("<player>", dName)));
		}
	}
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		if (e.getQuitMessage().contains("left the game")) {
			e.setQuitMessage(null);
		}
		Player p = e.getPlayer();
		String dName = p.getDisplayName();
		
		Bukkit.broadcastMessage(MessageUtils.chat(config.getString("leave_message").replace("<player>", dName)));
	}
}
