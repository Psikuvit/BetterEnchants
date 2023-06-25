package me.psikuvit.betterenchants;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
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
    public static void addEnchantLore(EnchantingSystem.CustomEnchantment enchantment, int level, ItemMeta meta) {
        List<String> lore = meta.getLore();

        if (lore == null) lore = new ArrayList<>();

        lore.add(ChatColor.GRAY + enchantment.getDisplayName() + " " + Messages.convertToRomanNumeral(level));
        meta.setLore(lore);
    }

    public static void registerEnchantment(final Enchantment enchantment) {
        try {
            final Field fieldAcceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
            fieldAcceptingNew.setAccessible(true);
            fieldAcceptingNew.set(null, true);
            fieldAcceptingNew.setAccessible(false);

            Enchantment.registerEnchantment(enchantment);
        } catch (final Throwable exception) {
            exception.printStackTrace();
        }
    }
}
