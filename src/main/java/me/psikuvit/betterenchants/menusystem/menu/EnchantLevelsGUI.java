package me.psikuvit.betterenchants.menusystem.menu;

import me.psikuvit.betterenchants.Animation;
import me.psikuvit.betterenchants.BetterEnchants;
import me.psikuvit.betterenchants.menusystem.Menu;
import me.psikuvit.betterenchants.menusystem.PlayerMenuUtility;
import me.psikuvit.betterenchants.utils.AnimationsUtils;
import me.psikuvit.betterenchants.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EnchantLevelsGUI extends Menu {

    private final @NotNull Enchantment enchantment;
    private final @NotNull ItemStack itemStack;

    public EnchantLevelsGUI(@NotNull PlayerMenuUtility playerMenuUtility, BetterEnchants plugin, @NotNull Enchantment enchantment, @NotNull ItemStack itemStack) {
        super(playerMenuUtility, plugin);
        this.enchantment = enchantment;
        this.itemStack = itemStack;
    }

    @Override
    public String getMenuName() {
        return enchantment.getName() + " Levels";
    }

    @Override
    public int getSlots() {
        return 36;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta() || !clickedItem.getType().equals(Material.ENCHANTED_BOOK)) return;

        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) clickedItem.getItemMeta();

        int level = Objects.requireNonNull(meta).getStoredEnchantLevel(enchantment);

        itemStack.addEnchantment(enchantment, level);

        e.getWhoClicked().getInventory().addItem(itemStack);
        Animation animation = AnimationsUtils.getPlayerStands().get(e.getWhoClicked().getUniqueId());
        animation.updateArmor(itemStack);


        Messages.sendMessage(e.getWhoClicked(), "&bEnchant successfully added");
        e.getWhoClicked().closeInventory();
    }

    @Override
    public void setMenuItems() {
        setFillerGlass();
        int levels = enchantment.getMaxLevel();
        int centerSlot = (27 - levels) / 2;

        for (int i = 0; i < levels; i++) {
            ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) enchantedBook.getItemMeta();
            meta.addStoredEnchant(enchantment, i + 1, true);
            enchantedBook.setItemMeta(meta);

            inventory.setItem(centerSlot + i, enchantedBook);
        }
    }
}
