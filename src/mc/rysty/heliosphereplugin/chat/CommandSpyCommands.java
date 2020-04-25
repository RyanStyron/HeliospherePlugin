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

	private HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	private SettingsManager settings = SettingsManager.getInstance();
	private FileConfiguration data = settings.getData();
	private FileConfiguration config = plugin.getConfig();

	public CommandSpyCommands(HelioSpherePlugin plugin) {
		plugin.getCommand("commandspy").setExecutor(this);
		plugin.getCommand("commandspy").setTabCompleter(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("commandspy")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("toggle")) {
					if (CommandSpy.CommandSpy) {
						if (sender.hasPermission("hs.commandspy.toggle")) {
							if (!(sender instanceof Player)) {
								sender.sendMessage(MessageUtils.chat(config.getString("console_error_message")));
							}
							Player player = (Player) sender;
							UUID playerId = player.getUniqueId();

							if (data.getConfigurationSection("users." + playerId + ".cmdspy.toggle") == null) {
								data.set("users." + playerId + ".cmdspy" + ".toggle.disabled", false);
								settings.saveData();
								player.sendMessage(MessageUtils.chat(config.getString("CommandSpy.cmdspy_enabled")));
							} else if (data.getConfigurationSection("users." + playerId + ".cmdspy.toggle") != null) {
								data.set("users." + playerId + ".cmdspy.toggle", null);
								settings.saveData();
								player.sendMessage(MessageUtils.chat(config.getString("CommandSpy.cmdspy_disabled")));
								return true;
							}
						} else {
							sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
							return false;
						}
					} else if (!CommandSpy.CommandSpy) {
						sender.sendMessage(MessageUtils.chat(config.getString("CommandSpy.cmdspy_currently_disabled")));
					}
				} else if (args[0].equalsIgnoreCase("bypass")) {
					if (CommandSpy.CommandSpy) {
						if (sender.hasPermission("hs.commandspy.bypass")) {
							Player player = (Player) sender;
							UUID playerId = player.getUniqueId();

							if (!(sender instanceof Player)) {
								sender.sendMessage(MessageUtils.chat(config.getString("console_error_message")));
							}
							if (data.getConfigurationSection("users." + playerId + ".cmdspy.bypass") == null) {
								data.set("users." + playerId + ".cmdspy" + ".bypass.enabled", true);
								settings.saveData();
								player.sendMessage(MessageUtils.chat(config.getString("CommandSpy.cmdspy_bypass_on")));
							} else if (data.getConfigurationSection("users." + playerId + ".cmdspy.bypass") != null) {
								data.set("users." + playerId + ".cmdspy.bypass", null);
								settings.saveData();
								player.sendMessage(MessageUtils.chat(config.getString("CommandSpy.cmdspy_bypass_off")));
								return true;
							}
						} else {
							sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
							return false;
						}
					} else if (!CommandSpy.CommandSpy) {
						sender.sendMessage(MessageUtils.chat(config.getString("CommandSpy.cmdspy_currently_disabled")));
					}
				} else if (args[0].equalsIgnoreCase("on")) {
					if (sender.hasPermission("hs.commandspy.onoff")) {
						if (CommandSpy.CommandSpy) {
							sender.sendMessage(
									MessageUtils.chat(config.getString("CommandSpy.cmdspy_already_enabled")));
							return false;
						} else if (!CommandSpy.CommandSpy) {
							CommandSpy.CommandSpy = true;
							sender.sendMessage(MessageUtils.chat(config.getString("CommandSpy.cmdspy_global_enabled")));
						}
					} else {
						sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
						return false;
					}
				} else if (args[0].equalsIgnoreCase("off")) {
					if (sender.hasPermission("hs.commandspy.onoff")) {
						if (!CommandSpy.CommandSpy) {
							sender.sendMessage(
									MessageUtils.chat(config.getString("CommandSpy.cmdspy_already_disabled")));
							return false;
						} else if (CommandSpy.CommandSpy) {
							CommandSpy.CommandSpy = false;
							sender.sendMessage(
									MessageUtils.chat(config.getString("CommandSpy.cmdspy_global_disabled")));
						}
					} else {
						sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
						return false;
					}
				}
			} else {
				if (args.length < 1) {
					sender.sendMessage(MessageUtils.chat(config.getString("CommandSpy.args_error")));
				}
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			List<String> completions = new ArrayList<>();

			completions.add("toggle");
			completions.add("bypass");
			completions.add("on");
			completions.add("off");

			return completions;
		}
		return null;
	}
}
