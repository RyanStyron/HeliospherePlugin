package mc.rysty.heliosphereplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MaterialUtils;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class CommandHat implements CommandExecutor {

    public CommandHat(HelioSpherePlugin plugin) {
        plugin.getCommand("hat").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("hat")) {
            if (sender.hasPermission("hs.hat")) {
                Player target = null;

                if (args.length <= 1) {
                    if (args.length > 0) {
                        if (sender.hasPermission("hs.hat.other"))
                            target = Bukkit.getPlayer(args[0]);
                        else
                            MessageUtils.argumentError(sender, "/hat");
                    } else if (sender instanceof Player)
                        target = (Player) sender;

                    if (target == null)
                        MessageUtils.validPlayerError(sender);
                    else {
                        PlayerInventory inventory = target.getInventory();
                        ItemStack mainHandItem = inventory.getItemInMainHand();

                        if (!MaterialUtils.isAir(mainHandItem.getType())) {
                            if (inventory.getHelmet() != null)
                                inventory.addItem(inventory.getHelmet());
                            inventory.removeItem(mainHandItem);
                            inventory.setHelmet(mainHandItem);
                            target.updateInventory();
                            if (target != sender)
                                MessageUtils.configStringMessage(sender, "CommandHat.hat-sender-message", "<player>",
                                        target.getName());
                            MessageUtils.configStringMessage(target, "CommandHat.hat-message");
                        } else
                            MessageUtils.configStringMessage(sender, "CommandHat.hat-error");
                    }
                } else
                    MessageUtils.argumentError(sender, "/hat [player]");
            } else
                MessageUtils.noPermissionError(sender);
        }
        return false;
    }
}