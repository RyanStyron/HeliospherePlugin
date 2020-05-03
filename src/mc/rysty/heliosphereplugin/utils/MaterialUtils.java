package mc.rysty.heliosphereplugin.utils;

import org.bukkit.Material;

public class MaterialUtils {

    public static boolean isAir(Material material) {
        return anyEquals(material, Material.AIR, Material.CAVE_AIR, Material.VOID_AIR);
    }

    @SuppressWarnings("all")
    public static <T> boolean anyEquals(T object, T... equals) {
        for (T equalsObject : equals)
            if (object.equals(equalsObject))
                return true;
        return false;
    }
}