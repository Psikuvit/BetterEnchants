package me.psikuvit.betterenchants.utils;

import me.psikuvit.betterenchants.BetterEnchants;
import me.psikuvit.betterenchants.EnchantingSystem;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnchantUtils {

    public static final NamespacedKey GLOW_ENCHANTMENT_KEY = new NamespacedKey(BetterEnchants.getPlugin(), "glow");
    private static final Enchantment instance;

    static {
        Enchantment existing = Enchantment.getByKey(GLOW_ENCHANTMENT_KEY);
        instance = Objects.requireNonNullElseGet(existing, GlowEnch::new);
        registerEnchantment(instance);
    }
    public static void addGlowEffect(@NotNull ItemMeta meta) {
        if (meta.hasEnchant(instance)) {
            return;
        }
        meta.addEnchant(instance, 1, true);
    }
    public static void addEnchantLore(CustomEnchantment enchantment, int level, ItemMeta meta) {
        List<String> lore = meta.getLore();

        if (lore == null) lore = new ArrayList<>();

        lore.add(ChatColor.GRAY + enchantment.getDisplayName() + " " + Messages.convertToRomanNumeral(level));
        meta.setLore(lore);
    }
    public static void removeEnchantLore(CustomEnchantment enchantment, ItemMeta meta) {
        List<String> lore = meta.getLore();

        if (lore == null) lore = new ArrayList<>();

        for (String s : lore) {
            String[] strings = s.split(" ");
            if (strings[0].equals(ChatColor.GRAY + enchantment.getDisplayName())) {
                lore.remove(s);
            }
        }
        meta.setLore(lore);
    }

    public static void registerEnchantment(final Enchantment enchantment) {
        try {
            final Field fieldAcceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
            fieldAcceptingNew.setAccessible(true);
            fieldAcceptingNew.set(null, true);
            fieldAcceptingNew.setAccessible(false);

            if (Enchantment.getByKey(enchantment.getKey()) == null) Enchantment.registerEnchantment(enchantment);
        } catch (final Throwable exception) {
            exception.printStackTrace();
        }
    }

    public static Target getItemEnchantTarget(ItemStack itemStack) {
        if (itemStack.getType().getEquipmentSlot().equals(EquipmentSlot.HEAD)) return Target.ARMOR_HEAD;
        else if (itemStack.getType().getEquipmentSlot().equals(EquipmentSlot.CHEST)) return Target.ARMOR_TORSO;
        else if (itemStack.getType().getEquipmentSlot().equals(EquipmentSlot.LEGS)) return Target.ARMOR_LEGS;
        else if (itemStack.getType().getEquipmentSlot().equals(EquipmentSlot.FEET)) return Target.ARMOR_FEET;
        else if (itemStack.getType().name().endsWith("SWORD")) return Target.WEAPON;
        else if (itemStack.getType().name().endsWith("CROSSBOW")) return Target.CROSSBOW;
        else if (itemStack.getType().name().endsWith("ROD")) return Target.FISHING_ROD;
        else if (itemStack.getType().name().endsWith("BOW")) return Target.BOW;
        else if (itemStack.getType().name().endsWith("SHOVEL")) return Target.SHOVEL;
        else if (itemStack.getType().name().endsWith("HOE")) return Target.HOE;
        else if (itemStack.getType().name().endsWith("PICKAXE")) return Target.PICKAXE;
        else if (itemStack.getType().name().endsWith("AXE")) return Target.AXE;
        else return null;
    }
}
