package me.psikuvit.betterenchants;

import me.psikuvit.betterenchants.armorequip.ArmorListener;
import me.psikuvit.betterenchants.armorequip.DispenserArmorListener;
import me.psikuvit.betterenchants.commands.EnchantStartCMD;
import me.psikuvit.betterenchants.listeners.enchant.EnchantListeners;
import me.psikuvit.betterenchants.listeners.InventoryClickEventListener;
import me.psikuvit.betterenchants.listeners.MoveListener;
import me.psikuvit.betterenchants.listeners.QuitJoinListener;
import me.psikuvit.betterenchants.menusystem.PlayerMenuUtility;
import me.psikuvit.betterenchants.utils.AnimationsUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class BetterEnchants extends JavaPlugin {

    private static BetterEnchants plugin;
    private final HashMap<UUID, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ArmorListener(), this);
        try{
            //Better way to check for this? Only in 1.13.1+?
            Class.forName("org.bukkit.event.block.BlockDispenseArmorEvent");
           pm.registerEvents(new DispenserArmorListener(), this);
        }catch(Exception ignored){}

        pm.registerEvents(new EnchantListeners(), this);
        pm.registerEvents(new InventoryClickEventListener(), this);
        pm.registerEvents(new MoveListener(), this);
        pm.registerEvents(new QuitJoinListener(), this);

        getCommand("betterenchants").setExecutor(new EnchantStartCMD());
    }

    @Override
    public void onDisable() {
        if (!AnimationsUtils.getPlayerStands().isEmpty()) {
            for (Animation animation : AnimationsUtils.getPlayerStands().values()) animation.setStopAnimation(true);
        }
}

    public static BetterEnchants getPlugin() {
        return plugin;
    }

    public PlayerMenuUtility getPlayerMenuUtility(Player p) {
        return playerMenuUtilityMap.computeIfAbsent(p.getUniqueId(), PlayerMenuUtility::new);
    }
}
