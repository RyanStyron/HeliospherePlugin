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
		String playerDisplayName = "%s";
		String message = "%s";
		String customChatFormat = convertChatColors(config.getString("CustomChatFormat"));

		event.setFormat(customChatFormat.replaceAll("<player>", playerDisplayName).replaceAll("<message>",
				convertChatColors(message)));
	}

	private String convertChatColors(String string) {
		return MessageUtils.chat(string);
	}
}
