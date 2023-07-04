package me.psikuvit.betterenchants.commands.args;

import me.psikuvit.betterenchants.Animation;
import me.psikuvit.betterenchants.BetterEnchants;
import me.psikuvit.betterenchants.commands.CommandAbstract;
import me.psikuvit.betterenchants.utils.AnimationsUtils;
import me.psikuvit.betterenchants.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AnimationStartArg extends CommandAbstract {

    private final AnimationsUtils animationsUtils = AnimationsUtils.getInstance();

    public AnimationStartArg(BetterEnchants plugin) {
        super(plugin);
    }

    @Override
    public void executeCommand(String[] args, CommandSender sender) {
        Player player = (Player) sender;
        if (animationsUtils.getPlayerStands().containsKey(player.getUniqueId())) {
            Messages.sendMessage(player, "&cYou already have an outgoing enchant session!.");
            return;
        }
        Animation animation = new Animation(player);
        animationsUtils.getPlayerStands().put(player.getUniqueId(), animation);
    }

    @Override
    public String correctArg() {
        return "/be start";
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
