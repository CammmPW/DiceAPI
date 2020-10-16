package org.dicemc.diceapi.api.serialization;

import org.bukkit.Location;
import org.bukkit.entity.Ocelot;
import org.dicemc.diceapi.json.JSONException;
import org.dicemc.diceapi.json.JSONObject;

import java.io.IOException;

public class OcelotSerialization {
    public static JSONObject serializeOcelot(Ocelot ocelot) {
        try {
            JSONObject root = LivingEntitySerialization.serializeEntity(ocelot);
            if (shouldSerialize("type"))
                root.put("type", ocelot.getCatType().name());
            return root;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String serializeOcelotAsString(Ocelot ocelot) {
        return serializeOcelotAsString(ocelot, false);
    }

    public static String serializeOcelotAsString(Ocelot ocelot, boolean pretty) {
        return serializeOcelotAsString(ocelot, pretty, 5);
    }

    public static String serializeOcelotAsString(Ocelot ocelot, boolean pretty, int indentFactor) {
        try {
            if (pretty)
                return serializeOcelot(ocelot).toString(indentFactor);
            return serializeOcelot(ocelot).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Ocelot spawnOcelot(Location location, String stats) {
        try {
            return spawnOcelot(location, new JSONObject(stats));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Ocelot spawnOcelot(Location location, JSONObject stats) {
        try {
            Ocelot ocelot = (Ocelot)LivingEntitySerialization.spawnEntity(location, stats);
            if (stats.has("type"))
                ocelot.setCatType(Ocelot.Type.valueOf(stats.getString("type")));
            return ocelot;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean shouldSerialize(String key) {
        return SerializationConfig.getShouldSerialize("ocelot." + key);
    }
}
