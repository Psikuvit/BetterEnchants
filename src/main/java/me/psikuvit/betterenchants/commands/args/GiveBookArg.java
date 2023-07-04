package me.psikuvit.betterenchants.commands.args;

import me.psikuvit.betterenchants.BetterEnchants;
import me.psikuvit.betterenchants.EnchantingSystem;
import me.psikuvit.betterenchants.commands.CommandAbstract;
import me.psikuvit.betterenchants.utils.CustomEnchantment;
import me.psikuvit.betterenchants.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GiveBookArg extends CommandAbstract {
    public GiveBookArg(BetterEnchants plugin) {
        super(plugin);
    }

    @Override
    public void executeCommand(String[] args, CommandSender sender) {
        Player player = (Player) sender;
        Player toGive = Bukkit.getPlayer(args[0]);
        if (toGive == null) {
            Messages.sendMessage(player, "Couldn't find this player");
            return;
        }
        try {

            CustomEnchantment customEnchantment = CustomEnchantment.valueOf(args[1].toUpperCase());
            ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantingSystem.addEnchantToBook(player, itemStack, customEnchantment, Integer.parseInt(args[2]));
            player.getInventory().addItem(itemStack);


        } catch (IllegalArgumentException e) {
            Messages.sendMessage(player, "&cEnchant doesn't exist");
        }

    }

    @Override
    public String correctArg() {
        return "/be give <player> <enchantment> <level>";
    }

    @Override
    public boolean onlyPlayer() {
        return true;
    }

    @Override
    public int requiredArg() {
        return 3;
    }

    @Override
    public int bypassArgLimit() {
        return 0;
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 3) {
            List<String> completions = new ArrayList<>();

            for (CustomEnchantment customEnchantment : CustomEnchantment.values()) completions.add(customEnchantment.name());
            completions.removeIf(cmdAlias -> !cmdAlias.toLowerCase().startsWith(args[2].toLowerCase()));

            return completions;
        } else if (args.length == 4) {
            List<String> levels = new ArrayList<>();
            try {
                CustomEnchantment customEnchantment = CustomEnchantment.valueOf(args[2]);
                for (int i = 1; i <= customEnchantment.getMaxLevel(); i++) levels.add(String.valueOf(i));
                return levels;

            }catch (IllegalArgumentException ignored) {
                return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }
}
