package me.psikuvit.betterenchants.commands;

import me.psikuvit.betterenchants.Animation;
import me.psikuvit.betterenchants.BetterEnchants;
import me.psikuvit.betterenchants.EnchantingSystem;
import me.psikuvit.betterenchants.menusystem.menu.PlayerArmorGUI;
import me.psikuvit.betterenchants.utils.AnimationsUtils;
import me.psikuvit.betterenchants.utils.CustomEnchantment;
import me.psikuvit.betterenchants.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EnchantStartCMD implements CommandExecutor {

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
        } else if (args.length == 3) {
            if (args[0].equals("enchant")) {
                int i;
                try {
                    i = Integer.parseInt(args[2]);

                    CustomEnchantment customEnchantment = CustomEnchantment.valueOf(args[1].toUpperCase());
                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                    EnchantingSystem.addCustomEnchantment(player, itemStack, customEnchantment, i);

                } catch (IllegalArgumentException e) {
                    Messages.sendMessage(player, "&cEnchant doesn't exist");
                }
            }
        }
        return false;
    }
}
