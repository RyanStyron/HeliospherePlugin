package mc.rysty.heliosphereplugin.commands.inventory;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class InvseeCommand implements CommandExecutor, Listener {

	public InvseeCommand(HelioSpherePlugin plugin) {
		plugin.getCommand("invsee").setExecutor(this);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	private HashMap<Player, Player> playerInvseeMap = new HashMap<Player, Player>();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("invsee")) {
			if (sender.hasPermission("hs.invsee")) {
				if (sender instanceof Player) {
					if (args.length < 2) {
						Player player = (Player) sender;
						Player target = null;

						if (args.length == 1)
							target = Bukkit.getPlayer(args[0]);
						else
							target = player;

						if (target == null)
							MessageUtils.validPlayerError(sender);
						else {
							player.openInventory(target.getInventory());
							playerInvseeMap.put(player, target);

							if (target != player)
								MessageUtils.configStringMessage(sender, "InvSeeCommand.invsee-other-message",
										"<player>", target.getDisplayName());
							else
								MessageUtils.configStringMessage(sender, "InvSeeCommand.invsee-message");
						}
					} else
						MessageUtils.configStringMessage(sender, "InvSeeCommand.argument-error");
				} else
					MessageUtils.consoleError();
			} else
				MessageUtils.noPermissionError(sender);
		}
		return false;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		HumanEntity player = event.getWhoClicked();

		if (playerInvseeMap.get(player) != null) {
			Player target = playerInvseeMap.get(player);

			if (target.hasPermission("hs.invsee.preventmodify"))
				event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		HumanEntity player = event.getPlayer();

		if (playerInvseeMap.get(player) != null)
			playerInvseeMap.remove(player);
	}
}
