package mc.rysty.heliosphereplugin.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.chat.CommandSpy;
import mc.rysty.heliosphereplugin.utils.SettingsManager;
import mc.rysty.heliosphereplugin.utils.VersionUtils;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class PlayerInfo implements CommandExecutor {

	private HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	private FileConfiguration config = plugin.getConfig();
	private SettingsManager settings = SettingsManager.getInstance();
	private FileConfiguration data = settings.getData();

	public PlayerInfo(HelioSpherePlugin plugin) {
		plugin.getCommand("playerinfo").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("playerinfo")) {
			if (sender.hasPermission("hs.playerinfo")) {
				if (args.length == 1) {
					Player target = Bukkit.getPlayer(args[0]);

					if (target == null) {
						sender.sendMessage(MessageUtils.chat(config.getString("player_offline_message")));
					} else {
						UUID targetId = target.getUniqueId();
						String displayName = target.getDisplayName();
						Location location = target.getLocation();
						String locationWorldName = location.getWorld().getName();
						double locationX = location.getBlockX();
						double locationY = location.getBlockY();
						double locationZ = location.getBlockZ();
						String version = VersionUtils.getPlayerVersion(target);

						MessageUtils.message(sender, "&3==&6Player Information: &e" + target.getName() + "&3==");
						MessageUtils.message(sender, "&6Display Name: &e" + displayName);
						MessageUtils.message(sender, "&6Version: &e" + version);
						MessageUtils.message(sender, "&6UUID: &e" + targetId);
						MessageUtils.message(sender, "&6IP Address: &e" + target.getAddress());
						MessageUtils.message(sender, "&6Gamemode: &e" + target.getGameMode().toString());
						MessageUtils.message(sender, "&6Flying: " + (target.getAllowFlight() ? "&aTrue" : "&cFalse"));
						MessageUtils.message(sender, "&6Health: &e" + target.getHealth());
						MessageUtils.message(sender, "&6World: &e" + locationWorldName);
						MessageUtils.message(sender,
								"&6Coordinates: &eX: " + locationX + ", Y: " + locationY + ", Z: " + locationZ);
						MessageUtils.message(sender, "&6Muted: "
								+ (data.getString("users." + targetId + ".muted") != null ? "&aTrue" : "&cFalse"));
						if (CommandSpy.commandSpyEnabled == true) {
							boolean commandSpyEnabled = data.getBoolean("users." + targetId + ".commandspy.enabled");
							boolean commandSpyBypass = data.getBoolean("users." + targetId + ".commandspy.bypass");

							if (target.hasPermission("hs.commandspy")) {
								MessageUtils.message(sender, commandSpyEnabled ? "&6Command-Spy Enabled: &aTrue"
										: "&6Command-Spy Enabled: &cFalse");
								MessageUtils.message(sender, commandSpyBypass ? "&6Command-Spy Bypass: &aTrue"
										: "&6Command-Spy Bypass: &cFalse");
							}
						}
						MessageUtils.message(sender, target.isOp() ? "&6Operator: &aTrue" : "&6Operator: &cFalse");
					}
				} else if (args.length < 1)
					MessageUtils.configStringMessage(sender, "PlayerinfoCommand.not_enough_args_message");
				else if (args.length > 1)
					MessageUtils.configStringMessage(sender, "PlayerinfoCommand.too_many_args_message");
			} else {
				sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
			}
		}
		return false;
	}

}
