package me.psikuvit.betterenchants;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Objects;

public class Utils {

    public static final NamespacedKey GLOW_ENCHANTMENT_KEY = new NamespacedKey(BetterEnchants.getPlugin(), "glow");
    private static final Enchantment instance;

    static {
        Enchantment existing = Enchantment.getByKey(GLOW_ENCHANTMENT_KEY);
        instance = Objects.requireNonNullElseGet(existing, GlowEnch::new);
        registerEnchantment(instance);
    }


    public static String convertToRomanNumeral(int number) {
        if (number < 1 || number > 100) {
            throw new IllegalArgumentException("Number must be between 1 and 100 (inclusive).");
        }

        StringBuilder romanNumeral = new StringBuilder();
        int[] arabicValues = {100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] romanSymbols = {"C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        for (int i = 0; i < arabicValues.length; i++) {
            while (number >= arabicValues[i]) {
                romanNumeral.append(romanSymbols[i]);
                number -= arabicValues[i];
            }
        }

        return romanNumeral.toString();
    }
    public static boolean addGlowEffect(@NotNull ItemMeta meta) {
        if (meta.hasEnchant(instance)) {
            return false;
        }
        meta.addEnchant(instance, 1, true);
        return true;
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
