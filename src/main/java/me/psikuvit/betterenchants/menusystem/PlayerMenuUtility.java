package me.psikuvit.betterenchants.menusystem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record PlayerMenuUtility(@NotNull UUID owner) {

    public Player getPlayer() {
        return Bukkit.getPlayer(owner);
    }
}

