package org.dicemc.diceapi.api.serialization;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.dicemc.diceapi.json.JSONArray;
import org.dicemc.diceapi.json.JSONException;
import org.dicemc.diceapi.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class SingleItemSerialization {
    public static JSONObject serializeItemInInventory(ItemStack items, int index) {
        return serializeItems(items, true, index);
    }

    public static JSONObject serializeItem(ItemStack items) {
        return serializeItems(items, false, 0);
    }

    private static JSONObject serializeItems(ItemStack items, boolean useIndex, int index) {
        try {
            JSONObject values = new JSONObject();
            if (items == null)
                return null;
            Material id = items.getType();
            int amount = items.getAmount();
            int data = items.getDurability();
            boolean hasMeta = items.hasItemMeta();
            String name = null, enchants = null;
            String[] lore = null;
            Material mat = items.getType();
            JSONObject bookMeta = null, armorMeta = null, skullMeta = null, fwMeta = null;
            if (mat == Material.WRITABLE_BOOK || mat == Material.WRITTEN_BOOK) {
                bookMeta = BookSerialization.serializeBookMeta((BookMeta)items.getItemMeta());
            } else if (mat == Material.ENCHANTED_BOOK) {
                bookMeta = BookSerialization.serializeEnchantedBookMeta((EnchantmentStorageMeta)items.getItemMeta());
            } else if (Util.isLeatherArmor(mat)) {
                armorMeta = LeatherArmorSerialization.serializeArmor((LeatherArmorMeta)items.getItemMeta());
            } else if (mat == Material.SKELETON_SKULL) {
                skullMeta = SkullSerialization.serializeSkull((SkullMeta)items.getItemMeta());
            } else if (mat == Material.FIREWORK_ROCKET) {
                fwMeta = FireworkSerialization.serializeFireworkMeta((FireworkMeta)items.getItemMeta());
            }
            if (hasMeta) {
                ItemMeta meta = items.getItemMeta();
                if (meta.hasDisplayName())
                    name = meta.getDisplayName();
                if (meta.hasLore())
                    lore = (String[])meta.getLore().toArray((Object[])new String[0]);
                if (meta.hasEnchants())
                    enchants = EnchantmentSerialization.serializeEnchantments(meta.getEnchants());
            }
            values.put("id", id);
            values.put("amount", amount);
            values.put("data", data);
            if (useIndex)
                values.put("index", index);
            if (name != null)
                values.put("name", name);
            if (enchants != null)
                values.put("enchantments", enchants);
            if (lore != null)
                values.put("lore", lore);
            if (bookMeta != null && bookMeta.length() > 0)
                values.put("book-meta", bookMeta);
            if (armorMeta != null && armorMeta.length() > 0)
                values.put("armor-meta", armorMeta);
            if (skullMeta != null && skullMeta.length() > 0)
                values.put("skull-meta", skullMeta);
            if (fwMeta != null && fwMeta.length() > 0)
                values.put("firework-meta", fwMeta);
            return values;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack getItem(String item) {
        return getItem(item, 0);
    }

    public static ItemStack getItem(String item, int index) {
        try {
            return getItem(new JSONObject(item), index);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack getItem(JSONObject item) {
        return getItem(item, 0);
    }

    public static ItemStack getItem(JSONObject item, int index) {
        try {
            int id = item.getInt("id");
            int amount = item.getInt("amount");
            int data = item.getInt("data");
            String name = null;
            Map<Enchantment, Integer> enchants = null;
            ArrayList<String> lore = null;
            if (item.has("name"))
                name = item.getString("name");
            if (item.has("enchantments"))
                enchants = EnchantmentSerialization.getEnchantments(item.getString("enchantments"));
            if (item.has("lore")) {
                JSONArray l = item.getJSONArray("lore");
                lore = new ArrayList<>();
                for (int j = 0; j < l.length(); j++)
                    lore.add(l.getString(j));
            }
            if (Material.getMaterial(String.valueOf(id)) == null)
                throw new IllegalArgumentException("Item " + index + " - No Material found with id of " + id);
            Material mat = Material.getMaterial(String.valueOf(id));
            ItemStack stuff = new ItemStack(mat, amount, (short)data);
            if ((mat == Material.WRITABLE_BOOK || mat == Material.WRITTEN_BOOK) && item.has("book-meta")) {
                BookMeta bookMeta = BookSerialization.getBookMeta(item.getJSONObject("book-meta"));
                stuff.setItemMeta((ItemMeta)bookMeta);
            } else if (mat == Material.ENCHANTED_BOOK && item.has("book-meta")) {
                EnchantmentStorageMeta enchantmentStorageMeta = BookSerialization.getEnchantedBookMeta(item.getJSONObject("book-meta"));
                stuff.setItemMeta((ItemMeta)enchantmentStorageMeta);
            } else if (Util.isLeatherArmor(mat) && item.has("armor-meta")) {
                LeatherArmorMeta leatherArmorMeta = LeatherArmorSerialization.getLeatherArmorMeta(item.getJSONObject("armor-meta"));
                stuff.setItemMeta((ItemMeta)leatherArmorMeta);
            } else if (mat == Material.SKELETON_SKULL && item.has("skull-meta")) {
                SkullMeta skullMeta = SkullSerialization.getSkullMeta(item.getJSONObject("skull-meta"));
                stuff.setItemMeta((ItemMeta)skullMeta);
            } else if (mat == Material. FIREWORK_ROCKET && item.has("firework-meta")) {
                FireworkMeta fireworkMeta = FireworkSerialization.getFireworkMeta(item.getJSONObject("firework-meta"));
                stuff.setItemMeta((ItemMeta)fireworkMeta);
            }
            ItemMeta meta = stuff.getItemMeta();
            if (name != null)
                meta.setDisplayName(name);
            if (lore != null)
                meta.setLore(lore);
            stuff.setItemMeta(meta);
            if (enchants != null)
                stuff.addUnsafeEnchantments(enchants);
            return stuff;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String serializeItemInInventoryAsString(ItemStack items, int index) {
        return serializeItemInInventoryAsString(items, index, false);
    }

    public static String serializeItemInInventoryAsString(ItemStack items, int index, boolean pretty) {
        return serializeItemInInventoryAsString(items, index, pretty, 5);
    }

    public static String serializeItemInInventoryAsString(ItemStack items, int index, boolean pretty, int indentFactor) {
        try {
            if (pretty)
                return serializeItemInInventory(items, index).toString(indentFactor);
            return serializeItemInInventory(items, index).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String serializeItemAsString(ItemStack items) {
        return serializeItemAsString(items, false);
    }

    public static String serializeItemAsString(ItemStack items, boolean pretty) {
        return serializeItemAsString(items, pretty, 5);
    }

    public static String serializeItemAsString(ItemStack items, boolean pretty, int indentFactor) {
        try {
            if (pretty)
                return serializeItem(items).toString(indentFactor);
            return serializeItem(items).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
