package mc.rysty.heliosphereplugin.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class AFK implements CommandExecutor, Listener {

	private HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	private FileConfiguration config = plugin.getConfig();

	public AFK(HelioSpherePlugin plugin) {
		plugin.getCommand("afk").setExecutor(this);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	private HashMap<Player, Integer> playerTimeMap = new HashMap<Player, Integer>();
	private HashMap<Player, Boolean> playerAfkMessageSent = new HashMap<Player, Boolean>();
	private List<Player> playerAfkList = new ArrayList<Player>();

	private String afkDisabledMessage = MessageUtils.chat(config.getString("AFK.afk_disabled"));
	private String afkEnabledMessage = MessageUtils.chat(config.getString("AFK.afk_enabled"));

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("afk")) {
			if (sender.hasPermission("hs.afk")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					String displayName = player.getDisplayName();

					if (args.length == 0) {
						if (!playerAfkList.contains(player)) {
							playerAfkList.add(player);
							Bukkit.broadcastMessage(afkEnabledMessage.replaceAll("<player>", displayName));
						}
						/*
						 * An else statement is not required here because it is already handled by the
						 * onPlayerCommandPreprocess(PlayerCommandPreprocessEvent) method.
						 */
					} else
						MessageUtils.argumentError(sender, "/afk");
				} else
					MessageUtils.consoleError();
			} else
				MessageUtils.noPermissionError(sender);
		}
		return false;
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		if (playerAfkList.contains(player))
			playerAfkList.remove(player);
		playerTimeMap.put(player, 0);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		playerTimeMap.put(player, 0);
		playerAfkMessageSent.put(player, false);

		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (player.isOnline()) {
					playerTimeMap.put(player, playerTimeMap.get(player) + 1);

					if (playerTimeMap.get(player) >= 5) {
						playerAfkList.add(player);

						if (!playerAfkMessageSent.get(player)) {
							Bukkit.broadcastMessage(afkEnabledMessage.replaceAll("<player>", player.getDisplayName()));
							playerAfkMessageSent.put(player, true);
						}
					}
				}
			}
		}, 0, 1200);
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		removePlayerFromAfkList(event.getPlayer());
	}

	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		removePlayerFromAfkList(event.getPlayer());
	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		removePlayerFromAfkList(event.getPlayer());
	}

	private void removePlayerFromAfkList(Player player) {
		playerTimeMap.put(player, 0);
		playerAfkMessageSent.put(player, false);

		if (playerAfkList.contains(player)) {
			playerAfkList.remove(player);
			Bukkit.broadcastMessage(afkDisabledMessage.replaceAll("<player>", player.getDisplayName()));
		}
	}
}
