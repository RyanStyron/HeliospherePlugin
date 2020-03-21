package mc.rysty.heliosphereplugin.commands.gamemode;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class GMSP implements CommandExecutor {

	HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	FileConfiguration config = plugin.getConfig();

	public GMSP(HelioSpherePlugin plugin) {
		plugin.getCommand("gmsp").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("gmsp")) {
			if (sender.hasPermission("hs.gamemode.spectator")) {
				if (args.length == 0) {
					if (!(sender instanceof Player)) {
						sender.sendMessage(MessageUtils.chat(config.getString("console_player_error_message")));
						return true;
					}
					Player p = (Player) sender;
					if (!p.getGameMode().equals(GameMode.SPECTATOR)) {
						p.setGameMode(GameMode.SPECTATOR);
						p.sendMessage(MessageUtils.chat(config.getString("GamemodeCommand.gmsp")));
						return true;
					} else {
						p.sendMessage(MessageUtils.chat(config.getString("GamemodeCommand.current_gamemode_error")));
					}
				} else {
					if (args.length == 1) {
						if (sender.hasPermission("hs.gamemode.other")) {
							Player t = Bukkit.getPlayer(args[0]);

							if (t == null) {
								sender.sendMessage(MessageUtils.chat(config.getString("player_offline_message")));
							} else {
								String tName = t.getName();

								if (t.getGameMode() != GameMode.SPECTATOR) {
									t.setGameMode(GameMode.SPECTATOR);
									sender.sendMessage(MessageUtils.chat(config.getString("GamemodeCommand.gm_other_message"))
											.replaceAll("<player>", tName).replaceAll("<gamemode>", "Spectator"));
								} else {
									sender.sendMessage(MessageUtils.chat(
											config.getString("GamemodeCommand.other_player_current_gamemode_error")
													.replaceAll("<target>", tName)));
								}
							}
						} else {
							sender.sendMessage(MessageUtils.chat(config.getString("GamemodeCommand.current_gamemode_error")));
						}
					}
				}
			} else {
				sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
			}
		}
		return false;
	}
}
