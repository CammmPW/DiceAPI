package org.dicemc.diceapi.util;

import org.dicemc.diceapi.DiceAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {
    public enum DisplayName {
        OAK_PLANK(5, 0, new String[0]),
        SPRUCE_PLANK(5, 1, new String[] { "pine_plank", "spruce_plank" }),
        BIRCH_PLANK(5, 2, new String[0]),
        JUNGLE_PLANK(5, 3, new String[0]),
        OAK_SAPLING(6, 0, new String[0]),
        SPRUCE_SAPLING(6, 1, new String[] { "pine_sapling" }),
        BIRCH_SAPLING(6, 2, new String[0]),
        JUNGLE_SAPLING(6, 3, new String[0]),
        OAK_LOG(17, 0, new String[] { "log" }),
        SPRUCE_LOG(17, 1, new String[0]),
        BIRH_LOG(17, 2, new String[0]),
        JUNGLE_LOG(17, 3, new String[0]),
        OAK_LEAVES(18, 0, new String[0]),
        SPRUCE_LEAVES(18, 1, new String[] { "pine_leaves" }),
        BIRCH_LEAVES(18, 2, new String[0]),
        JUNGLE_LEAVES(18, 3, new String[0]),
        WHITE_WOOL(35, 0, new String[] { "white", "wool" }),
        ORANGE_WOOL(35, 1, new String[] { "orange" }),
        MAGENTA_WOOL(35, 2, new String[] { "magenta" }),
        LIGHT_BLUE_WOOL(35, 3, new String[] { "light_blue" }),
        YELLOW_WOOL(35, 4, new String[] { "yellow" }),
        LIME_GREEN_WOOL(35, 5, new String[] { "light_green_wool", "light_green", "lime_green" }),
        PINK_WOOL(35, 6, new String[] { "pink", "lightish_red" }),
        GRAY_WOOL(35, 7, new String[] { "grey_wool", "grey", "gray_wool", "gray" }),
        LIGHT_GRAY_WOOL(35, 8, new String[] { "light_grey_wool", "light_grey", "light_gray" }),
        CYAN_WOOL(35, 9, new String[] { "cyan" }),
        PURPLE_WOOL(35, 10, new String[] { "purple" }),
        BLUE_WOOL(35, 11, new String[] { "blue" }),
        BROWN_WOOL(35, 12, new String[] { "brown" }),
        GREEN_WOOL(35, 13, new String[] { "green" }),
        RED_WOOL(35, 14, new String[] { "red" }),
        BLACK_WOOL(35, 15, new String[] { "black" }),
        WHITE_STAINED_CLAY(159, 0, new String[] { "white_clay" }),
        ORANGE_STAINED_CLAY(159, 1, new String[] { "orange_clay" }),
        MAGENTA_STAINED_CLAY(159, 2, new String[] { "magenta_clay" }),
        LIGHT_BLUE_STAINED_CLAY(159, 3, new String[] { "light_blue_clay" }),
        YELLOW_STAINED_CLAY(159, 4, new String[] { "yellow_clay" }),
        LIME_GREEN_STAINED_CLAY(159, 5, new String[] { "light_green_stained_clay", "light_green_clay", "lime_green_clay", "lime_stained_clay", "lime_clay" }),
        PINK_STAINED_CLAY(159, 6, new String[] { "pink_clay" }),
        GRAY_STAINED_CLAY(159, 7, new String[] { "gray_clay", "grey_stained_clay", "grey_clay" }),
        LIGHT_GRAY_STAINED_CLAY(159, 8, new String[] { "light_gray_clay", "grey_stained_clay", "grey_clay" }),
        CYAN_STAINED_CLAY(159, 9, new String[] { "cyan_clay" }),
        PURPLE_STAINED_CLAY(159, 10, new String[] { "purple_clay" }),
        BLUE_STAINED_CLAY(159, 11, new String[] { "blue_clay" }),
        BROWN_STAINED_CLAY(159, 12, new String[] { "brown_clay" }),
        GREEN_STAINED_CLAY(159, 13, new String[] { "green_clay" }),
        RED_STAINED_CLAY(159, 14, new String[] { "red_clay" }),
        BLACK_STAINED_CLAY(159, 15, new String[] { "black_Clay" }),
        WHITE_CARPET(171, 0, new String[] { "carpet" }),
        ORANGE_CARPET(171, 1, new String[0]),
        MAGENTA_CARPET(171, 2, new String[0]),
        LIGHT_BLUE_CARPET(171, 3, new String[0]),
        YELLOW_CARPET(171, 4, new String[0]),
        LIME_GREEN_CARPET(171, 5, new String[] { "light_green_carpet" }),
        PINK_CARPET(171, 6, new String[0]),
        GRAY_CARPET(171, 7, new String[] { "grey_carpet" }),
        LIGHT_GRAY_CARPET(171, 8, new String[] { "light_grey_carpet" }),
        CYAN_CARPET(171, 9, new String[0]),
        PURPLE_CARPET(171, 10, new String[0]),
        BLUE_CARPET(171, 11, new String[0]),
        BROWN_CARPET(171, 12, new String[0]),
        GREEN_CARPET(171, 13, new String[0]),
        RED_CARPET(171, 14, new String[0]),
        BLACK_CARPET(171, 15, new String[0]),
        BLACK_DYE(
                351, 0, new String[] { "ink_sac", "ink_sack" }),
        RED_DYE(351, 1, new String[] { "rose_red_dye", "rose_red" }),
        GREEN_DYE(351, 2, new String[] { "cactus_green_dye", "catus_green" }),
        BROWN_DYE(351, 3, new String[] { "cocoa_beans" }),
        BLUE_DYE(351, 4, new String[] { "lapis_lazuli" }),
        PURPLE_DYE(351, 5, new String[0]),
        CYAN_DYE(351, 6, new String[0]),
        LIGHT_GRAY_DYE(351, 7, new String[] { "light_grey_dye" }),
        GRAY_DYE(351, 8, new String[] { "grey_dye" }),
        PINK_DYE(351, 9, new String[] { "lightish_red_dye" }),
        LIME_GREEN_DYE(351, 10, new String[] { "light_green_dye" }),
        YELLOW_DYE(351, 11, new String[] { "dandelion_yellow_dye", "dandelion_yellow" }),
        LIGHT_BLUE_DYE(351, 12, new String[0]),
        MAGENTA_DYE(351, 13, new String[0]),
        ORANGE_DYE(351, 14, new String[0]),
        WHITE_DYE(351, 15, new String[] { "bonemeal_dye", "bonemeal" }),
        CREEPER_SPAWN_EGG(383, 50, new String[] { "spawn_creeper" }),
        SKELETON_SPAWN_EGG(383, 51, new String[] { "spawn_skeleton" }),
        SPIDER_SPAWN_EGG(383, 52, new String[] { "spawn_spider" }),
        GIANT_SPAWN_EGG(383, 53, new String[] { "spawn_giant" }),
        ZOMBIE_SPAWN_EGG(383, 54, new String[] { "spawn_zombie" }),
        SLIME_SPAWN_EGG(383, 55, new String[] { "spawn_slime" }),
        GHAST_SPAWN_EGG(383, 56, new String[] { "spawn_ghast" }),
        ZOMBIE_PIGMAN_SPAWN_EGG(383, 57, new String[] { "pigzombie_spawn_egg", "spawn_pigzombie", "spawn_zombie_pigman" }),
        ENDERMAN_SPAWN_EGG(383, 58, new String[] { "spawn_enderman" }),
        CAVE_SPIDER_SPAWN_EGG(383, 59, new String[] { "spawn_cave_spider" }),
        SILVERFISH_SPAWN_EGG(383, 60, new String[] { "spawn_silverfish" }),
        BLAZE_SPAWN_EGG(383, 61, new String[] { "spawn_blaze" }),
        MAGMA_CUBE_SPAWN_EGG(383, 62, new String[] { "spawn_magma_cube" }),
        ENDER_DRAGON_SPAWN_EGG(383, 63, new String[] { "enderdragon_spawn_egg", "spawn_enderdragon", "spawn_ender_dragon" }),
        WITHER_SPAWN_EGG(383, 64, new String[] { "spawn_wither" }),
        BAT_SPAWN_EGG(383, 65, new String[] { "spawn_bat" }),
        WITCH_SPAWN_EGG(383, 66, new String[] { "spawn_witch" }),
        PIG_SPAWN_EGG(383, 90, new String[] { "spawn_pig" }),
        SHEEP_SPAWN_EGG(383, 91, new String[] { "spawn_sheep" }),
        COW_SPAWN_EGG(383, 92, new String[] { "spawn_cow" }),
        CHICKEN_SPAWN_EGG(383, 93, new String[] { "spawn_chicken" }),
        SQUID_SPAWN_EGG(383, 94, new String[] { "spawn_squid" }),
        WOLF_SPAWN_EGG(383, 95, new String[] { "spawn_wolf" }),
        MOOSHROOM_SPAWN_EGG(383, 96, new String[] { "spawn_mooshroom" }),
        SNOW_GOLEM_SPAWN_EGG(383, 97, new String[] { "spawn_snow_golem" }),
        OCELOT_SPAWN_EGG(383, 98, new String[] { "spawn_ocelot" }),
        IRON_GOLEM_SPAWN_EGG(383, 99, new String[] { "spawn_iron_golem" }),
        HORSE_SPAWN_EGG(383, 100, new String[] { "spawn_horse" }),
        VILLAGER_SPAWN_EGG(383, 120, new String[] { "spawn_villager" }),
        SKELETON_HEAD(397, 0, new String[0]),
        WITHER_SKELETON_HEAD(383, 1, new String[0]),
        ZOMBIE_HEAD(383, 2, new String[0]),
        HUMAN_HEAD(383, 3, new String[] { "steve_head" }),
        CREEPER_HEAD(383, 4, new String[0]);

        private int id;

        private int damage;

        private String[] aliases;

        DisplayName(int id, int damage, String... aliases) {
            this.id = id;
            this.damage = damage;
            this.aliases = aliases;
        }

        public boolean hasAlias(String alias) {
            if (name().equalsIgnoreCase(alias))
                return true;
            byte b;
            int i;
            String[] arrayOfString;
            for (i = (arrayOfString = this.aliases).length, b = 0; b < i; ) {
                String s = arrayOfString[b];
                if (s.equalsIgnoreCase(alias))
                    return true;
                b++;
            }
            return false;
        }

        public String getName() {
            return DiceAPI.getChatUtils().toProperCase(name().replaceAll("_", " "));
        }

        public int getId() {
            return this.id;
        }

        public int getDamage() {
            return this.damage;
        }

        public static DisplayName getDisplayName(String name) {
            byte b;
            int i;
            DisplayName[] arrayOfDisplayName;
            for (i = (arrayOfDisplayName = values()).length, b = 0; b < i; ) {
                DisplayName dn = arrayOfDisplayName[b];
                if (dn.hasAlias(name))
                    return dn;
                b++;
            }
            return null;
        }

        public static DisplayName getDisplayName(int id, int damage) {
            byte b;
            int i;
            DisplayName[] arrayOfDisplayName;
            for (i = (arrayOfDisplayName = values()).length, b = 0; b < i; ) {
                DisplayName dn = arrayOfDisplayName[b];
                if (dn.getId() == id && dn.getDamage() == damage)
                    return dn;
                b++;
            }
            return null;
        }
    }

    public enum EnchantDisplayName {
        PROTECTION(0),
        FIRE_PROTECTION(1),
        FEATHER_FALLING(2),
        BLAST_PROTECTION(3),
        PROJECTILE_PROTECTION(4),
        RESPIRATION(5),
        AQUA_AFFINITY(6),
        THORNS(7),
        SHARPNESS(16),
        SMITE(17),
        BANE_OF_ARTHROPODS(18),
        KNOCKBACK(19),
        FIRE_ASPECT(20),
        LOOTING(21),
        EFFICIENCY(32),
        SILK_TOUCH(33),
        UNBREAKING(34),
        FORTUNE(35),
        POWER(48),
        PUNCH(49),
        FLAME(50),
        INFINITY(51);

        private int id;

        EnchantDisplayName(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return DiceAPI.getChatUtils().toProperCase(name().replace("_", " "));
        }

        public static EnchantDisplayName getDisplayName(int id) {
            byte b;
            int i;
            EnchantDisplayName[] arrayOfEnchantDisplayName;
            for (i = (arrayOfEnchantDisplayName = values()).length, b = 0; b < i; ) {
                EnchantDisplayName name = arrayOfEnchantDisplayName[b];
                if (id == name.getId())
                    return name;
                b++;
            }
            return null;
        }
    }

    public ItemStack createItemStack(String name, int amount) {
        String alias;
        short damage = 0;
        if (name.contains(":")) {
            String[] split = name.split(":");
            alias = split[0];
            if (DiceAPI.getChatUtils().isNum(split[1]))
                damage = (short)Integer.parseInt(split[1]);
        } else {
            alias = name;
        }
        Material mat = getMaterial(alias);
        if (mat == null) {
            DisplayName dn = DisplayName.getDisplayName(alias);
            if (dn == null)
                return null;
            return new ItemStack(mat, amount, (short)dn.getDamage());
        }
        return new ItemStack(mat, amount, damage);
    }

    public Material getMaterial(String material) {
        if (DiceAPI.getChatUtils().isNum(material))
            return Material.getMaterial((material));
        return Material.getMaterial(material.toUpperCase());
    }

    public boolean displayNameExists(String name) {
        byte b;
        int i;
        DisplayName[] arrayOfDisplayName;
        for (i = (arrayOfDisplayName = DisplayName.values()).length, b = 0; b < i; ) {
            DisplayName dn = arrayOfDisplayName[b];
            if (dn.hasAlias(name))
                return true;
            b++;
        }
        return false;
    }

    public boolean isHelmet(Material material) {
        return !(material != Material.LEATHER_HELMET && material != Material.GOLDEN_HELMET && material != Material.IRON_HELMET &&
                material != Material.CHAINMAIL_HELMET && material != Material.DIAMOND_HELMET);
    }

    public boolean isChestPlate(Material material) {
        return !(material != Material.LEATHER_CHESTPLATE && material != Material.GOLDEN_CHESTPLATE && material != Material.IRON_CHESTPLATE &&
                material != Material.CHAINMAIL_CHESTPLATE && material != Material.DIAMOND_CHESTPLATE);
    }

    public boolean isLegging(Material material) {
        return !(material != Material.LEATHER_LEGGINGS && material != Material.GOLDEN_LEGGINGS && material != Material.IRON_LEGGINGS &&
                material != Material.CHAINMAIL_LEGGINGS && material != Material.DIAMOND_LEGGINGS);
    }

    public boolean isBoot(Material material) {
        return !(material != Material.LEATHER_BOOTS && material != Material.GOLDEN_BOOTS && material != Material.IRON_BOOTS &&
                material != Material.CHAINMAIL_BOOTS && material != Material.DIAMOND_BOOTS);
    }

    public boolean isArmor(Material material) {
        return !(!isHelmet(material) && !isChestPlate(material) && !isLegging(material) && !isBoot(material));
    }

    public boolean isLeatherArmor(Material material) {
        return !(material != Material.LEATHER_HELMET && material != Material.LEATHER_CHESTPLATE &&
                material != Material.LEATHER_LEGGINGS && material != Material.LEATHER_BOOTS);
    }

    public boolean isGoldArmor(Material material) {
        return !(material != Material.GOLDEN_HELMET && material != Material.GOLDEN_CHESTPLATE &&
                material != Material.GOLDEN_LEGGINGS && material != Material.GOLDEN_BOOTS);
    }

    public boolean isIronArmor(Material material) {
        return !(material != Material.IRON_HELMET && material != Material.IRON_CHESTPLATE &&
                material != Material.IRON_LEGGINGS && material != Material.IRON_BOOTS);
    }

    public boolean isChainMailArmor(Material material) {
        return !(material != Material.CHAINMAIL_HELMET && material != Material.CHAINMAIL_CHESTPLATE &&
                material != Material.CHAINMAIL_LEGGINGS && material != Material.CHAINMAIL_BOOTS);
    }

    public boolean isDiamondArmor(Material material) {
        return !(material != Material.DIAMOND_HELMET && material != Material.DIAMOND_CHESTPLATE &&
                material != Material.DIAMOND_LEGGINGS && material != Material.DIAMOND_BOOTS);
    }

    public boolean isPickaxe(Material material) {
        return !(material != Material.WOODEN_PICKAXE && material != Material.GOLDEN_PICKAXE && material != Material.IRON_PICKAXE && material != Material.DIAMOND_PICKAXE);
    }

    public boolean isAxe(Material material) {
        return !(material != Material.WOODEN_AXE && material != Material.GOLDEN_AXE && material != Material.IRON_AXE && material != Material.DIAMOND_AXE);
    }

    public boolean isShovel(Material material) {
        return !(material != Material.WOODEN_SHOVEL && material != Material.GOLDEN_SHOVEL && material != Material.IRON_SHOVEL && material != Material.DIAMOND_SHOVEL);
    }

    public boolean isHoe(Material material) {
        return !(material != Material.WOODEN_HOE && material != Material.GOLDEN_HOE && material != Material.IRON_HOE && material != Material.DIAMOND_HOE);
    }

    public boolean isTool(Material material) {
        return !(!isPickaxe(material) && !isAxe(material) && !isShovel(material) && !isHoe(material) && material != Material.SHEARS);
    }

    public boolean isSword(Material material) {
        return !(material != Material.WOODEN_SWORD && material != Material.GOLDEN_SWORD && material != Material.IRON_SWORD && material != Material.DIAMOND_SWORD);
    }

    public boolean isWeapon(Material material) {
        return !(!isSword(material) && material != Material.BOW);
    }
}
