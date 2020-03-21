package mc.rysty.heliosphereplugin.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.SettingsManager;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class Back implements Listener, CommandExecutor {

	HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	FileConfiguration config = plugin.getConfig();
	SettingsManager settings = SettingsManager.getInstance();
	FileConfiguration data = settings.getData();

	public Back(HelioSpherePlugin plugin) {
		plugin.getCommand("back").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("back")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				UUID playerId = player.getUniqueId();

				if (player.hasPermission("hs.back")) {
					if (data.getString("users." + playerId + ".lastlocation") != null) {
						World world = Bukkit.getWorld(data.getString("users." + playerId + ".lastlocation.world"));
						double locX = data.getDouble("users." + playerId + ".lastlocation.x");
						double locY = data.getDouble("users." + playerId + ".lastlocation.y");
						double locZ = data.getDouble("users." + playerId + ".lastlocation.z");

						player.teleport(new Location(world, locX, locY, locZ));
						player.sendMessage(MessageUtils.chat(config.getString("BackCommand.back_success_message")));
						return true;
					} else {
						player.sendMessage(MessageUtils.chat(config.getString("BackCommand.back_error_message")));
						return false;
					}
				} else {
					sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
					return false;
				}
			} else {
				sender.sendMessage(MessageUtils.chat(config.getString("console_error_message")));
				return false;
			}
		}
		return false;
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity().getPlayer();
		Location location = player.getLocation();

		saveLocation(player, location);
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		Location location = event.getFrom();

		saveLocation(player, location);
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Location location = player.getLocation();

		saveLocation(player, location);
	}

	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		Location location = player.getLocation();

		saveLocation(player, location);
	}

	/** This function saves the player's last location when called upon. */
	private void saveLocation(Player player, Location location) {
		UUID playerId = player.getUniqueId();

		if (player.hasPermission("hs.back")) {
			data.set("users." + playerId + ".lastlocation.world", location.getWorld().getName());
			data.set("users." + playerId + ".lastlocation.x", location.getX());
			data.set("users." + playerId + ".lastlocation.y", location.getY());
			data.set("users." + playerId + ".lastlocation.z", location.getZ());
			settings.saveData();
		}
	}
}
