package me.psikuvit.betterenchants.commands;

import me.psikuvit.betterenchants.BetterEnchants;
import org.bukkit.command.CommandSender;

import java.util.List;
public abstract class CommandAbstract {

    protected BetterEnchants plugin;
    public CommandAbstract(final BetterEnchants plugin) {
        this.plugin = plugin;
    }

    public abstract void executeCommand(final String[] args, final CommandSender sender);

    public abstract String correctArg();

    public abstract boolean onlyPlayer();

    public abstract int requiredArg();

    public abstract int bypassArgLimit();

    public abstract List<String> tabComplete(final String[] args);
}