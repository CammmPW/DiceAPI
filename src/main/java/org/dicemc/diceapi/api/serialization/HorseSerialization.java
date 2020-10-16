package org.dicemc.diceapi.api.serialization;

import org.bukkit.Location;
import org.bukkit.entity.Horse;
import org.dicemc.diceapi.json.JSONException;
import org.dicemc.diceapi.json.JSONObject;

import java.io.IOException;

public class HorseSerialization {
    public static JSONObject serializeHorse(Horse horse) {
        try {
            JSONObject root = LivingEntitySerialization.serializeEntity(horse);
            if (shouldSerialize("color"))
                root.put("color", horse.getColor().name());
            if (shouldSerialize("inventory"))
                root.put("inventory", InventorySerialization.serializeInventory(horse.getInventory()));
            if (shouldSerialize("jump-strength"))
                root.put("jump-strength", horse.getJumpStrength());
            if (shouldSerialize("style"))
                root.put("style", horse.getStyle());
            if (shouldSerialize("variant"))
                root.put("variant", horse.getVariant());
            return root;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String serializeHorseAsString(Horse horse) {
        return serializeHorseAsString(horse, false);
    }

    public static String serializeHorseAsString(Horse horse, boolean pretty) {
        return serializeHorseAsString(horse, pretty, 5);
    }

    public static String serializeHorseAsString(Horse horse, boolean pretty, int indentFactor) {
        try {
            if (pretty)
                return serializeHorse(horse).toString(indentFactor);
            return serializeHorse(horse).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Horse spawnHorse(Location location, String stats) {
        try {
            return spawnHorse(location, new JSONObject(stats));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Horse spawnHorse(Location location, JSONObject stats) {
        try {
            Horse horse = (Horse)LivingEntitySerialization.spawnEntity(location, stats);
            if (stats.has("color"))
                horse.setColor(Horse.Color.valueOf(stats.getString("color")));
            if (stats.has("jump-strength"))
                horse.setCustomName(stats.getString("name"));
            if (stats.has("style"))
                horse.setStyle(Horse.Style.valueOf(stats.getString("style")));
            if (stats.has("inventory"))
                PotionEffectSerialization.addPotionEffects(stats.getString("potion-effects"), horse);
            if (stats.has("variant"))
                horse.setVariant(Horse.Variant.valueOf(stats.getString("variant")));
            return horse;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean shouldSerialize(String key) {
        return SerializationConfig.getShouldSerialize("horse." + key);
    }
}
