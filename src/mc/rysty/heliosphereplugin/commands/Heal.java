package mc.rysty.heliosphereplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class Heal implements CommandExecutor {

	HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	FileConfiguration config = plugin.getConfig();

	public Heal(HelioSpherePlugin plugin) {
		plugin.getCommand("heal").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("hs.heal")) {
			if (args.length == 0) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(MessageUtils.chat(config.getString("console_player_error_message")));
					return true;
				}
				Player p = (Player) sender;

				p.setHealth(20);
				p.setFoodLevel(20);
				p.sendMessage(MessageUtils.chat(config.getString("HealCommand.heal")));
				return true;
			} else if (args.length == 1) {
				Player t = Bukkit.getPlayer(args[0]);

				if (t == null) {
					sender.sendMessage(MessageUtils.chat(config.getString("player_offline_message")));
				}
				t.setHealth(20);
				t.setFoodLevel(20);
				sender.sendMessage(MessageUtils.chat(
						plugin.getConfig().getString("HealCommand.heal_other").replaceAll("<target>", t.getName())));
				return true;
			} else if (args.length > 1) {
				sender.sendMessage(MessageUtils.chat(config.getString("too_many_args_error")));
				return false;
			}
		} else {
			sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
		}
		return false;
	}

}
