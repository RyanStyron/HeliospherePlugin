package mc.rysty.heliosphereplugin.commands.inventory;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class InvseeCommand implements CommandExecutor {

	HelioSpherePlugin plugin = HelioSpherePlugin.getInstance();
	FileConfiguration config = plugin.getConfig();

	public InvseeCommand(HelioSpherePlugin plugin) {
		plugin.getCommand("invsee").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("invsee")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(MessageUtils.chat(config.getString("console_error_message")));
				return false;
			}
			Player p = (Player) sender;

			if (p.hasPermission("hs.invsee")) {
				if (args.length == 1) {
					Player t = Bukkit.getPlayer(args[0]);

					if (t == null) {
						p.sendMessage(MessageUtils.chat(config.getString("player_offline_message")));
					} else {
						PlayerInventory inv = t.getInventory();
						String tName = t.getDisplayName();

						p.sendMessage(MessageUtils.chat(config.getString("InvSeeCommand.inv_opened_message").replaceAll("<player>", tName)));
						if ((!p.hasPermission("hs.invsee.modify") || (t.hasPermission("hs.invsee.preventmodify")))) {
							//p.sendMessage(ChatColor.RED + "You do not have permission to edit " + tName +  ChatColor.RED +  "'s inventory");
							p.openInventory(inv);
						} else if ((p.hasPermission("hs.invsee.modify")) && (!t.hasPermission("hs.invsee.preventmodify"))) {
							p.openInventory(inv);
						} 
					}
				} else {
					p.sendMessage(MessageUtils.chat(config.getString("InvSeeCommand.argument_error")));
					return false;
				}
			} else {
				sender.sendMessage(MessageUtils.chat(config.getString("no_perm_message")));
			}
		}
		return false;
	}

}
