package mc.rysty.heliosphereplugin.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;
import mc.rysty.heliosphereplugin.utils.SettingsManager;

public class CommandSpyCommands implements CommandExecutor, TabCompleter {

	private SettingsManager settings = SettingsManager.getInstance();
	private FileConfiguration data = settings.getData();

	public CommandSpyCommands(HelioSpherePlugin plugin) {
		plugin.getCommand("commandspy").setExecutor(this);
		plugin.getCommand("commandspy").setTabCompleter(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("commandspy")) {
			if (sender.hasPermission("hs.commandspy")) {
				if (args.length == 1) {
					String commandArgument = args[0];

					if (commandArgument.equalsIgnoreCase("toggle"))
						commandArgument = "toggle";
					else if (commandArgument.equalsIgnoreCase("bypass"))
						if (sender.hasPermission("hs.commandspy.bypass"))
							commandArgument = "bypass";
						else {
							MessageUtils.noPermissionError(sender);
							return false;
						}
					else if (commandArgument.equalsIgnoreCase("servertoggle"))
						if (sender.hasPermission("hs.commandspy.servertoggle"))
							commandArgument = "servertoggle";
						else {
							MessageUtils.noPermissionError(sender);
							return false;
						}
					else
						MessageUtils.argumentError(sender, "/commandspy <toggle|servertoggle|bypass>");

					if (commandArgument.equals("toggle") || commandArgument.equals("bypass")) {
						if (!(sender instanceof Player)) {
							MessageUtils.consoleError();
							return false;
						}
						if (!CommandSpy.commandSpyEnabled) {
							MessageUtils.configStringMessage(sender, "CommandSpy.cmdspy_currently_disabled");
							return false;
						}
					}

					if (commandArgument.equals("toggle")) {
						Player player = (Player) sender;
						UUID playerId = player.getUniqueId();

						if (data.getBoolean("users." + playerId + ".commandspy.enabled") != true) {
							data.set("users." + playerId + ".commandspy.enabled", true);
							settings.saveData();
							MessageUtils.configStringMessage(sender, "CommandSpy.cmdspy_enabled");
						} else {
							data.set("users." + playerId + ".commandspy.enabled", null);
							settings.saveData();
							MessageUtils.configStringMessage(sender, "CommandSpy.cmdspy_disabled");
						}
					} else if (commandArgument.equals("bypass")) {
						Player player = (Player) sender;
						UUID playerId = player.getUniqueId();

						if (data.getBoolean("users." + playerId + ".commandspy.bypass") != true) {
							data.set("users." + playerId + ".commandspy.bypass", true);
							settings.saveData();
							MessageUtils.configStringMessage(sender, "CommandSpy.cmdspy_bypass_on");
						} else {
							data.set("users." + playerId + ".commandspy.bypass", null);
							settings.saveData();
							MessageUtils.configStringMessage(sender, "CommandSpy.cmdspy_bypass_off");
						}
					} else if (commandArgument.equals("servertoggle")) {
						CommandSpy.commandSpyEnabled = !CommandSpy.commandSpyEnabled;

						if (CommandSpy.commandSpyEnabled)
							MessageUtils.configStringMessage(sender, "CommandSpy.cmdspy_global_enabled");
						else
							MessageUtils.configStringMessage(sender, "CommandSpy.cmdspy_global_disabled");
					}
				} else
					MessageUtils.argumentError(sender, "/commandspy <toggle|servertoggle|bypass>");
			} else
				MessageUtils.noPermissionError(sender);
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			List<String> completions = new ArrayList<>();

			completions.add("bypass");
			completions.add("toggle");
			completions.add("servertoggle");

			return completions;
		}
		return null;
	}
}
