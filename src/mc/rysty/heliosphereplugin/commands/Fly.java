package mc.rysty.heliosphereplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class Fly implements CommandExecutor {

	private HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	private FileConfiguration config = plugin.getConfig();

	public Fly(HelioSpherePlugin plugin) {
		plugin.getCommand("fly").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("fly")) {
			if (sender.hasPermission("hs.fly")) {
				Player target = null;

				if (args.length == 0) {
					if (!(sender instanceof Player)) {
						sender.sendMessage(MessageUtils.chat(config.getString("console_player_error_message")));
						return true;
					}
					target = (Player) sender;

					if (target.isFlying()) {
						target.setAllowFlight(false);
						target.setFlying(false);
						target.sendMessage(MessageUtils.chat(config.getString("FlyCommand.flying_disabled")));
						return true;
					} else {
						target.setAllowFlight(true);
						target.setFlying(true);
						target.sendMessage(MessageUtils.chat(config.getString("FlyCommand.flying_enabled")));
					}
				} else if (args.length == 1) {
					if (sender.hasPermission("hs.fly.other")) {
						target = Bukkit.getPlayer(args[0]);

						if (target == null)
							sender.sendMessage(MessageUtils.chat(config.getString("player_offline_message")));
						else if (target != null)
							if (target.isFlying()) {
								target.setAllowFlight(false);
								target.setFlying(false);
								target.sendMessage(MessageUtils.chat(config.getString("FlyCommand.flying_disabled")));
								sender.sendMessage(
										MessageUtils.chat(config.getString("FlyCommand.flying_target_disabled")
												.replaceAll("<target>", target.getDisplayName())));
								return true;
							} else {
								target.setAllowFlight(true);
								target.setFlying(true);
								target.sendMessage(MessageUtils.chat(config.getString("FlyCommand.flying_enabled")));
								sender.sendMessage(
										MessageUtils.chat(config.getString("FlyCommand.flying_target_enabled")
												.replaceAll("<target>", target.getDisplayName())));
							}
					} else {
						sender.sendMessage(MessageUtils.chat(config.getString("FlyCommand.no_perm_message")));
					}
				} else if (args.length > 1) {
					sender.sendMessage(MessageUtils.chat(config.getString("too_many_args_error")));
					return false;
				}
			} else
				MessageUtils.noPermissionError(sender);
		}
		return false;
	}

}
