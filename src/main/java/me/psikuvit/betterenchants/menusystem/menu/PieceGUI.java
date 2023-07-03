package me.psikuvit.betterenchants.menusystem.menu;

import me.psikuvit.betterenchants.BetterEnchants;
import me.psikuvit.betterenchants.menusystem.Menu;
import me.psikuvit.betterenchants.menusystem.PaginatedMenu;
import me.psikuvit.betterenchants.menusystem.PlayerMenuUtility;
import me.psikuvit.betterenchants.utils.EnchantUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PieceGUI extends PaginatedMenu {

    private final List<Enchantment> enchantmentList = new ArrayList<>();
    private final @NotNull ItemStack itemStack;
    public PieceGUI(@NotNull PlayerMenuUtility playerMenuUtility, BetterEnchants plugin, @NotNull ItemStack itemStack) {
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
            Enchantment enchantment = Enchantment.getByName(e.getCurrentItem().getItemMeta().getDisplayName());
            new EnchantLevelsGUI(playerMenuUtility, plugin, Objects.requireNonNull(enchantment), itemStack).open(p);
        }
    }

    @Override
    public void setMenuItems() {
        addMenuBorder();
        Enchantment[] enchantments = Enchantment.values();
        for (Enchantment enchantment : enchantments) {
            if (enchantment.getItemTarget().includes(itemStack.getType())) {
                enchantmentList.add(enchantment);
            }
        }
        for (int i = 0; i < getMaxItemsPerPage(); i++) {
            index = getMaxItemsPerPage() * page + i;
            if (index >= enchantmentList.size()) break;
            if (enchantmentList.get(index) != null) {
                ///////////////////////////
                Enchantment enchantment = enchantmentList.get(i);

                //Create an item from our collection and place it into the inventory
                ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta itemMeta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType());
                itemMeta.setDisplayName(enchantment.getName());

                itemStack.setItemMeta(itemMeta);

                inventory.addItem(itemStack);
            }
        }
    }
}
