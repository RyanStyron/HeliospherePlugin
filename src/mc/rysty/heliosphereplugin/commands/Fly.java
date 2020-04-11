package mc.rysty.heliosphereplugin.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;
import mc.rysty.heliosphereplugin.utils.SettingsManager;

public class Fly implements CommandExecutor, Listener {

	private SettingsManager dataFileManager = SettingsManager.getInstance();
	private FileConfiguration dataFile = dataFileManager.getData();

	public Fly(HelioSpherePlugin plugin) {
		plugin.getCommand("fly").setExecutor(this);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("fly")) {
			if (sender.hasPermission("hs.fly")) {
				Player target = null;

				if (args.length > 0)
					target = Bukkit.getPlayer(args[0]);
				else if (sender instanceof Player)
					target = (Player) sender;

				if (target == null)
					MessageUtils.validPlayerError(sender);
				else {
					String displayName = target.getDisplayName();
					UUID targetId = target.getUniqueId();
					String dataFileFlyString = dataFile.getString("users." + targetId + ".fly");

					if (dataFileFlyString == null) {
						dataFile.set("users." + targetId + ".fly", true);
						target.setAllowFlight(true);
						target.setFlying(true);
					} else {
						dataFile.set("users." + targetId + ".fly", null);
						target.setAllowFlight(false);
						target.setFlying(false);
					}
					dataFileManager.saveData();

					MessageUtils.configStringMessage(sender,
							!target.isFlying() ? "FlyCommand.fly-disabled-message" : "FlyCommand.fly-enabled-message",
							"<player>", displayName);
					if (target != sender)
						MessageUtils.configStringMessage(target,
								!target.isFlying() ? "FlyCommand.fly-disabled-player-message"
										: "FlyCommand.fly-enabled-player-message");
				}
			} else
				MessageUtils.noPermissionError(sender);
		}
		return false;
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		disablePlayerFlight(event.getPlayer());
	}

	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		disablePlayerFlight(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		disablePlayerFlight(((OfflinePlayer) event).getPlayer());
	}

	private void disablePlayerFlight(Player player) {
		UUID playerId = player.getUniqueId();
		String dataFileFlyString = dataFile.getString("users." + playerId + ".fly");

		if (dataFileFlyString != null)
			dataFile.set("users." + playerId + ".fly", null);
	}
}
