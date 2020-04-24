package mc.rysty.heliosphereplugin.chat;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;
import mc.rysty.heliosphereplugin.utils.SettingsManager;

public class CommandMute implements CommandExecutor, Listener {

    private SettingsManager settings = SettingsManager.getInstance();
    private FileConfiguration data = settings.getData();

    public CommandMute(HelioSpherePlugin plugin) {
        plugin.getCommand("mute").setExecutor(this);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mute")) {
            if (sender.hasPermission("hs.mute")) {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);

                    if (target == null)
                        MessageUtils.validPlayerError(sender);
                    else {
                        UUID targetId = target.getUniqueId();

                        if (data.getString("users." + targetId + ".muted") == null)
                            data.set("users." + targetId + ".muted", true);
                        else
                            data.set("users." + targetId + ".muted", null);
                        settings.saveData();

                        MessageUtils.configStringMessage(sender, "CommandMute.mute-message", "<player>",
                                target.getDisplayName(), "<muted>",
                                data.getString("users." + targetId + ".muted") == null ? "no longer" : "now");
                        MessageUtils.configStringMessage(target, "CommandMute.mute-message-target", "<muted>",
                                data.getString("users." + targetId + ".muted") == null ? "no longer" : "now");
                    }
                } else
                    MessageUtils.argumentError(sender, "/mute <player>");
            } else
                MessageUtils.noPermissionError(sender);
        }
        return false;
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (data.getString("users." + playerId + ".muted") != null) {
            event.setCancelled(true);
            MessageUtils.configStringMessage(player, "CommandMute.chat-error");
        }
    }
}