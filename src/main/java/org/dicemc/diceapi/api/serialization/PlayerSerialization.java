package org.dicemc.diceapi.api.serialization;

import org.bukkit.entity.Player;
import org.dicemc.diceapi.json.JSONException;
import org.dicemc.diceapi.json.JSONObject;

import java.io.IOException;

public class PlayerSerialization {
    public static JSONObject serializePlayer(Player player) {
        try {
            JSONObject root = new JSONObject();
            if (SerializationConfig.getShouldSerialize("player-ender-chest"))
                root.put("ender-chest", InventorySerialization.serializeInventory(player.getEnderChest()));
            if (SerializationConfig.getShouldSerialize("player.inventory"))
                root.put("inventory", InventorySerialization.serializePlayerInventory(player.getInventory()));
            if (SerializationConfig.getShouldSerialize("player.stats"))
                root.put("stats", PlayerStatsSerialization.serializePlayerStats(player));
            return root;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String serializePlayerAsString(Player player) {
        return serializePlayerAsString(player, false);
    }

    public static String serializePlayerAsString(Player player, boolean pretty) {
        return serializePlayerAsString(player, pretty, 5);
    }

    public static String serializePlayerAsString(Player player, boolean pretty, int indentFactor) {
        try {
            if (pretty)
                return serializePlayer(player).toString(indentFactor);
            return serializePlayer(player).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setPlayer(String meta, Player player) {
        try {
            setPlayer(new JSONObject(meta), player);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setPlayer(JSONObject meta, Player player) {
        try {
            if (meta.has("ender-chest"))
                InventorySerialization.setInventory(player.getEnderChest(), meta.getJSONArray("ender-chest"));
            if (meta.has("inventory"))
                InventorySerialization.setPlayerInventory(player, meta.getJSONObject("inventory"));
            if (meta.has("stats"))
                PlayerStatsSerialization.applyPlayerStats(player, meta.getJSONObject("stats"));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean shouldSerialize(String key) {
        return SerializationConfig.getShouldSerialize("player." + key);
    }
}
