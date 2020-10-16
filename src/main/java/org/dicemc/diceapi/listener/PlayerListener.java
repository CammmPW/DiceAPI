package org.dicemc.diceapi.listener;

import org.dicemc.diceapi.DiceAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        DiceAPI.getPlayerAPI().saveLocation(event.getPlayer().getName(), event.getFrom());
    }

    @EventHandler(ignoreCancelled = true)
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        DiceAPI.getPlayerAPI().saveGameMode(player.getName(), player.getGameMode());
    }
}
