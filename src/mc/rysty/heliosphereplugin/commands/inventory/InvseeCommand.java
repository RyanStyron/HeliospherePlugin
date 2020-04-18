package mc.rysty.heliosphereplugin.commands.inventory;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class InvseeCommand implements CommandExecutor {

	public InvseeCommand(HelioSpherePlugin plugin) {
		plugin.getCommand("invsee").setExecutor(this);
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
							targetInventory = Bukkit.createInventory(target, 45, target.getName() + "'s Inventory");
							targetInventory.setContents(target.getInventory().getContents());
							targetInventory.setContents(target.getInventory().getArmorContents());

							if (target.hasPermission("hs.admin") && !player.hasPermission("hs.admin")) {
								player.openInventory(targetInventory);
							} else
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
}
