package mc.rysty.heliosphereplugin.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.chat.CommandSpy;
import mc.rysty.heliosphereplugin.utils.SettingsManager;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class PlayerInfo implements CommandExecutor {

	HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	FileConfiguration config = plugin.getConfig();
	SettingsManager settings = SettingsManager.getInstance();
	FileConfiguration data = settings.getData();

	public PlayerInfo(HelioSpherePlugin plugin) {
		plugin.getCommand("playerinfo").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("playerinfo")) {
			if (sender.hasPermission("hs.playerinfo")) {
				if (args.length == 1) {
					Player t = Bukkit.getPlayer(args[0]);

					if (t == null) {
						sender.sendMessage(MessageUtils.chat(config.getString("player_offline_message")));
						return false;
					} else {
						UUID targetId = t.getUniqueId();
						String tDName = t.getDisplayName();
						Location location = t.getLocation();
						ChatColor gold = ChatColor.GOLD;
						ChatColor yellow = ChatColor.YELLOW;
						String locationW = location.getWorld().getName();
						double locationX = location.getBlockX();
						double locationY = location.getBlockY();
						double locationZ = location.getBlockZ();
						// int ping = ((CraftPlayer) t).getHandle().ping;
						CommandSender s = sender;

						s.sendMessage(ChatColor.DARK_AQUA + "==" + gold + "Player Information: " + yellow + t.getName()
								+ ChatColor.DARK_AQUA + "==");
						s.sendMessage(gold + "Display Name: " + yellow + tDName);
						s.sendMessage(gold + "UUID: " + yellow + t.getUniqueId().toString());
						s.sendMessage(gold + "IP Address: " + yellow + t.getAddress().getAddress());
						// p.sendMessage(ChatColor.GOLD + "Ping: " + ChatColor.YELLOW + ping + "ms");
						s.sendMessage(gold + "Gamemode: " + yellow + t.getGameMode().toString());
						s.sendMessage(gold + "Health: " + yellow + t.getHealth());
						s.sendMessage(gold + "World: " + yellow + locationW);
						s.sendMessage(gold + "Coordinates: " + yellow + "X: " + locationX + ", Y: " + locationY
								+ ", Z: " + locationZ);
						if (CommandSpy.CommandSpy == true) {
							if (t.hasPermission("hs.commandspy")) {
								if (data.getConfigurationSection("users." + targetId + ".cmdspy.toggle") != null) {
									s.sendMessage(gold + "Command-Spy Enabled: " + ChatColor.GREEN + "TRUE");
								}
								if (data.getConfigurationSection("users." + targetId + ".cmdspy.toggle") == null) {
									s.sendMessage(gold + "Command-Spy Enabled: " + ChatColor.RED + "FALSE");
								}
								if (data.getConfigurationSection("users." + targetId + ".cmdspy.bypass") != null) {
									s.sendMessage(gold + "Command-Spy Bypass: " + ChatColor.GREEN + "TRUE");
								}
								if (data.getConfigurationSection("users." + targetId + ".cmdspy.bypass") == null) {
									s.sendMessage(gold + "Command-Spy Bypass: " + ChatColor.RED + "FALSE");
								}
							}
						}
						if (t.isOp()) {
							s.sendMessage(gold + "Operator: " + ChatColor.GREEN + "TRUE");
						}
						if (!t.isOp()) {
							s.sendMessage(gold + "Operator: " + ChatColor.RED + "FALSE");
						}
					}
				} else if (args.length < 1) {
					sender.sendMessage(MessageUtils.chat(config.getString("PlayerinfoCommand.not_enough_args_message")));
				} else if (args.length > 1) {
					sender.sendMessage(MessageUtils.chat(config.getString("PlayerinfoCommand.too_many_args_message")));
				}
			} else {
				sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
			}
		}
		return false;
	}

}
