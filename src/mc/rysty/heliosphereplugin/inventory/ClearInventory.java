package mc.rysty.heliosphereplugin.inventory;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class ClearInventory implements CommandExecutor {

	private HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	private FileConfiguration config = plugin.getConfig();

	public ClearInventory(HelioSpherePlugin plugin) {
		plugin.getCommand("clear").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("clear")) {
			if (sender.hasPermission("hs.clearinventory")) {
				Player target = null;

				if (args.length > 0)
					target = Bukkit.getPlayer(args[0]);
				else if (sender instanceof Player)
					target = (Player) sender;

				if (target == null)
					MessageUtils.message(sender, config.getString("ClearCommand.specified_player_error"));

				PlayerInventory inventory = target.getInventory();
				ItemStack[] inventoryContents = inventory.getContents();

				if (inventoryContents != null) {
					inventory.clear();
					MessageUtils.message(sender, config.getString("ClearCommand.clear_message").replace("<player>",
							target.getDisplayName()));
				} else {
					MessageUtils.message(sender,
							config.getString("ClearCommand.clear_error").replace("<player>", target.getDisplayName()));
				}
			} else {
				MessageUtils.message(sender, config.getString("no_perm_message"));
			}
		}
		return false;
	}
}
