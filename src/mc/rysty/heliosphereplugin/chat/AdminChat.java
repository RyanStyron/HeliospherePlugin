package mc.rysty.heliosphereplugin.chat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class AdminChat implements Listener {

    private FileConfiguration config = HelioSpherePlugin.getInstance().getConfig();

    public AdminChat(HelioSpherePlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String displayName = player.getDisplayName();

        if (message.startsWith("@ac")) {
            message = message.replace("@ac", "");

            event.setCancelled(true);
            MessageUtils.broadcastMessage(config.getString("StaffChat.admin-chat-message").replaceAll("<msg>", message)
                    .replaceAll("<player>", displayName), "hs.adminchat");
        }
    }
}
