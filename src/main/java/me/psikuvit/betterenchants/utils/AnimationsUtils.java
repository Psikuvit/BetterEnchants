package me.psikuvit.betterenchants.utils;

import me.psikuvit.betterenchants.Animation;
import me.psikuvit.betterenchants.BetterEnchants;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AnimationsUtils {

    private final static Map<UUID, Animation> playerStands = new HashMap<>();

    public static BukkitRunnable teleportBlessing(ArmorStand armorStand) {

        return new BukkitRunnable() {
            @Override
            public void run() {
                armorStand.teleport(armorStand.getLocation().add(0, 0.1, 0));
            }
        };
    }

    // Start teleporting the armor stand from the bottom (inside the chest)
    public static void startFromBottom(ArmorStand armorStand) {
        for (int i = 0; i <= 18; i+=2)
            teleportBlessing(armorStand).runTaskLater(BetterEnchants.getPlugin(), i);
    }

    // Play blessing chest open sound
    public static BukkitRunnable playOpenSound(Player player, Float volume, Float pitch) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, volume, pitch);
            }
        };
    }

    public static Map<UUID, Animation> getPlayerStands() {
        return playerStands;
    }
}
