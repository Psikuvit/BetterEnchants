package me.psikuvit.betterenchants.menusystem.menu;

import me.psikuvit.betterenchants.BetterEnchants;
import me.psikuvit.betterenchants.menusystem.PaginatedMenu;
import me.psikuvit.betterenchants.menusystem.PlayerMenuUtility;
import me.psikuvit.betterenchants.utils.CustomEnchantment;
import me.psikuvit.betterenchants.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PieceEnchantsGUI extends PaginatedMenu {

    private final List<Object> enchantmentList = new ArrayList<>();
    private final @NotNull ItemStack itemStack;

    public PieceEnchantsGUI(@NotNull PlayerMenuUtility playerMenuUtility, BetterEnchants plugin, @NotNull ItemStack itemStack) {
        super(playerMenuUtility, plugin);
        this.itemStack = itemStack;
    }

    @Override
    public String getMenuName() {
        return itemStack.getItemMeta().getDisplayName() + " Menu";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem().getType() == Material.BARRIER) {
            p.closeInventory();
        } else if (e.getCurrentItem().getType() == Material.DARK_OAK_BUTTON) {
            if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Left")) {
                if (page == 0) {
                    p.sendMessage(ChatColor.GRAY + "You are already on the first page.");
                } else {
                    page = page - 1;
                    super.open(p);
                }
            } else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Right")) {
                if (!((index + 1) >= enchantmentList.size())) {
                    page = page + 1;
                    super.open(p);
                } else {
                    p.sendMessage(ChatColor.GRAY + "You are on the last page.");
                }
            }
        }
        if (e.getCurrentItem().getType().equals(Material.ENCHANTED_BOOK)) {
            new EnchantLevelsGUI(playerMenuUtility, plugin, e.getCurrentItem().getItemMeta().getDisplayName(), itemStack).open(p);

        }
    }

    @Override
    public void setMenuItems() {
        addMenuBorder();
        for (Enchantment enchantment : Enchantment.values()) {
            if (enchantment.getItemTarget().includes(itemStack.getType())) enchantmentList.add(enchantment);
        }
        for (CustomEnchantment customEnchantment : CustomEnchantment.values()) {
            if (customEnchantment.getTarget().includes(itemStack.getType())) enchantmentList.add(customEnchantment);
        }
        for (int i = 0; i < getMaxItemsPerPage(); i++) {
            index = getMaxItemsPerPage() * page + i;
            if (index >= enchantmentList.size()) break;
            if (enchantmentList.get(index) != null) {
                ///////////////////////////
                if (enchantmentList.get(i) instanceof Enchantment enchantment) {
                    //Create an item from our collection and place it into the inventory
                    ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
                    ItemMeta itemMeta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType());
                    itemMeta.setDisplayName(enchantment.getName());

                    itemStack.setItemMeta(itemMeta);

                    inventory.addItem(itemStack);
                } else if (enchantmentList.get(i) instanceof CustomEnchantment customEnchantment) {
                    ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
                    ItemMeta itemMeta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType());
                    itemMeta.setDisplayName(customEnchantment.name());

                    itemStack.setItemMeta(itemMeta);

                    inventory.addItem(itemStack);
                }
            }
        }
    }
}
