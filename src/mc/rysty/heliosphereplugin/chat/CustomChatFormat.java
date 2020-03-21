package mc.rysty.heliosphereplugin.chat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class CustomChatFormat implements Listener {

	HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	FileConfiguration config = plugin.getConfig();

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		String dName = "%s";
		String msg = "%s".replaceAll("&", "§");
		String customChatFormat = MessageUtils.chat(config.getString("CustomChatFormat"));

		if (!msg.contains("&")) {
			event.setFormat(
					customChatFormat.replaceAll("<player>", dName).replaceAll("<message>", msg));
		} else {
			event.setFormat(
					customChatFormat.replaceAll("<player>", dName).replaceAll("<message>", msg.replaceAll("&", "§")));
		}
	}
}
