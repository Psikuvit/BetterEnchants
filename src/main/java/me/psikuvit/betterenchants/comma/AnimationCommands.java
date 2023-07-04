package me.psikuvit.betterenchants.comma;

import me.psikuvit.betterenchants.Animation;
import me.psikuvit.betterenchants.BetterEnchants;
import me.psikuvit.betterenchants.menusystem.menu.PlayerArmorGUI;
import me.psikuvit.betterenchants.utils.AnimationsUtils;
import me.psikuvit.betterenchants.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AnimationCommands implements CommandExecutor {

    private final BetterEnchants plugin = BetterEnchants.getPlugin();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) {
            switch (args[0]) {
                case "start" -> {
                    if (AnimationsUtils.getPlayerStands().containsKey(player.getUniqueId())) {
                        Messages.sendMessage(player, "&cYou already have an outgoing enchant session!.");
                        return true;
                    }
                    Animation animation = new Animation(player);
                    AnimationsUtils.getPlayerStands().put(player.getUniqueId(), animation);
                }
                case "stop" -> {
                    if (!AnimationsUtils.getPlayerStands().containsKey(player.getUniqueId())) {
                        Messages.sendMessage(player, "&cYou have no enchant session at the moment!.");
                        return true;
                    }
                    Animation animation = AnimationsUtils.getPlayerStands().get(player.getUniqueId());
                    animation.setStopAnimation(true);
                }
                case "gui" -> {
                    if (!AnimationsUtils.getPlayerStands().containsKey(player.getUniqueId())) {
                        Messages.sendMessage(player, "&cYou have no enchant session at the moment!.");
                        return true;
                    }
                    new PlayerArmorGUI(plugin.getPlayerMenuUtility(player), plugin).open(player);
                }
            }
        }
        return false;
    }
}
