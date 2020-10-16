package org.dicemc.diceapi;

import org.bukkit.plugin.java.JavaPlugin;

public class DiceAPI extends JavaPlugin {
    public static DiceAPI instance;

    @Override
    public void onDisable() {
        getLogger().info("Disabled");
    }

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Enabled");
    }
}
