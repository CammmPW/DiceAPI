package org.dicemc.diceapi.api.serialization;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.dicemc.diceapi.json.JSONException;
import org.dicemc.diceapi.json.JSONObject;

public class SkullSerialization {
    public static JSONObject serializeSkull(SkullMeta meta) {
        try {
            JSONObject root = new JSONObject();
            if (meta.hasOwner())
                root.put("owner", meta.getOwner());
            return root;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String serializeSkullAsString(SkullMeta meta) {
        return serializeSkullAsString(meta, false);
    }

    public static String serializeSkullAsString(SkullMeta meta, boolean pretty) {
        return serializeSkullAsString(meta, pretty, 5);
    }

    public static String serializeSkullAsString(SkullMeta meta, boolean pretty, int indentFactor) {
        try {
            if (pretty)
                return serializeSkull(meta).toString(indentFactor);
            return serializeSkull(meta).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SkullMeta getSkullMeta(String meta) {
        try {
            return getSkullMeta(new JSONObject(meta));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SkullMeta getSkullMeta(JSONObject meta) {
        try {
            ItemStack dummyItems = new ItemStack(Material.SKULL_ITEM);
            SkullMeta dummyMeta = (SkullMeta)dummyItems.getItemMeta();
            if (meta.has("owner"))
                dummyMeta.setOwner(meta.getString("owner"));
            return dummyMeta;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
