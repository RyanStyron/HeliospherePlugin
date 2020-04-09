package mc.rysty.heliosphereplugin.commands.inventory;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class EnderChestCommand implements CommandExecutor {

	HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	FileConfiguration config = plugin.getConfig();

	public EnderChestCommand(HelioSpherePlugin plugin) {
		plugin.getCommand("echest").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("echest")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(MessageUtils.chat(config.getString("console_error_message")));
				return false;
			}
			Player p = (Player) sender;

			if (p.hasPermission("hs.echest")) {
				if (args.length == 0) {
					Inventory echest = p.getEnderChest();
					p.openInventory(echest);
					p.sendMessage("Opening your ender chest...");

				} else if (args.length == 1) {
					if (p.hasPermission("hs.echest.other")) {
						Player t = Bukkit.getPlayer(args[0]);

						if (t == null) {
							p.sendMessage(MessageUtils.chat(config.getString("player_offline_message")));
						} else {
							String tName = t.getDisplayName();
							Inventory echest = t.getEnderChest();

							p.sendMessage(MessageUtils.chat("&fOpening " + tName + "'s ender chest..."));
							if ((!p.hasPermission("hs.echest.modify")
									|| (t.hasPermission("hs.echest.preventmodify")))) {
								// p.sendMessage(ChatColor.RED + "You do not have permission to edit " + tName
								// + ChatColor.RED + "'s ender chest");
								p.openInventory(echest);
							} else if ((p.hasPermission("hs.echest.modify"))
									&& (!t.hasPermission("hs.echest.preventmodify"))) {
								p.openInventory(echest);
							}
						}
					} else {
						p.sendMessage(MessageUtils.chat(
								"&cYou do not have permission to access another player's ender chest! Correct usage: /echest"));
						return false;
					}
				} else if (args.length > 1) {
					p.sendMessage(MessageUtils.chat("&cToo many arguments were provided. Correct format: /echest <player>"));
					return false;
				}
			} else {
				sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
			}
		}
		return false;
	}

}
