package me.psikuvit.betterenchants;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantListeners implements Listener {

    @EventHandler //Life steal enchant listener
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player player) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();

            if (EnchantingSystem.hasCustomEnchantment(itemStack)) {
                if (EnchantingSystem.getCustomEnchantment(itemStack) == EnchantingSystem.CustomEnchantment.LIFE_STEAL) {
                    int i = EnchantingSystem.getCustomEnchantmentLevel(itemStack, EnchantingSystem.CustomEnchantment.LIFE_STEAL);
                    double result = 0.05 * i + 0.05;
                    double playerHealth = player.getHealth();
                    double newHealth = result + playerHealth;
                    if (newHealth > 20) {
                        newHealth = 20;
                    }
                    player.setHealth(newHealth);
                    player.sendMessage("Healed for " + result);

                }
            }
        }
    }
}