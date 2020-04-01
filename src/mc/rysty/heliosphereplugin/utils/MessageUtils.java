package mc.rysty.heliosphereplugin.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;

public class MessageUtils {

	private static FileConfiguration config = HelioSpherePlugin.getInstance().getConfig();

	public static String chat(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public static void message(CommandSender sender, String message) {
		sender.sendMessage(chat(message));
	}

	public static void configStringMessage(CommandSender sender, String configString) {
		message(sender, config.getString(configString));
	}
}