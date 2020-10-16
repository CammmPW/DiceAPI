package org.dicemc.diceapi.api.serialization;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.dicemc.diceapi.json.JSONArray;
import org.dicemc.diceapi.json.JSONException;
import org.dicemc.diceapi.json.JSONObject;

public class FireworkEffectSerialization {
    public static FireworkEffect getFireworkEffect(String json) {
        return getFireworkEffect(json);
    }

    public static FireworkEffect getFireworkEffect(JSONObject json) {
        try {
            FireworkEffect.Builder builder = FireworkEffect.builder();
            JSONArray colors = json.getJSONArray("colors");
            for (int j = 0; j < colors.length(); j++)
                builder.withColor(ColorSerialization.getColor(colors.getJSONObject(j)));
            JSONArray fadeColors = json.getJSONArray("fade-colors");
            for (int i = 0; i < fadeColors.length(); i++)
                builder.withFade(ColorSerialization.getColor(colors.getJSONObject(i)));
            if (json.getBoolean("flicker"))
                builder.withFlicker();
            if (json.getBoolean("trail"))
                builder.withTrail();
            builder.with(FireworkEffect.Type.valueOf(json.getString("type")));
            return builder.build();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject serializeFireworkEffect(FireworkEffect effect) {
        try {
            JSONObject root = new JSONObject();
            JSONArray colors = new JSONArray();
            for (Color c : effect.getColors())
                colors.put(ColorSerialization.serializeColor(c));
            root.put("colors", colors);
            JSONArray fadeColors = new JSONArray();
            for (Color c : effect.getFadeColors())
                fadeColors.put(ColorSerialization.serializeColor(c));
            root.put("fade-colors", fadeColors);
            root.put("flicker", effect.hasFlicker());
            root.put("trail", effect.hasTrail());
            root.put("type", effect.getType().name());
            return root;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String serializeFireworkEffectAsString(FireworkEffect effect) {
        return serializeFireworkEffectAsString(effect, false);
    }

    public static String serializeFireworkEffectAsString(FireworkEffect effect, boolean pretty) {
        return serializeFireworkEffectAsString(effect, false, 5);
    }

    public static String serializeFireworkEffectAsString(FireworkEffect effect, boolean pretty, int indentFactor) {
        return Serializer.toString(serializeFireworkEffect(effect), pretty, indentFactor);
    }
}
