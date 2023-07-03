package me.psikuvit.betterenchants.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class Messages {

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

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    public static List<String> color(List<String> msgs) {
        return msgs.stream().map(Messages::color).collect(Collectors.toList());
    }

    public static void log(String msg) {
        Bukkit.getLogger().info(msg);
    }
    public static void sendMessage(Player player, String msg) {
        player.sendMessage(color(msg));
    }

    public static void sendMessage(HumanEntity player, String msg) {
        player.sendMessage(color(msg));
    }
}
