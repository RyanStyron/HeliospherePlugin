package mc.rysty.heliosphereplugin.chat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class CustomChatFormat implements Listener {

	private FileConfiguration config = HelioSpherePlugin.getInstance().getConfig();

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		String playerDisplayName = "%s";
		String message = "%s";

		event.setFormat(MessageUtils.chat(config.getString("CustomChatFormat").replaceAll("<player>", playerDisplayName)
				.replaceAll("<message>", message)));
	}
}
