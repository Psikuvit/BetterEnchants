package me.psikuvit.betterenchants.listeners;

import me.psikuvit.betterenchants.utils.AnimationsUtils;
import me.psikuvit.betterenchants.utils.EnchantUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (AnimationsUtils.getInstance().getPlayerStands().containsKey(player.getUniqueId())) {
            e.setCancelled(true);
            player.setFlySpeed(0);
            player.setWalkSpeed(0);
        } else {
            e.setCancelled(false);
            player.setWalkSpeed(0.2F);
            player.setFlySpeed(0.5F);
        }
    }
}
