package me.psikuvit.betterenchants.listeners;

import me.psikuvit.betterenchants.Animation;
import me.psikuvit.betterenchants.utils.AnimationsUtils;
import me.psikuvit.betterenchants.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitJoinListener implements Listener {

    private final AnimationsUtils animationsUtils = AnimationsUtils.getInstance();

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (animationsUtils.getPlayerStands().containsKey(player.getUniqueId())) {
            Animation animation = animationsUtils.getPlayerStands().get(player.getUniqueId());
            animation.setPause(true);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (animationsUtils.getPlayerStands().containsKey(player.getUniqueId())) {
            Animation animation = animationsUtils.getPlayerStands().get(player.getUniqueId());
            animation.setPause(false);
            animation.createArmorStandAnimation();
        }
    }
}
