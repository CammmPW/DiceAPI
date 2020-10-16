package org.dicemc.diceapi;

import org.bukkit.plugin.java.JavaPlugin;

public class DiceAPI extends JavaPlugin {
    @Override
    public void onDisable() {
        getLogger().info("Disabled");
    }

    @Override
    public void onEnable() {
        getLogger().info("Enabled");
    }
}
