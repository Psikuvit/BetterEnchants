package me.psikuvit.betterenchants.menusystem.menu;

import me.psikuvit.betterenchants.Animation;
import me.psikuvit.betterenchants.BetterEnchants;
import me.psikuvit.betterenchants.EnchantingSystem;
import me.psikuvit.betterenchants.menusystem.Menu;
import me.psikuvit.betterenchants.menusystem.PlayerMenuUtility;
import me.psikuvit.betterenchants.utils.AnimationsUtils;
import me.psikuvit.betterenchants.utils.CustomEnchantment;
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

    private final @NotNull String enchantment;
    private final @NotNull ItemStack itemStack;

    public EnchantLevelsGUI(@NotNull PlayerMenuUtility playerMenuUtility, BetterEnchants plugin, @NotNull String enchantment, @NotNull ItemStack itemStack) {
        super(playerMenuUtility, plugin);
        this.enchantment = enchantment;
        this.itemStack = itemStack;
    }

    @Override
    public String getMenuName() {
        Enchantment ench = Enchantment.getByName(enchantment);
        if (ench != null) {
            return ench.getName() + " Levels";
        } else {
            CustomEnchantment customEnchantment = CustomEnchantment.valueOf(enchantment);
            return customEnchantment.name() + " Levels";
        }
    }

    @Override
    public int getSlots() {
        return 36;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta() || !clickedItem.getType().equals(Material.ENCHANTED_BOOK))
            return;

        if (clickedItem.getType().equals(Material.BARRIER)) e.getWhoClicked().closeInventory();

        Enchantment ench = Enchantment.getByName(enchantment);
        if (ench != null) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) clickedItem.getItemMeta();

            int level = Objects.requireNonNull(meta).getStoredEnchantLevel(ench);

            itemStack.addEnchantment(ench, level);

            Animation animation = AnimationsUtils.getInstance().getPlayerStands().get(e.getWhoClicked().getUniqueId());
            animation.updateArmorTools(itemStack);

        } else {
            CustomEnchantment customEnchantment = CustomEnchantment.valueOf(enchantment);

            int level = EnchantingSystem.getCustomEnchantmentLevel(clickedItem, customEnchantment);
            EnchantingSystem.addCustomEnchantment((Player) e.getWhoClicked(), itemStack, customEnchantment, level);

            Animation animation = AnimationsUtils.getInstance().getPlayerStands().get(e.getWhoClicked().getUniqueId());
            animation.updateArmorTools(itemStack);

        }
        Messages.sendMessage(e.getWhoClicked(), "&bEnchant successfully added");
        e.getWhoClicked().closeInventory();
    }

    @Override
    public void setMenuItems() {
        setFillerGlass();
        Enchantment ench = Enchantment.getByName(enchantment);
        if (ench != null) {
            int levels = ench.getMaxLevel();
            int centerSlot = (27 - levels) / 2;

            for (int i = 0; i < levels; i++) {
                ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK);
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) enchantedBook.getItemMeta();
                meta.addStoredEnchant(ench, i + 1, true);
                enchantedBook.setItemMeta(meta);

                inventory.setItem(centerSlot + i, enchantedBook);
            }
        } else {
            CustomEnchantment customEnchantment = CustomEnchantment.valueOf(enchantment);
            int levels = customEnchantment.getMaxLevel();
            int centerSlot = (27 - levels) / 2;

            for (int i = 0; i < levels; i++) {
                ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK);
                EnchantingSystem.addEnchantToBook(playerMenuUtility.getPlayer(), enchantedBook, customEnchantment, i + 1);

                inventory.setItem(centerSlot + i, enchantedBook);
            }
        }
    }
}
