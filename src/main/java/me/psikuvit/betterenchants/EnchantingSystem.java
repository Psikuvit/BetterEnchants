package me.psikuvit.betterenchants;

import me.psikuvit.betterenchants.utils.CustomEnchantment;
import me.psikuvit.betterenchants.utils.EnchantUtils;
import me.psikuvit.betterenchants.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EnchantingSystem {


    public static boolean hasCustomEnchantment(ItemStack item, CustomEnchantment enchantment) {
        if (item == null || !item.hasItemMeta()) return false;
        return getCustomEnchantmentLevel(item, enchantment) > 0;
    }
    public static boolean containsCustomEnchantment(ItemStack item) {
        if (item == null) return false;
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        for (CustomEnchantment customEnchantment : CustomEnchantment.values()) {
            if (container.has(customEnchantment.getKey(), PersistentDataType.INTEGER)) {
                return true;
            }
        }
        return false;
    }

    public static CustomEnchantment getBookEnchant(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        for (CustomEnchantment customEnchantment : CustomEnchantment.values()) {
            if (container.has(customEnchantment.getKey(), PersistentDataType.INTEGER)) return customEnchantment;
        }
        return null;
    }


    public static void addCustomEnchantment(Player player, ItemStack item, CustomEnchantment enchantment, int level) {
        if (item == null) return;
        if (!enchantment.getTarget().includes(item.getType())) {
            Messages.sendMessage(player, "This enchant can't be applied to this Item");
            return;
        }
        if (hasCustomEnchantment(item, enchantment)) {
            Messages.sendMessage(player, "This Item already have this enchant");
            return;
        }
        if (getCustomEnchantmentLevel(item, enchantment) >= level) {
            Messages.sendMessage(player, "This item has a higher tier");
        }
        if (enchantment.getToReplace() != null) {
            if (item.getItemMeta().hasEnchant(enchantment.getToReplace())) item.removeEnchantment(enchantment.getToReplace());
        }
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (level > enchantment.getMaxLevel()) {
            Messages.sendMessage(player, "Couldn't add enchant because the max level for this enchant is: " + enchantment.getMaxLevel());
            return;
        }

        container.set(enchantment.getKey(), PersistentDataType.INTEGER, level);

        EnchantUtils.addEnchantLore(enchantment, level, itemMeta);
        EnchantUtils.addGlowEffect(itemMeta);

        item.setItemMeta(itemMeta);
    }
    public static void addEnchantToBook(Player player, ItemStack item, CustomEnchantment enchantment, int level) {
        if (item == null) return;
        ItemMeta itemMeta = item.hasItemMeta() ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta(item.getType());
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (level > enchantment.getMaxLevel()) {
            Messages.sendMessage(player, "Couldn't add enchant because the max level for this enchant is: " + enchantment.getMaxLevel());
            return;
        }
        container.set(enchantment.getKey(), PersistentDataType.INTEGER, level);
        EnchantUtils.addEnchantLore(enchantment, level, itemMeta);

        item.setItemMeta(itemMeta);
    }

    public static int getCustomEnchantmentLevel(ItemStack item, CustomEnchantment enchantment) {
        if (item == null) return 0;
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(enchantment.getKey(), PersistentDataType.INTEGER, 0);
    }

    public static void removeCustomEnchantment(ItemStack item, CustomEnchantment enchantment) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        container.remove(enchantment.getKey());
        EnchantUtils.removeEnchantLore(enchantment, item.getItemMeta());
    }

}