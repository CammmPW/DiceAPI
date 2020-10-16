package org.dicemc.diceapi.obj;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryObject {
    public boolean canHold(Player player, ItemStack items) {
        int space = 0, needed = items.getAmount();
        for (ItemStack i : player.getInventory()) {
            if (i == null) {
                space += items.getMaxStackSize();
                continue;
            }
            if (i.getType() == items.getType() && i.getDurability() == items.getDurability() && (
                    !i.getItemMeta().hasDisplayName() || i.getItemMeta().getDisplayName()
                            .equalsIgnoreCase(items.getItemMeta().getDisplayName())))
                space += items.getMaxStackSize() - i.getAmount();
        }
        return space >= needed;
    }

    public boolean giveItems(Player player, ItemStack items) {
        if (canHold(player, items)) {
            player.getInventory().addItem(items);
            return true;
        }
        return false;
    }

    public boolean hasItems(Player player, ItemStack items) {
        int needed = items.getAmount();
        int amount = 0;
        for (ItemStack i : player.getInventory()) {
            if (i != null &&
                    i.getType() == items.getType() && i.getDurability() == items.getDurability()) {
                boolean hasEnchants = true;
                for (Enchantment e : i.getEnchantments().keySet()) {
                    if (items.getItemMeta().hasEnchant(e)) {
                        if (i.getEnchantmentLevel(e) != items.getEnchantmentLevel(e)) {
                            hasEnchants = false;
                            break;
                        }
                        continue;
                    }
                    hasEnchants = false;
                    break;
                }
                if (hasEnchants)
                    amount += i.getAmount();
            }
        }
        return amount >= needed;
    }

    public boolean takeItems(Player player, ItemStack items) {
        if (hasItems(player, items)) {
            player.getInventory().removeItem(items);
            return true;
        }
        return false;
    }

    public int removeAll(Player player, Material material) {
        int removed = getAmountOfMaterial(player, material);
        if (removed > 0)
            player.getInventory().remove(material);
        return removed;
    }

    public int getAmountOfMaterial(Player player, Material material) {
        if (material == null)
            return 0;
        return getAmountOfMaterial(player, material.getId());
    }

    public int getAmountOfMaterial(Player player, int id) {
        return getAmountOfMaterial(player, Material.getMaterial(String.valueOf(id)));
    }

    public int getAmountOfMaterial(Player player, Material id, int damage) {
        int amount = 0;
        if (id == Material.AIR || Material.getMaterial(String.valueOf(id)) == null)
            return 0;
        for (ItemStack i : player.getInventory()) {
            if (i.getType() == id && i.getDurability() == damage)
                amount += i.getAmount();
        }
        return amount;
    }
}
