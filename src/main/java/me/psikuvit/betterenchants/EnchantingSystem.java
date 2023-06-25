package me.psikuvit.betterenchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class EnchantingSystem {

    private static final BetterEnchants pluginInstance = BetterEnchants.getPlugin();

    private static final NamespacedKey ENCHANTMENT_KEY = new NamespacedKey(pluginInstance, "custom_enchantment");

    public static boolean hasCustomEnchantment(ItemStack item) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return container.has(ENCHANTMENT_KEY, PersistentDataType.STRING);
    }

    public static void addCustomEnchantment(ItemStack item, CustomEnchantment enchantment, int level) {
        if (getCustomEnchantment(item) == enchantment) {
            Messages.log("This Item already have this enchant");
            return;
        }
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(ENCHANTMENT_KEY, PersistentDataType.STRING, enchantment.name());

        if (level > enchantment.getMaxLevel()) {
            Messages.log("Couldn't add enchant because the max level for this enchant is: " + enchantment.getMaxLevel());
            return;
        }

        container.set(new NamespacedKey(pluginInstance, enchantment.name()), PersistentDataType.INTEGER, level);

        EnchantUtils.addEnchantLore(enchantment, level, itemMeta);
        EnchantUtils.addGlowEffect(itemMeta);

        item.setItemMeta(itemMeta);
    }

    public static CustomEnchantment getCustomEnchantment(ItemStack item) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        String enchantmentName = container.get(ENCHANTMENT_KEY, PersistentDataType.STRING);
        if (enchantmentName != null) {
            return CustomEnchantment.valueOf(enchantmentName);
        }
        return null;
    }

    public static int getCustomEnchantmentLevel(ItemStack item, CustomEnchantment enchantment) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        NamespacedKey levelKey = new NamespacedKey(pluginInstance, enchantment.getDisplayName());
        Integer level = container.get(levelKey, PersistentDataType.INTEGER);
        return level != null ? level : 0;
    }

    public static void removeCustomEnchantment(ItemStack item) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        container.remove(ENCHANTMENT_KEY);
        for (CustomEnchantment enchantment : CustomEnchantment.values()) {
            container.remove(new NamespacedKey(pluginInstance, enchantment.name()));
        }
    }

    public enum CustomEnchantment {
        // Define your custom enchantments here
        LIFE_STEAL("Life_Steal", 3);

        private final String displayName;
        private final int maxLevel;

        CustomEnchantment(String displayName, int maxLevel) {
            this.displayName = displayName;
            this.maxLevel = maxLevel;
        }

        public String getDisplayName() {
            return displayName;
        }

        public int getMaxLevel() {
            return maxLevel;
        }
    }
}