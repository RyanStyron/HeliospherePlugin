package mc.rysty.heliosphereplugin.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.SettingsManager;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class Invincible implements CommandExecutor {

	HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	FileConfiguration config = plugin.getConfig();
	SettingsManager settings = SettingsManager.getInstance();
	FileConfiguration data = settings.getData();

	public Invincible(HelioSpherePlugin plugin) {
		plugin.getCommand("invincible").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("invincible")) {
			if (sender.hasPermission("hs.invincible")) {
				if (args.length == 0) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						UUID playerId = p.getUniqueId();

						if (data.getString("users." + playerId + ".invincibility") != null) {
							p.setInvulnerable(false);
							p.removePotionEffect(PotionEffectType.SATURATION);
							p.sendMessage(MessageUtils.chat(config.getString("InvincibleCommand.invc_disabled")));
							data.set("users." + playerId + ".invincibility", null);
							settings.saveData();
							return true;
						} else if (data.getString("users." + playerId + ".invincibility") == null) {
							p.setInvulnerable(true);
							p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 100000, 255, false, false));
							p.sendMessage(MessageUtils.chat(config.getString("InvincibleCommand.invc_enabled")));
							data.set("users." + playerId + ".invincibility", "enabled");
							settings.saveData();
							return true;
						}
					} else {
						sender.sendMessage(
								MessageUtils.chat(config.getString("InvincibleCommand.player_specify_message")));
					}
				} else if (args.length == 1) {
					if (sender.hasPermission("hs.invincible.other")) {
						Player t = Bukkit.getPlayer(args[0]);

						if (t == null) {
							sender.sendMessage(MessageUtils.chat(config.getString("player_offline_message")));
						}
						UUID targetId = t.getUniqueId();

						if (data.getString("users." + targetId + ".invincibility") != null) {
							t.setInvulnerable(false);
							t.removePotionEffect(PotionEffectType.SATURATION);
							t.sendMessage(MessageUtils.chat(config.getString("InvincibleCommand.invc_disabled")));
							sender.sendMessage(
									MessageUtils.chat(config.getString("InvincibleCommand.invc_target_disabled")
											.replaceAll("<target>", t.getDisplayName())));
							data.set("users." + targetId + ".invincibility", null);
							settings.saveData();
							return true;
						} else if (data.getString("users." + targetId + ".invincibility") == null) {
							t.setInvulnerable(true);
							t.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 100000, 255, false, false));
							t.sendMessage(MessageUtils.chat(config.getString("InvincibleCommand.invc_enabled")));
							sender.sendMessage(
									MessageUtils.chat(config.getString("InvincibleCommand.invc_target_enabled")
											.replaceAll("<target>", t.getDisplayName())));
							data.set("users." + targetId + ".invincibility", "enabled");
							settings.saveData();
							return true;
						}
					} else {
						sender.sendMessage(MessageUtils.chat(config.getString("InvincibleCommand.no_perm_message")));
					}
				} else if (args.length > 1) {
					sender.sendMessage(MessageUtils.chat(config.getString("InvincibleCommand.too_many_args_message")));
					return false;
				}
			} else {
				sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
			}
		}
		return false;
	}

}
