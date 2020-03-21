package mc.rysty.heliosphereplugin.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class ChatCommands implements CommandExecutor, Listener {

	HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	FileConfiguration config = plugin.getConfig();

	public ChatCommands(HelioSpherePlugin plugin) {
		plugin.getCommand("chat").setExecutor(this);
	}

	public static boolean muted = false;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("chat")) {
			if (args.length == 0) {
				if (sender.hasPermission("hs.chat")) {
					sender.sendMessage(MessageUtils.chat(config.getString("ChatCommand.chat_help")));
					return true;
				} else {
					sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
				}
			}
			if ((args.length == 1) && (args[0].equalsIgnoreCase("clear"))) {
				if (sender.hasPermission("hs.chat.clear")) {
					for (int x = 0; x < 100; x++) {
						Bukkit.broadcastMessage("");
					}
					Bukkit.broadcastMessage(MessageUtils.chat(config.getString("ChatCommand.chat_cleared_message")));
					Bukkit.getOnlinePlayers().forEach(players -> players.playSound(players.getLocation(), "block.note_block.harp", 2, 1));
				} else {
					sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
				}
			}
			if ((args.length == 1) && ((args[0].equalsIgnoreCase("toggle")))) {
				if (sender.hasPermission("hs.chat.toggle")) {
					if (!muted) {
						muted = true;
						Bukkit.broadcastMessage(MessageUtils.chat(config.getString("ChatCommand.chat_locked_message")));
						Bukkit.getOnlinePlayers().forEach(players -> players.playSound(players.getLocation(), "block.note_block.harp", 2, 1));
						for (Player o : Bukkit.getOnlinePlayers()) {
							if (o.hasPermission("hs.chat.toggle")) {
								if (!(o instanceof Player)) {	
									o.sendMessage(MessageUtils.chat(config.getString("ChatCommand.chat_locked_staff_message")).replaceAll("<player>", ChatColor.YELLOW + sender.getName()));
								} else if (o instanceof Player){
									Player p = (Player) sender;
									o.sendMessage(MessageUtils.chat(config.getString("ChatCommand.chat_locked_staff_message")).replaceAll("<player>", ChatColor.YELLOW + p.getDisplayName()));
								}
							}
						}
					} else if (muted) {
						muted = false;
						Bukkit.broadcastMessage(MessageUtils.chat(config.getString("ChatCommand.chat_unlocked_message")));
						Bukkit.getOnlinePlayers().forEach(players -> players.playSound(players.getLocation(), "block.note_block.harp", 2, 1));
						
						for (Player o : Bukkit.getOnlinePlayers()) {
							if (o.hasPermission("hs.chat.toggle")) {
								if (!(o instanceof Player)) {
									o.sendMessage(MessageUtils.chat(config.getString("ChatCommand.chat_unlocked_staff_message")).replaceAll("<player>", ChatColor.YELLOW + sender.getName()));
								} else if (o instanceof Player){
									Player p = (Player) sender;
									o.sendMessage(MessageUtils.chat(config.getString("ChatCommand.chat_unlocked_staff_message")).replaceAll("<player>", ChatColor.YELLOW + p.getDisplayName()));
								}
							}
						}

					}
				} else {
					sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
				}
			}

		}
		return false;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();

		if (muted) {
			if (!p.hasPermission("hs.chat.locked")) {
				e.setCancelled(true);
				p.sendMessage(MessageUtils.chat(config.getString("ChatCommand.chat_locked_player_message")));
			} else if (p.hasPermission("hs.chat.locked")) {
				e.setCancelled(false);
			}
		} 
	}
}
