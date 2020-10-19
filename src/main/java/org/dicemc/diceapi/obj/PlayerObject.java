package org.dicemc.diceapi.obj;

import org.dicemc.diceapi.DiceAPI;
import org.dicemc.diceapi.api.serialization.InventorySerialization;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;

public class PlayerObject {
    public InventoryObject getInventoryAPI() {
        return new InventoryObject();
    }

    public ArrayList<Tameable> getPets(String playerName) {
        return new ArrayList<>(getTamedWolves(playerName));
    }

    public ArrayList<Wolf> getTamedWolves(String playerName) {
        ArrayList<Wolf> wolves = new ArrayList<>();
        for (World w : DiceAPI.instance.getServer().getWorlds()) {
            for (Wolf wolf : w.getEntitiesByClass(Wolf.class)) {
                if (wolf.isTamed() && wolf.getOwner() instanceof Player && (
                        (Player)wolf.getOwner()).getName().equalsIgnoreCase(playerName))
                    wolves.add(wolf);
            }
        }
        return wolves;
    }

    public void teleport(Player player, Entity entity) {
        teleport(player, entity, true);
    }

    public void teleport(Player player, Entity entity, boolean smoke) {
        teleport(player, entity.getLocation(), smoke);
    }

    public void teleport(Player player, Location location) {
        teleport(player, location, true);
    }

    public void teleport(Player player, Location location, boolean smoke) {
        if (smoke)
            DiceAPI.getEffectAPI().showSmoke(player.getLocation());
        player.teleport(location);
        if (smoke)
            DiceAPI.getEffectAPI().showSmoke(player.getLocation());
    }

    public void teleportToLastLocation(Player player) {
        teleportToLastLocation(player, true);
    }

    public void teleportToLastLocation(Player player, boolean smoke) {
        Location location = getLastLocation(player.getName());
        if (location != null) {
            teleport(player, getLastLocation(player.getName()), smoke);
        }
    }

    @Nullable
    public Location getLastLocation(String name) {
        File file = new File(DiceAPI.playerData + "/" + name + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String world = config.getString("last-location.world");
        if (world == null) return null;
        World w = DiceAPI.instance.getServer().getWorld(world);
        if (w == null) return null;
        double x = config.getDouble("last-location.x");
        double y = config.getDouble("last-location.y");
        double z = config.getDouble("last-location.z");
        DiceAPI.instance.chat.out(world + " " + x + " " + y + " " + z);
        return new Location(w, x, y, z);
    }

    public void saveLocation(String name, Location location) {
        File file = new File(DiceAPI.playerData + "/" + name + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("last-location.world", location.getWorld().getName());
        config.set("last-location.x", location.getX());
        config.set("last-location.y", location.getY());
        config.set("last-location.z", location.getZ());
        save(file, config);
    }

    public void setToLastGameMode(Player player) {
        GameMode gm = getLastGameMode(player.getName());
        player.setGameMode(gm);
    }

    public GameMode getLastGameMode(String name) {
        File file = new File(DiceAPI.playerData + "/" + name + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        return GameMode.valueOf(config.getString("last-gamemode"));
    }

    public void saveGameMode(String name, GameMode gameMode) {
        File file = new File(DiceAPI.playerData + "/" + name + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("last-gamemode", gameMode.toString());
        save(file, config);
    }

    public void savePlayerInventory(Player player) {
        savePlayerInventory(player.getInventory(), player.getName());
    }

    public void savePlayerInventory(PlayerInventory inventory, String name) {
        try {
            File invFile = new File(DiceAPI.playerData + "/inventories/" + name + "/inventory.json");
            File armorFile = new File(DiceAPI.playerData + "/inventories/" + name + "/armor.json");
            if (invFile.exists())
                invFile.delete();
            if (armorFile.exists())
                armorFile.delete();
            invFile.getParentFile().mkdirs();
            invFile.createNewFile();
            armorFile.createNewFile();
            String inv = InventorySerialization.serializeInventoryAsString((Inventory)inventory, true, 5);
            String armor = InventorySerialization.serializeInventoryAsString(inventory.getArmorContents(), true, 5);
            FileWriter invWriter = new FileWriter(invFile);
            FileWriter armorWriter = new FileWriter(armorFile);
            invWriter.append(inv);
            invWriter.close();
            armorWriter.append(armor);
            armorWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ItemStack[] getPlayerInventory(Player player) {
        return getPlayerInventory(player.getName());
    }

    public ItemStack[] getPlayerInventory(String name) {
        File invFile = new File(DiceAPI.playerData + "/inventories/" + name + "/inventory.json");
        return getItemsFromFile(invFile, 36);
    }

    public ItemStack[] getPlayerArmor(Player player) {
        return getPlayerArmor(player.getName());
    }

    public ItemStack[] getPlayerArmor(String name) {
        File armorFile = new File(DiceAPI.playerData + "/inventories/" + name + "/armor.json");
        return getItemsFromFile(armorFile, 4);
    }

    private ItemStack[] getItemsFromFile(File file, int size) {
        return InventorySerialization.getInventory(file, size);
    }

    public void restoreInventory(Player player) {
        String name = player.getName();
        ItemStack[] inv = getPlayerInventory(name);
        ItemStack[] armor = getPlayerArmor(name);
        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setArmorContents(armor);
        for (int i = 0; i < inv.length; i++) {
            ItemStack items = inv[i];
            if (items != null)
                inventory.setItem(i, items);
        }
    }

    private void save(File file, YamlConfiguration config) {
        try {
            file.getParentFile().mkdir();
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
