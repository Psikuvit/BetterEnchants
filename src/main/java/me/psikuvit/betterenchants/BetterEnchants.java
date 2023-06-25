package me.psikuvit.betterenchants;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class BetterEnchants extends JavaPlugin {

    private static BetterEnchants plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        getCommand("lifesteal").setExecutor(this);
        Bukkit.getServer().getPluginManager().registerEvents(new EnchantListeners(), this);


    }

    public static BetterEnchants getPlugin() {
        return plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            EnchantingSystem.addCustomEnchantment(itemStack, EnchantingSystem.CustomEnchantment.LIFE_STEAL, 3);

        }
        return false;
    }

}
