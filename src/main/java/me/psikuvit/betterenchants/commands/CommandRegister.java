package me.psikuvit.betterenchants.commands;

import me.psikuvit.betterenchants.BetterEnchants;
import me.psikuvit.betterenchants.commands.args.ApplyEnchantArg;
import me.psikuvit.betterenchants.commands.args.GiveBookArg;
import me.psikuvit.betterenchants.utils.Messages;
import me.psikuvit.betterenchants.commands.CommandAbstract;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandRegister implements CommandExecutor, TabCompleter {
    private final Map<String, CommandAbstract> commandAbstractMap;

    public CommandRegister(final BetterEnchants plugin) {
        commandAbstractMap = new HashMap<>();
        commandAbstractMap.put("enchant", new ApplyEnchantArg(plugin));
        commandAbstractMap.put("book", new GiveBookArg(plugin));

    }

    public boolean onCommand(final @NotNull CommandSender commandSender, final @NotNull Command command, final @NotNull String label, String[] args) {
        if (args.length == 0) {
            return true;
        }
        final String subCommand = args[0];
        boolean found = false;
        for (final String cmdAlias : this.commandAbstractMap.keySet()) {
            if (cmdAlias.equalsIgnoreCase(subCommand)) {
                final int argsCount = args.length - 1;
                final boolean isSenderPlayer = commandSender instanceof Player;
                final CommandAbstract cmd = this.commandAbstractMap.get(cmdAlias);
                if (cmd.bypassArgLimit() == 0) {
                    if (argsCount > cmd.requiredArg()) {
                        commandSender.sendMessage(Messages.color("§cCorrect usage: §e" + cmd.correctArg()));
                        return true;
                    }
                    if (argsCount < cmd.requiredArg()) {
                        commandSender.sendMessage(Messages.color("§cCorrect usage: §e" + cmd.correctArg()));
                        return true;
                    }
                } else if (cmd.bypassArgLimit() > 0) {
                    if (argsCount < cmd.bypassArgLimit()) {
                        commandSender.sendMessage(Messages.color("§cCorrect usage: §e" + cmd.correctArg()));
                        return true;
                    }
                }
                if (!isSenderPlayer && cmd.onlyPlayer()) {
                    commandSender.sendMessage(Messages.color("&cMust be a player"));
                    return true;
                }
                args = this.move(args);
                cmd.executeCommand(args, commandSender);
                found = true;
                break;
            }
        }
        if (!found) {
            commandSender.sendMessage(Messages.color("&cIncorrect command"));
        }
        return true;
    }

    private String[] move(final String[] args) {
        final String[] result = new String[args.length - 1];
        System.arraycopy(args, 1, result, 0, args.length - 1);
        return result;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            // Return available sub-commands as tab completions
            List<String> completions = new ArrayList<>(commandAbstractMap.keySet());
            completions.removeIf(cmdAlias -> !cmdAlias.toLowerCase().startsWith(args[0].toLowerCase()));
            return completions;
        } else if (args.length > 1) {
            // Retrieve the CommandAbstract associated with the given sub-command
            CommandAbstract cmd = commandAbstractMap.get(args[0].toLowerCase());
            if (cmd != null) {
                // Delegate tab completion to the associated CommandAbstract
                return cmd.tabComplete(args);
            }
        }
        return Collections.emptyList();
    }
}
