package me.psikuvit.betterenchants.commands.args;

import me.psikuvit.betterenchants.Animation;
import me.psikuvit.betterenchants.BetterEnchants;
import me.psikuvit.betterenchants.commands.CommandAbstract;
import me.psikuvit.betterenchants.menusystem.menu.PlayerArmorGUI;
import me.psikuvit.betterenchants.utils.AnimationsUtils;
import me.psikuvit.betterenchants.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AnimationGuiArg extends CommandAbstract {

    private final AnimationsUtils animationsUtils = AnimationsUtils.getInstance();

    public AnimationGuiArg(BetterEnchants plugin) {
        super(plugin);
    }

    @Override
    public void executeCommand(String[] args, CommandSender sender) {
        Player player = (Player) sender;
        if (!animationsUtils.getPlayerStands().containsKey(player.getUniqueId())) {
            Messages.sendMessage(player, "&cYou have no enchant session at the moment!.");
            return;
        }
        new PlayerArmorGUI(plugin.getPlayerMenuUtility(player), plugin).open(player);
    }

    @Override
    public String correctArg() {
        return "/be gui";
    }

    @Override
    public boolean onlyPlayer() {
        return true;
    }

    @Override
    public int requiredArg() {
        return 0;
    }

    @Override
    public int bypassArgLimit() {
        return 0;
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return null;
    }
}