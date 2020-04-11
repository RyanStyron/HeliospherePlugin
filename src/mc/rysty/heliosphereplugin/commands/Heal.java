package mc.rysty.heliosphereplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class Heal implements CommandExecutor {

	public Heal(HelioSpherePlugin plugin) {
		plugin.getCommand("heal").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("heal")) {
			if (sender.hasPermission("hs.heal")) {
				Player target = null;

				if (args.length > 0)
					target = Bukkit.getPlayer(args[0]);
				else if (sender instanceof Player)
					target = (Player) sender;

				if (target == null)
					MessageUtils.validPlayerError(sender);
				else {
					String displayName = target.getDisplayName();
					@SuppressWarnings("all")
					double maxHealth = target.getMaxHealth();

					target.setHealth(maxHealth);
					target.setFoodLevel(20);

					if (target != sender)
						MessageUtils.configStringMessage(sender, "HealCommand.heal_other", "<target>", displayName);
					MessageUtils.configStringMessage(target, "HealCommand.heal");
				}
			} else
				MessageUtils.noPermissionError(sender);
		}
		return false;
	}
}