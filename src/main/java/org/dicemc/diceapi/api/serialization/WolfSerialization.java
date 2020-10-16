package org.dicemc.diceapi.api.serialization;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wolf;
import org.dicemc.diceapi.json.JSONException;
import org.dicemc.diceapi.json.JSONObject;

import java.io.IOException;

public class WolfSerialization {
    public static JSONObject serializeWolf(Wolf wolf) {
        try {
            JSONObject root = LivingEntitySerialization.serializeEntity((LivingEntity)wolf);
            if (shouldSerialize("collar-color"))
                root.put("collar-color", ColorSerialization.serializeColor(wolf.getCollarColor().getColor()));
            return root;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String serializeWolfAsString(Wolf wolf) {
        return serializeWolfAsString(wolf, false);
    }

    public static String serializeWolfAsString(Wolf wolf, boolean pretty) {
        return serializeWolfAsString(wolf, pretty, 5);
    }

    public static String serializeWolfAsString(Wolf wolf, boolean pretty, int indentFactor) {
        try {
            if (pretty)
                return serializeWolf(wolf).toString(indentFactor);
            return serializeWolf(wolf).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Wolf spawnWolf(Location location, String stats) {
        try {
            return spawnWolf(location, new JSONObject(stats));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Wolf spawnWolf(Location location, JSONObject stats) {
        try {
            Wolf wolf = (Wolf)LivingEntitySerialization.spawnEntity(location, stats);
            if (stats.has("collar-color"))
                wolf.setCollarColor(DyeColor.getByColor(ColorSerialization.getColor(stats.getString("collar-color"))));
            return wolf;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean shouldSerialize(String key) {
        return SerializationConfig.getShouldSerialize("wolf." + key);
    }
}
