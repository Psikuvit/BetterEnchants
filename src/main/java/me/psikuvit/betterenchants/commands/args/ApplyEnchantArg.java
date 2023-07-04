package me.psikuvit.betterenchants.commands.args;

import me.psikuvit.betterenchants.BetterEnchants;
import me.psikuvit.betterenchants.EnchantingSystem;
import me.psikuvit.betterenchants.commands.CommandAbstract;
import me.psikuvit.betterenchants.utils.CustomEnchantment;
import me.psikuvit.betterenchants.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplyEnchantArg extends CommandAbstract {
    public ApplyEnchantArg(BetterEnchants plugin) {
        super(plugin);
    }

    @Override
    public void executeCommand(String[] args, CommandSender sender) {
        Player player = (Player) sender;
        try {

            CustomEnchantment customEnchantment = CustomEnchantment.valueOf(args[0].toUpperCase());
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            EnchantingSystem.addCustomEnchantment(player, itemStack, customEnchantment, Integer.parseInt(args[1]));

        } catch (IllegalArgumentException e) {
            Messages.sendMessage(player, "&cEnchant doesn't exist");
        }
    }

    @Override
    public String correctArg() {
        return "/be enchant <enchantment> <level>";
    }

    @Override
    public boolean onlyPlayer() {
        return true;
    }

    @Override
    public int requiredArg() {
        return 2;
    }

    @Override
    public int bypassArgLimit() {
        return 0;
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 2) {
            List<String> completions = new ArrayList<>();

            for (CustomEnchantment customEnchantment : CustomEnchantment.values()) completions.add(customEnchantment.name());
            completions.removeIf(cmdAlias -> !cmdAlias.toLowerCase().startsWith(args[1].toLowerCase()));

            return completions;
        } else if (args.length == 3) {
            List<String> levels = new ArrayList<>();
            try {
                CustomEnchantment customEnchantment = CustomEnchantment.valueOf(args[1]);
                for (int i = 1; i <= customEnchantment.getMaxLevel(); i++) levels.add(String.valueOf(i));
                return levels;

            }catch (IllegalArgumentException ignored) {
                return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }
}
