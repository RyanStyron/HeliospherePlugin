package mc.rysty.heliosphereplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MaterialUtils;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class CommandRename implements CommandExecutor {

    public CommandRename(HelioSpherePlugin plugin) {
        plugin.getCommand("rename").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("rename")) {
            if (sender.hasPermission("hs.rename")) {
                if (sender instanceof Player) {
                    if (args.length > 0) {
                        Player player = (Player) sender;
                        String name = "";

                        for (int i = 0; i < args.length; i++)
                            name += args[i] + " ";

                        PlayerInventory inventory = player.getInventory();
                        ItemStack item = inventory.getItemInMainHand();
                        ItemMeta itemMeta = item.getItemMeta();

                        if (!MaterialUtils.isAir(item.getType())) {
                            itemMeta.setDisplayName(MessageUtils.chat(name));
                            item.setItemMeta(itemMeta);
                            inventory.setItemInMainHand(item);
                            player.updateInventory();
                            MessageUtils.configStringMessage(sender, "CommandRename.rename-message");
                        } else
                            MessageUtils.configStringMessage(sender, "CommandRename.rename-error");
                    } else
                        MessageUtils.argumentError(sender, "/rename <name>");
                } else
                    MessageUtils.consoleError();
            } else
                MessageUtils.noPermissionError(sender);
        }
        return false;
    }
}