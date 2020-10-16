package org.dicemc.diceapi.api.serialization;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerializationConfig {
    private static YamlConfiguration config;

    public static File getDataFolder() {
        File pluginFolder = Bukkit.getServer().getPluginManager().getPlugins()[0].getDataFolder();
        return new File(pluginFolder.getParentFile() + "/TacoSerialization/");
    }

    public static File getConfigFile() {
        return new File(getDataFolder() + "/config.yml");
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(getConfigFile());
        setDefaults();
        save();
        Logger.getLogger("Minecraft").log(Level.INFO, "[TacoSerialization] Config reloaded");
    }

    public static void save() {
        try {
            config.save(getConfigFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setDefaults() {
        addDefault("horse.color", Boolean.valueOf(true));
        addDefault("horse.inventory", Boolean.valueOf(true));
        addDefault("horse.jump-stength", Boolean.valueOf(true));
        addDefault("horse.style", Boolean.valueOf(true));
        addDefault("horse.variant", Boolean.valueOf(true));
        addDefault("living-entity.age", Boolean.valueOf(true));
        addDefault("living-entity.health", Boolean.valueOf(true));
        addDefault("living-entity.name", Boolean.valueOf(true));
        addDefault("living-entity.potion-effects", Boolean.valueOf(true));
        addDefault("ocelot.type", Boolean.valueOf(true));
        addDefault("player.ender-chest", Boolean.valueOf(true));
        addDefault("player.inventory", Boolean.valueOf(true));
        addDefault("player.stats", Boolean.valueOf(true));
        addDefault("player-stats.can-fly", Boolean.valueOf(true));
        addDefault("player-stats.display-name", Boolean.valueOf(true));
        addDefault("player-stats.exhaustion", Boolean.valueOf(true));
        addDefault("player-stats.exp", Boolean.valueOf(true));
        addDefault("player-stats.food", Boolean.valueOf(true));
        addDefault("player-stats.flying", Boolean.valueOf(true));
        addDefault("player-stats.health", Boolean.valueOf(true));
        addDefault("player-stats.level", Boolean.valueOf(true));
        addDefault("player-stats.potion-effects", Boolean.valueOf(true));
        addDefault("player-stats.saturation", Boolean.valueOf(true));
        addDefault("wolf.collar-color", Boolean.valueOf(true));
    }

    public static void addDefault(String path, Object value) {
        if (!getConfig().contains(path))
            getConfig().set(path, value);
    }

    private static YamlConfiguration getConfig() {
        if (config == null)
            reload();
        return config;
    }

    public static boolean getShouldSerialize(String path) {
        return getConfig().getBoolean(path);
    }
}
