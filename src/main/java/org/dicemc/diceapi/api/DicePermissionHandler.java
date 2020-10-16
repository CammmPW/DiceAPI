package org.dicemc.diceapi.api;

import org.bukkit.entity.Player;

public interface DicePermissionHandler {
    String getPrefix();

    boolean handlePerm(Player paramPlayer, String paramString);
}
