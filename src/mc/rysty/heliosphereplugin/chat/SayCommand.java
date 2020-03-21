package mc.rysty.heliosphereplugin.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class SayCommand implements CommandExecutor {

	HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	FileConfiguration config = plugin.getConfig();

	public SayCommand(HelioSpherePlugin plugin) {
		plugin.getCommand("say").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("say")) {
			if (args.length > 0) {
				String message = "";

				for (int i = 0; i < args.length; i++) {
					message += args[i] + " ";
				}

				if (sender instanceof Player) {
					Player player = (Player) sender;
					String displayName = player.getDisplayName();
					String customChatFormat = MessageUtils.chat(config.getString("CustomChatFormat"));

					Bukkit.broadcastMessage(MessageUtils.chat(MessageUtils.chat(
							customChatFormat.replaceAll("<player>", displayName).replaceAll("<message>", message))));
				}

				if (sender instanceof ConsoleCommandSender) {
					ConsoleCommandSender console = (ConsoleCommandSender) sender;
					String staffChatMessage = MessageUtils.chat(config.getString("StaffChat.sc_message"));

					for (Player player : plugin.getServer().getOnlinePlayers()) {
						if (player.hasPermission("hs.sc.see")) {
							player.sendMessage(MessageUtils.chat(staffChatMessage.replaceAll("<msg>", message)
									.replaceAll("<player>", "&6&lConsole")));
						}
					}
					console.sendMessage(MessageUtils.chat(staffChatMessage.replaceAll("<msg>", message).replaceAll("<player>", "&6&lConsole")));
				}
			} else {
				sender.sendMessage(MessageUtils.chat(config.getString("not_enough_args_error")));
			}
		}
		return false;
	}

}
