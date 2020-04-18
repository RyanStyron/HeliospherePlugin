package mc.rysty.heliosphereplugin.commands.inventory;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class EnderChestCommand implements CommandExecutor {

	public EnderChestCommand(HelioSpherePlugin plugin) {
		plugin.getCommand("echest").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("echest")) {
			if (sender.hasPermission("hs.enderchest")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					Player target = null;

					if (args.length < 2) {
						if (args.length == 1) {
							if (sender.hasPermission("hs.enderchest.other") || Bukkit.getPlayer(args[0]) == player)
								target = Bukkit.getPlayer(args[0]);
							else {
								MessageUtils.configStringMessage(sender, "EnderchestCommand.permission-error");
								return false;
							}
						} else
							target = player;

						if (target == null)
							MessageUtils.validPlayerError(sender);
						else {
							Inventory targetEnderchest = target.getEnderChest();

							player.openInventory(targetEnderchest);
							if (target != player)
								MessageUtils.configStringMessage(sender, "EnderchestCommand.enderchest-other-message",
										"<player>", target.getDisplayName());
							else
								MessageUtils.configStringMessage(sender, "EnderchestCommand.enderchest-message");
						}
					} else
						MessageUtils.configStringMessage(sender, "EnderchestCommand.argument-error");
				} else
					MessageUtils.consoleError();
			} else
				MessageUtils.noPermissionError(sender);
		}
		return false;
	}
}
