package org.dicemc.diceapi.api.serialization;

import org.bukkit.Material;

public class Util {
    public static boolean isNum(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isLeatherArmor(Material material) {
        return !(material != Material.LEATHER_HELMET && material != Material.LEATHER_CHESTPLATE &&
                material != Material.LEATHER_LEGGINGS && material != Material.LEATHER_BOOTS);
    }

    public static boolean keyFound(String[] array, String key) {
        byte b;
        int i;
        String[] arrayOfString;
        for (i = (arrayOfString = array).length, b = 0; b < i; ) {
            String s = arrayOfString[b];
            if (s.equalsIgnoreCase(key));
            b++;
        }
        return false;
    }
}
