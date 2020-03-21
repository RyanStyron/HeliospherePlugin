package mc.rysty.heliosphereplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class See implements CommandExecutor {
	
	HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	FileConfiguration config = plugin.getConfig();

	public See(HelioSpherePlugin plugin) {
		plugin.getCommand("see").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		if (sender.hasPermission("hs.see")) {
			if (args.length == 1) {
				Player targetPlayer = Bukkit.getPlayer(args[0]);

				if (targetPlayer == null) {
					sender.sendMessage(MessageUtils.chat(config.getString("player_offline_message")));
				}
				if (targetPlayer.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
					targetPlayer.removePotionEffect(PotionEffectType.NIGHT_VISION);
					targetPlayer.sendMessage(
							MessageUtils.chat(config.getString("SeeCommand.see_message").replaceAll("<toggle>", "disabled")));
					sender.sendMessage(MessageUtils.chat(config.getString("SeeCommand.see_other_message")
							.replaceAll("<toggle>", "Disabled").replaceAll("<target>", targetPlayer.getName())));
					return true;
				}
				if (!targetPlayer.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
					targetPlayer.addPotionEffect(
							new PotionEffect(PotionEffectType.NIGHT_VISION, 100000, 255, false, false));
					targetPlayer.sendMessage(
							MessageUtils.chat(config.getString("SeeCommand.see_message").replaceAll("<toggle>", "enabled")));
					sender.sendMessage(MessageUtils.chat(config.getString("SeeCommand.see_other_message")
							.replaceAll("<toggle>", "Enabled").replaceAll("<target>", targetPlayer.getName())));
					return true;
				}
			} else if (args.length == 0) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(MessageUtils.chat(config.getString("console_player_error_message")));
					return true;
				}
				Player player = (Player) sender;

				if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
					player.removePotionEffect(PotionEffectType.NIGHT_VISION);
					player.sendMessage(
							MessageUtils.chat(config.getString("SeeCommand.see_message").replaceAll("<toggle>", "disabled")));
					return true;
				}
				if (!player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 100000, 255, false, false));
					player.sendMessage(
							MessageUtils.chat(config.getString("SeeCommand.see_message").replaceAll("<toggle>", "enabled")));
					return true;
				}
			} else if (args.length > 1) {
				sender.sendMessage(MessageUtils.chat(config.getString("too_many_args_error")));
				return false;
			}
		} else {
			sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
			return false;
		}
		return false;
	}
}
