package mc.rysty.heliosphereplugin.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.SettingsManager;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class ToggleBlockModify implements CommandExecutor, Listener {

	HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	FileConfiguration config = plugin.getConfig();
	SettingsManager settings = SettingsManager.getInstance();
	FileConfiguration data = settings.getData();

	public ToggleBlockModify(HelioSpherePlugin plugin) {
		plugin.getCommand("toggleblockmodify").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("toggleblockmodify")) {
			if (sender.hasPermission("hs.toggleblockmodify")) {
				String argumentError = MessageUtils.chat(config.getString("ToggleBlockModify.argument_error"));

				if (args.length < 1) {
					sender.sendMessage(argumentError);
				}
				if (args.length > 1) {
					sender.sendMessage(argumentError);
				}
				if (args.length == 1) {
					if (Bukkit.getPlayer(args[0]) == null) {
						sender.sendMessage(MessageUtils.chat(config.getString("player_offline_message")));
						return false;
					} else {
						Player player = Bukkit.getPlayer(args[0]);
						UUID playerId = player.getUniqueId();
						String displayName = player.getDisplayName();

						if (data.getString("users." + playerId + ".noblockmodify") == "true") {
							data.set("users." + playerId + ".noblockmodify", "false");
						} else {
							data.set("users." + playerId + ".noblockmodify", "true");
						}
						settings.saveData();

						if (data.getString("users." + playerId + ".noblockmodify") == "true") {
							sender.sendMessage(MessageUtils.chat(config.getString("ToggleBlockModify.toggle_enabled")
									.replaceAll("<player>", displayName)));
						} else {
							sender.sendMessage(MessageUtils.chat(config.getString("ToggleBlockModify.toggle_disabled")
									.replaceAll("<player>", displayName)));
						}
						return true;
					}
				}
			} else {
				sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
			}
		}
		return false;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player player = e.getPlayer();

		if (data.getString("users." + player.getUniqueId() + ".noblockmodify") == "true") {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player player = e.getPlayer();

		if (data.getString("users." + player.getUniqueId() + ".noblockmodify") == "true") {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent e) {
		Player player = e.getPlayer();

		if (data.getString("users." + player.getUniqueId() + ".noblockmodify") == "true") {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerBucketFill(PlayerBucketFillEvent e) {
		Player player = e.getPlayer();

		if (data.getString("users." + player.getUniqueId() + ".noblockmodify") == "true") {
			e.setCancelled(true);
		}
	}
}
