package org.dicemc.diceapi.api.serialization;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.dicemc.diceapi.json.JSONArray;
import org.dicemc.diceapi.json.JSONException;
import org.dicemc.diceapi.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class InventorySerialization {
    public static JSONArray serializeInventory(Inventory inv) {
        JSONArray inventory = new JSONArray();
        for (int i = 0; i < inv.getSize(); i++) {
            JSONObject values = SingleItemSerialization.serializeItemInInventory(inv.getItem(i), i);
            if (values != null)
                inventory.put(values);
        }
        return inventory;
    }

    public static JSONObject serializePlayerInventory(PlayerInventory inv) {
        try {
            JSONObject root = new JSONObject();
            JSONArray inventory = serializeInventory((Inventory)inv);
            JSONArray armor = serializeInventory(inv.getArmorContents());
            root.put("inventory", inventory);
            root.put("armor", armor);
            return root;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String serializePlayerInventoryAsString(PlayerInventory inv) {
        return serializePlayerInventoryAsString(inv, false);
    }

    public static String serializePlayerInventoryAsString(PlayerInventory inv, boolean pretty) {
        return serializePlayerInventoryAsString(inv, pretty, 5);
    }

    public static String serializePlayerInventoryAsString(PlayerInventory inv, boolean pretty, int indentFactor) {
        try {
            if (pretty)
                return serializePlayerInventory(inv).toString(indentFactor);
            return serializePlayerInventory(inv).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String serializeInventoryAsString(Inventory inventory) {
        return serializeInventoryAsString(inventory, false);
    }

    public static String serializeInventoryAsString(Inventory inventory, boolean pretty) {
        return serializeInventoryAsString(inventory, pretty, 5);
    }

    public static String serializeInventoryAsString(Inventory inventory, boolean pretty, int indentFactor) {
        try {
            if (pretty)
                return serializeInventory(inventory).toString(indentFactor);
            return serializeInventory(inventory).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String serializeInventoryAsString(ItemStack[] contents) {
        return serializeInventoryAsString(contents, false);
    }

    public static String serializeInventoryAsString(ItemStack[] contents, boolean pretty) {
        return serializeInventoryAsString(contents, pretty, 5);
    }

    public static String serializeInventoryAsString(ItemStack[] contents, boolean pretty, int indentFactor) {
        try {
            if (pretty)
                return serializeInventory(contents).toString(indentFactor);
            return serializeInventory(contents).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONArray serializeInventory(ItemStack[] contents) {
        JSONArray inventory = new JSONArray();
        for (int i = 0; i < contents.length; i++) {
            JSONObject values = SingleItemSerialization.serializeItemInInventory(contents[i], i);
            if (values != null)
                inventory.put(values);
        }
        return inventory;
    }

    public static ItemStack[] getInventory(String json, int size) {
        try {
            return getInventory(new JSONArray(json), size);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack[] getInventory(JSONArray inv, int size) {
        try {
            ItemStack[] contents = new ItemStack[size];
            for (int i = 0; i < inv.length(); i++) {
                JSONObject item = inv.getJSONObject(i);
                int index = item.getInt("index");
                if (index > size)
                    throw new IllegalArgumentException("index found is greator than expected size (" + index + ">" + size + ")");
                if (index > contents.length || index < 0)
                    throw new IllegalArgumentException("Item " + i + " - Slot " + index + " does not exist in this inventory");
                ItemStack stuff = SingleItemSerialization.getItem(item);
                contents[index] = stuff;
            }
            return contents;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack[] getInventory(File jsonFile, int size) {
        String source = "";
        try {
            Scanner x = new Scanner(jsonFile);
            while (x.hasNextLine())
                source = source + x.nextLine() + "\n";
            x.close();
            return getInventory(source, size);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setInventory(InventoryHolder holder, String inv) {
        setInventory(holder.getInventory(), inv);
    }

    public static void setInventory(InventoryHolder holder, JSONArray inv) {
        setInventory(holder.getInventory(), inv);
    }

    public static void setInventory(Inventory inventory, String inv) {
        try {
            setInventory(inventory, new JSONArray(inv));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setInventory(Inventory inventory, JSONArray inv) {
        ItemStack[] items = getInventory(inv, inventory.getSize());
        inventory.clear();
        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            if (item != null)
                inventory.setItem(i, item);
        }
    }

    public static void setPlayerInventory(Player player, String inv) {
        try {
            setPlayerInventory(player, new JSONObject(inv));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setPlayerInventory(Player player, JSONObject inv) {
        try {
            PlayerInventory inventory = player.getInventory();
            ItemStack[] armor = getInventory(inv.getJSONArray("armor"), 4);
            inventory.clear();
            inventory.setArmorContents(armor);
            setInventory(player, inv.getJSONArray("inventory"));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
