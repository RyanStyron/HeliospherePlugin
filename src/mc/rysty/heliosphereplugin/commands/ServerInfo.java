package mc.rysty.heliosphereplugin.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.chat.ChatCommands;
import mc.rysty.heliosphereplugin.chat.CommandSpy;
import mc.rysty.heliosphereplugin.utils.ListUtils;
import mc.rysty.heliosphereplugin.utils.MessageUtils;
import mc.rysty.heliosphereplugin.utils.VersionUtils;

public class ServerInfo implements CommandExecutor {

	public ServerInfo(HelioSpherePlugin plugin) {
		plugin.getCommand("serverinfo").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("serverinfo")) {
			if (sender.hasPermission("hs.serverinfo")) {
				Server server = Bukkit.getServer();
				String serverName = server.getName();
				String serverVersion = VersionUtils.getServerVersion();
				int opSize = server.getOperators().size();
				int banSize = server.getBannedPlayers().size();
				PluginManager pluginManager = server.getPluginManager();
				int pluginsSize = pluginManager.getPlugins().length;
				OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
				Collection<? extends Player> onlinePlayers = server.getOnlinePlayers();
				int onlinePlayersSize = onlinePlayers.size();
				int totalPlayers = offlinePlayers.length;

				MessageUtils.message(sender, "&3==&6Server Information: &e" + serverName + "&3==");
				MessageUtils.message(sender, "&6Online Players: &e" + onlinePlayersSize);
				MessageUtils.message(sender, "&6Total Players: &e" + totalPlayers);
				MessageUtils.message(sender, "&6Plugins: &e" + pluginsSize);
				MessageUtils.message(sender, "&6Version: &e" + serverVersion);

				List<String> operatorList = new ArrayList<>();
				List<String> banList = new ArrayList<>();
				for (OfflinePlayer offlinePlayer : offlinePlayers) {
					if (offlinePlayer.isOp())
						operatorList.add(offlinePlayer.getName());
					if (offlinePlayer.isBanned())
						banList.add(offlinePlayer.getName());
				}

				MessageUtils.message(sender,
						"&6Operators (&e" + opSize + "&6): &e" + ListUtils.fromList(operatorList, false, false));
				MessageUtils.message(sender,
						"&6Ban List &6(&e" + banSize + "&6): &e" + ListUtils.fromList(banList, false, false));

				MessageUtils.message(sender, "&6Whitelist Enabled: " + (server.hasWhitelist() ? "&aTrue" : "&cFalse"));
				MessageUtils.message(sender, (!ChatCommands.muted ? "&6Chat Muted: &cFalse" : "&6Chat Muted: &aTrue"));
				MessageUtils.message(sender,
						(CommandSpy.CommandSpy ? "&6Command-Spy Enabled: &aTrue" : "Command-Spy Enabled: &cFalse"));
			} else
				MessageUtils.configStringMessage(sender, "no_perm_message");
		}
		return false;
	}
}
