package org.dicemc.diceapi.api.serialization;

import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.dicemc.diceapi.json.JSONException;
import org.dicemc.diceapi.json.JSONObject;

import java.io.IOException;

public class LivingEntitySerialization {
    public static JSONObject serializeEntity(LivingEntity entity) {
        if (entity instanceof Player)
            return PlayerSerialization.serializePlayer((Player)entity);
        try {
            JSONObject root = new JSONObject();
            if (shouldSerialize("age") && entity instanceof Ageable)
                root.put("age", ((Ageable)entity).getAge());
            if (shouldSerialize("health"))
                root.put("health", entity.getHealth());
            if (shouldSerialize("name"))
                root.put("name", entity.getCustomName());
            if (shouldSerialize("potion-effects"))
                root.put("potion-effects", PotionEffectSerialization.serializeEffects(entity.getActivePotionEffects()));
            root.put("type", entity.getType().getTypeId());
            return root;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String serializeEntityAsString(LivingEntity entity) {
        return serializeEntityAsString(entity, false);
    }

    public static String serializeEntityAsString(LivingEntity entity, boolean pretty) {
        return serializeEntityAsString(entity, pretty, 5);
    }

    public static String serializeEntityAsString(LivingEntity entity, boolean pretty, int indentFactor) {
        try {
            if (pretty)
                return serializeEntity(entity).toString(indentFactor);
            return serializeEntity(entity).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LivingEntity spawnEntity(Location location, String stats) {
        try {
            return spawnEntity(location, new JSONObject(stats));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LivingEntity spawnEntity(Location location, JSONObject stats) {
        try {
            if (!stats.has("type"))
                throw new IllegalArgumentException("The type of the entity cannot be determined");
            LivingEntity entity = (LivingEntity)location.getWorld().spawnEntity(location, EntityType.fromId(stats.getInt("type")));
            if (stats.has("age") && entity instanceof Ageable)
                ((Ageable)entity).setAge(stats.getInt("age"));
            if (stats.has("health"))
                entity.setHealth(stats.getDouble("health"));
            if (stats.has("name"))
                entity.setCustomName(stats.getString("name"));
            if (stats.has("potion-effects"));
            PotionEffectSerialization.addPotionEffects(stats.getString("potion-effects"), entity);
            return entity;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean shouldSerialize(String key) {
        return SerializationConfig.getShouldSerialize("living-entity." + key);
    }
}
