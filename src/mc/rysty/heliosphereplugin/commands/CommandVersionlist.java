package mc.rysty.heliosphereplugin.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.ListUtils;
import mc.rysty.heliosphereplugin.utils.MessageBuilder;
import mc.rysty.heliosphereplugin.utils.MessageUtils;
import mc.rysty.heliosphereplugin.utils.VersionUtils;
import net.md_5.bungee.api.chat.HoverEvent;

public class CommandVersionlist implements CommandExecutor {

    public CommandVersionlist(HelioSpherePlugin plugin) {
        plugin.getCommand("versionlist").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("versionlist")) {
            Map<String, List<UUID>> versionlist = new HashMap<>();
            MessageBuilder message = new MessageBuilder();

            for (Player online : Bukkit.getOnlinePlayers()) {
                String version = VersionUtils.getPlayerVersion(online);
                List<UUID> players = versionlist.getOrDefault(version, new ArrayList<>());

                players.add(online.getUniqueId());
                versionlist.put(version, players);
            }

            for (Map.Entry<String, List<UUID>> entry : versionlist.entrySet()) {
                List<String> displaynames = new ArrayList<>();
                List<String> names = new ArrayList<>();

                for (UUID uuid : entry.getValue()) {
                    Player target = Bukkit.getPlayer(uuid);
                    displaynames.add(target.getDisplayName());
                    names.add(target.getName());
                }

                if (!message.isEmpty())
                    message.append("\n");

                message.append("&6&l(!)&e [&6" + entry.getKey() + "&e] " + ListUtils.fromList(displaynames, false, false))
                        .hoverEvent(HoverEvent.Action.SHOW_TEXT, "&6" + ListUtils.fromList(names, false, false));
            }

            if (message.isEmpty())
                MessageUtils.message(sender, "&4&l(!)&c There are no players currently online.");
            else
                MessageUtils.message(sender, message.build());
        }
        return false;
    }
}