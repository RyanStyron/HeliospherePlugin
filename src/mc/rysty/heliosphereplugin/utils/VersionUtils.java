package mc.rysty.heliosphereplugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import protocolsupport.api.ProtocolSupportAPI;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;

public class VersionUtils {

    private static PluginManager pluginManager = Bukkit.getPluginManager();
    private static boolean hasProtocolSupport = pluginManager.isPluginEnabled("ProtocolSupport");
    private static boolean hasViaVersion = pluginManager.isPluginEnabled("ViaVersion");

    public static boolean hasProtocolSupport() {
        return hasProtocolSupport;
    }

    public static boolean hasViaVersion() {
        return hasViaVersion;
    }

    public static String getServerVersion() {
        return Bukkit.getBukkitVersion().split("-")[0];
    }

    @SuppressWarnings("all")
    public static String getPlayerVersion(Player player) {
        if (hasProtocolSupport)
            return ProtocolSupportAPI.getConnection(player).getVersion().getName();

        if (hasViaVersion)
            return ProtocolVersion.getProtocol(Via.getAPI().getPlayerVersion(player)).getName(); 

        return getServerVersion();
    }

    public static String getOldest() {
        if (hasProtocolSupport)
            return "1.4.7";
        return getServerVersion();
    }

    public static String getLatest() {
        if (hasViaVersion) {
            for (ProtocolVersion version : ProtocolVersion.getProtocols()) {
                int highest = 0;
                int id = version.getId();

                if (ProtocolVersion.isRegistered(version.getId()) && id > highest)
                    highest = id;

                return ProtocolVersion.getProtocol(highest).getName();
            }
        }
        return getServerVersion();
    }

    public static boolean isBefore(Player player, String legacyVersion, boolean trueOnEqual) {
        String playerVersion = getPlayerVersion(player);

        if (legacyVersion.equals(playerVersion))
            return trueOnEqual;

        String[] legacySplit = legacyVersion.split("\\.");
        String[] playerSplit = playerVersion.split("\\.");
        int legacyLength = legacySplit.length;
        int playerLength = playerSplit.length;
        int length = MathUtils.getLargestInt(legacyLength, playerLength);

        for (int i = 0; i < length && (legacyLength & playerLength) >= i - 1; i++) {
            int legacy = Integer.valueOf(legacySplit[i]);
            int current = Integer.valueOf(playerSplit[i]);

            if (current < legacy)
                return false;
        }
        return true;
    }

    public static boolean isAfter(Player player, String futureVersion, boolean trueOnEqual) {
        String playerVersion = getPlayerVersion(player);

        if (futureVersion.equals(playerVersion))
            return trueOnEqual;

        String[] futureSplit = futureVersion.split("\\.");
        String[] playerSplit = playerVersion.split("\\.");
        int futureLength = futureSplit.length;
        int playerLength = playerSplit.length;
        int length = MathUtils.getLargestInt(futureLength, playerLength);

        for (int i = 0; i < length && (futureLength & playerLength) >= i - 1; i++) {
            int future = Integer.valueOf(futureSplit[i]);
            int current = Integer.valueOf(playerSplit[i]);

            if (current > future)
                return false;
        }
        return true;
    }
}