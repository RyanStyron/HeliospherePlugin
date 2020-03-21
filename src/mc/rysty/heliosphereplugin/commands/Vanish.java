package mc.rysty.heliosphereplugin.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class Vanish implements CommandExecutor, Listener {

	HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	FileConfiguration config = plugin.getConfig();

	public Vanish(HelioSpherePlugin plugin) {
		plugin.getCommand("vanish").setExecutor(this);
	}

	private String joinMessage = config.getString("join_message");
	private String leaveMessage = config.getString("leave_message");
	private ArrayList<Player> vanished = new ArrayList<Player>();

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("vanish")) {
			if (sender.hasPermission("hs.vanish")) {
				if (args.length == 0) {
					if (!(sender instanceof Player)) {
						sender.sendMessage(MessageUtils.chat(config.getString("console_player_error_message")));
						return false;
					}
					Player p = (Player) sender;
					String dName = p.getDisplayName();

					if (!vanished.contains(p)) {
						for (Player pl : Bukkit.getOnlinePlayers()) {
							pl.hidePlayer(p);
						}
						vanished.add(p);
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								"discord bcast > " + dName + " has left the server");
						Bukkit.broadcastMessage(MessageUtils.chat(leaveMessage.replace("<player>", dName)));
						p.sendMessage(MessageUtils.chat(config.getString("VanishCommand.vanish_on_message")));
					} else {
						for (Player pl : Bukkit.getOnlinePlayers()) {
							pl.showPlayer(p);
						}
						vanished.remove(p);
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								"discord bcast > " + dName + " has joined the server");
						Bukkit.broadcastMessage(MessageUtils.chat(joinMessage.replace("<player>", dName)));
						p.sendMessage(MessageUtils.chat(config.getString("VanishCommand.vanish_off_message")));
					}
				} else if (args.length == 1) {
					Player target = Bukkit.getPlayer(args[0]);

					if (target == null) {
						sender.sendMessage(MessageUtils.chat(config.getString("player_offline_message")));
						return false;
					} else {
						String targetName = target.getDisplayName();

						if (!vanished.contains(target)) {
							for (Player pl : Bukkit.getOnlinePlayers()) {
								pl.hidePlayer(target);
							}
							vanished.add(target);
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
									"discord bcast > " + targetName + " has left the server");
							Bukkit.broadcastMessage(MessageUtils.chat(leaveMessage.replace("<player>", targetName)));
							target.sendMessage(MessageUtils.chat(config.getString("VanishCommand.vanish_on_message")));
							sender.sendMessage(MessageUtils.chat(config.getString("VanishCommand.vanish_other_on_message")
									.replaceAll("<player>", targetName)));
						} else {
							for (Player pl : Bukkit.getOnlinePlayers()) {
								pl.showPlayer(target);
							}
							vanished.remove(target);
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
									"discord bcast > " + targetName + " has joined the server");
							Bukkit.broadcastMessage(MessageUtils.chat(joinMessage.replace("<player>", targetName)));
							target.sendMessage(MessageUtils.chat(config.getString("VanishCommand.vanish_off_message")));
							sender.sendMessage(MessageUtils.chat(config.getString("VanishCommand.vanish_other_off_message")
									.replaceAll("<player>", targetName)));
						}
					}
				} else if (args.length > 1) {
					sender.sendMessage(MessageUtils.chat(config.getString("too_many_args_error")));
					return false;
				}
			} else {
				sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		for (Player p : vanished) {
			e.getPlayer().hidePlayer(p);
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		vanished.remove(e.getPlayer());
	}
}
