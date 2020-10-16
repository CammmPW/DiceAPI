package org.dicemc.diceapi.obj;

import org.bukkit.entity.Player;

public class PermObject {
    public boolean hasPermission(Player player, String perm) {
        if (perm == null || perm.isEmpty() || player.hasPermission(perm))
            return true;
        if (perm.contains("."))
            return hasOverridePerm(player, perm);
        return false;
    }

    private String getOverridePerm(String perm) {
        if (perm.matches(".*\\.\\*"))
            perm = perm.substring(0, perm.lastIndexOf('.'));
        if (!perm.contains("."))
            return perm;
        return String.valueOf(perm.substring(0, perm.lastIndexOf('.'))) + ".*";
    }

    private boolean hasOverridePerm(Player player, String perm) {
        String overridePerm = getOverridePerm(perm);
        if (player.hasPermission(overridePerm))
            return true;
        if (overridePerm.contains("."))
            return hasOverridePerm(player, overridePerm);
        return false;
    }
}
