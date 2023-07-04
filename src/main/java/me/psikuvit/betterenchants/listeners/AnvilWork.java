package me.psikuvit.betterenchants.listeners;

import me.psikuvit.betterenchants.EnchantingSystem;
import me.psikuvit.betterenchants.utils.CustomEnchantment;
import me.psikuvit.betterenchants.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class AnvilWork implements Listener {

    @EventHandler
    public void onUse(PrepareAnvilEvent e) {
        Player player = (Player) e.getView().getPlayer();
        if (e.getInventory().getItem(2) == null) {

            ItemStack toEnchant = e.getInventory().getItem(0);
            ItemStack book = e.getInventory().getItem(1);

            if (toEnchant != null && book != null) {
                if (toEnchant.getType() != Material.ENCHANTED_BOOK && book.getType().equals(Material.ENCHANTED_BOOK)) {
                    ItemStack result = new ItemStack(toEnchant);
                    if (EnchantingSystem.containsCustomEnchantment(book)) {
                        CustomEnchantment customEnchantment = EnchantingSystem.getBookEnchant(book);

                        if (!EnchantingSystem.hasCustomEnchantment(result, customEnchantment)) {
                            int level = EnchantingSystem.getCustomEnchantmentLevel(book, customEnchantment);
                            EnchantingSystem.addCustomEnchantment(player, result, customEnchantment, level);

                            e.setResult(result);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onTakeOutItem(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (!event.getClickedInventory().getType().equals(InventoryType.ANVIL)) return;

        var player = (Player) event.getWhoClicked();
        var action = event.getAction();
        var inventory = event.getClickedInventory();
        if (event.getSlot() == 2 && action.equals(InventoryAction.NOTHING)) {
            //Take out item!

            var resultStack = event.getCurrentItem();
            if (event.isShiftClick()) {
                player.getInventory().addItem(resultStack);
            } else if ( event.getCursor() == null) {
                event.setCancelled(true);
                return;
            } else {
                event.getView().setCursor(resultStack);
            }

            if (inventory.getLocation() != null) {
                inventory.getLocation().getWorld().playSound(inventory.getLocation(), Sound.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1, 1);
            }
            inventory.setItem(0, null);
            inventory.setItem(1, null);

            var anvilEvent = new PrepareAnvilEvent(event.getView(), null);
            Bukkit.getPluginManager().callEvent(anvilEvent);

            inventory.setItem(2, null);
        }
    }
}
