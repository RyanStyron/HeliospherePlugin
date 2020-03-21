package mc.rysty.heliosphereplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.chat.ChatCommands;
import mc.rysty.heliosphereplugin.chat.CommandSpy;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class ServerInfo implements CommandExecutor {

	HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	FileConfiguration config = plugin.getConfig();

	public ServerInfo(HelioSpherePlugin plugin) {
		plugin.getCommand("serverinfo").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("serverinfo")) {
			if (sender.hasPermission("hs.serverinfo")) {
				ChatColor gold = ChatColor.GOLD;
				ChatColor yellow = ChatColor.YELLOW;
				Server server = Bukkit.getServer();
				String serverName = Bukkit.getName();
				String serverVersion = server.getVersion().toString();
				String serverIp = server.getIp().toString();
				int opSize = server.getOperators().size();
				int banSize = server.getBannedPlayers().size();
				PluginManager pm = server.getPluginManager();
				int pluginsSize = pm.getPlugins().length;
				OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
				int onlinePlayers = server.getOnlinePlayers().size();
				int totalPlayers = offlinePlayers.length;
				CommandSender s = sender;

				s.sendMessage(ChatColor.DARK_AQUA + "==" + gold + "Server Information: " + yellow + serverName
						+ ChatColor.DARK_AQUA + "==");
				s.sendMessage(gold + "Online Players: " + yellow + onlinePlayers);
				s.sendMessage(gold + "Total Players: " + yellow + totalPlayers);
				s.sendMessage(gold + "IP: " + yellow + serverIp);
				s.sendMessage(gold + "Plugins: " + yellow + pluginsSize);
				s.sendMessage(gold + "Version: " + yellow + serverVersion);
				s.sendMessage(gold + "Operators " + gold + "(" + yellow + opSize + gold + "): ");
				for (OfflinePlayer ops : offlinePlayers) {
					if (ops.isOp()) {
						s.sendMessage(gold + "- " + yellow + ops.getName());
					}
				}
				s.sendMessage(gold + "Ban List " + gold + "(" + yellow + banSize + gold + "): ");
				for (OfflinePlayer bans : offlinePlayers) {
					if (bans.isBanned()) {
						s.sendMessage(gold + "- " + yellow + bans.getName());
					}
				}
				if (!ChatCommands.muted) {
					s.sendMessage(gold + "Chat Muted: " + ChatColor.RED + "FALSE");
				}
				if (ChatCommands.muted) {
					s.sendMessage(gold + "Chat Muted: " + ChatColor.GREEN + "TRUE");
				}
				if (CommandSpy.CommandSpy) {
					s.sendMessage(gold + "Command-Spy Enabled: " + ChatColor.GREEN + "TRUE");
				}
				if (!CommandSpy.CommandSpy) {
					s.sendMessage(gold + "Command-Spy Enabled: " + ChatColor.RED + "FALSE");
				}
			} else {
				sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
			}
		}
		return false;
	}
}
