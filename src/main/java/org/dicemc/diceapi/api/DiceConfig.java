package org.dicemc.diceapi.api;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class DiceConfig {
    protected YamlConfiguration config;

    private final File file;

    public DiceConfig(File file) {
        this.file = file;
        reload();
    }

    protected abstract void setDefaults();

    public void addDefaultValue(String path, Object value) {
        if (!this.config.contains(path))
            if (value instanceof String) {
                this.config.set(path, value);
            } else {
                this.config.set(path, value);
            }
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
        setDefaults();
        save();
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String path) {
        return this.config.getString(path);
    }

    public List<String> getStringList(String path) {
        return this.config.getStringList(path);
    }

    public double getDouble(String path) {
        return this.config.getDouble(path);
    }

    public List<Double> getDoubleList(String path) {
        return this.config.getDoubleList(path);
    }

    public int getInt(String path) {
        return this.config.getInt(path);
    }

    public List<Integer> getIntList(String path) {
        return this.config.getIntegerList(path);
    }

    public boolean getBoolean(String path) {
        return this.config.getBoolean(path);
    }

    public List<Boolean> getBooleanList(String path) {
        return this.config.getBooleanList(path);
    }

    public void set(String path, Object value) {
        this.config.set(path, value);
        save();
    }

    public void setBoolean(String path, boolean value) {
        set(path, value);
        save();
    }

    public void setString(String path, String value) {
        set(path, value);
        save();
    }

    public void setInt(String path, int value) {
        set(path, value);
        save();
    }

    public void setDouble(String path, double value) {
        set(path, value);
        save();
    }

    public boolean contains(String path) {
        return this.config.contains(path);
    }

    public Map<String, Object> getMeta() {
        return this.config.getValues(true);
    }
}