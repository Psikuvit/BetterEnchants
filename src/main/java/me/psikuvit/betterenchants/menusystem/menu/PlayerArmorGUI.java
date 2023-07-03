package me.psikuvit.betterenchants.menusystem.menu;

import me.psikuvit.betterenchants.Animation;
import me.psikuvit.betterenchants.BetterEnchants;
import me.psikuvit.betterenchants.menusystem.Menu;
import me.psikuvit.betterenchants.menusystem.PlayerMenuUtility;
import me.psikuvit.betterenchants.utils.AnimationsUtils;
import me.psikuvit.betterenchants.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PlayerArmorGUI extends Menu {

    private final @NotNull Player player = Objects.requireNonNull(Bukkit.getPlayer(playerMenuUtility.owner()));

    public PlayerArmorGUI(PlayerMenuUtility playerMenuUtility, BetterEnchants plugin) {
        super(playerMenuUtility, plugin);
    }

    @Override
    public String getMenuName() {
        return player.getName() + " Armor";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack itemStack = e.getCurrentItem();
        if (itemStack == null ||itemStack.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;

        if (itemStack.getType().equals(Material.BARRIER)) {
            e.getWhoClicked().closeInventory();
            return;
        }

        new PieceGUI(playerMenuUtility, plugin, itemStack).open(player);

    }

    @Override
    public void setMenuItems() {
        setFillerGlass();
        Player player = playerMenuUtility.getPlayer();
        Animation animation = AnimationsUtils.getPlayerStands().get(player.getUniqueId());
        inventory.setItem(13, animation.getArmor()[3]);
        inventory.setItem(22, animation.getArmor()[2]);
        inventory.setItem(31, animation.getArmor()[1]);
        inventory.setItem(40, animation.getArmor()[0]);
    }
}
