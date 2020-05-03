package mc.rysty.heliosphereplugin.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import mc.rysty.heliosphereplugin.HelioSpherePlugin;
import mc.rysty.heliosphereplugin.utils.MessageUtils;

public class SignColors implements Listener {

    public SignColors(HelioSpherePlugin plugin) {
       plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        String[] lines = event.getLines();

        for (int i = 0; i <= 3; i++)
            event.setLine(i, MessageUtils.chat(lines[i]));
    }
}