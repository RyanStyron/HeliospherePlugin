package mc.rysty.heliosphereplugin.commands.inventory;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class InvseeCommand implements CommandExecutor, Listener {

	public InvseeCommand(HelioSpherePlugin plugin) {
		plugin.getCommand("invsee").setExecutor(this);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("invsee")) {
			if (sender.hasPermission("hs.staff")) {
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
							Inventory targetInventory;
							targetInventory = Bukkit.createInventory(player, 36, target.getName() + "'s Inventory");
							targetInventory.setContents(target.getInventory().getStorageContents());

							player.openInventory(targetInventory);

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
		if (event.getInventory() == null)
			return;
		Inventory inventory = event.getInventory();

		if (event.getView() == null)
			return;
		InventoryView view = event.getView();

		if (view.getTitle() == null)
			return;
		String title = view.getTitle();

		if (title.contains("'s Inventory")) {
			String targetNameString = title.replace("'s Inventory", "");
			Player target = Bukkit.getPlayer(targetNameString);
			Player player = (Player) event.getWhoClicked();

			target.getInventory().setContents(inventory.getContents());
			target.updateInventory();
		}
	}
}
