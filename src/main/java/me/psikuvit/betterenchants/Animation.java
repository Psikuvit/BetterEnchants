package me.psikuvit.betterenchants;

import me.psikuvit.betterenchants.runnables.BobbingRunnable;
import me.psikuvit.betterenchants.runnables.ParticleRunnable;
import me.psikuvit.betterenchants.runnables.RotationRunnable;
import me.psikuvit.betterenchants.utils.AnimationsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class Animation {

    private final BetterEnchants plugin = BetterEnchants.getPlugin();
    private ArmorStand armorStand;
    private final Player player;
    private ItemStack[] armor;
    private boolean stopAnimation;
    private boolean pause;

    public Animation(Player player) {
        this.player = player;
        stopAnimation = false;
        pause = false;
        armor = player.getInventory().getArmorContents();
        createArmorStandAnimation();
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public boolean isStopAnimation() {
        return stopAnimation;
    }

    public void setStopAnimation(boolean stop) {
        this.stopAnimation = stop;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public void updateArmor(ItemStack itemStack) {
        ItemStack[] itemStacks = getArmor();
        if (itemStack.getType().getEquipmentSlot().equals(EquipmentSlot.HAND)) itemStacks[3] = itemStack;
        else if (itemStack.getType().getEquipmentSlot().equals(EquipmentSlot.CHEST)) itemStacks[2] = itemStack;
        else if (itemStack.getType().getEquipmentSlot().equals(EquipmentSlot.LEGS)) itemStacks[1] = itemStack;
        else if (itemStack.getType().getEquipmentSlot().equals(EquipmentSlot.FEET)) itemStacks[0] = itemStack;
        armor = itemStacks;

        armorStand.getEquipment().setArmorContents(armor);
    }

    public void createArmorStandAnimation() {
        // Create and spawn the armor stand
        Vector vec = player.getLocation().getDirection().multiply(2);
        armorStand = player.getWorld().spawn(player.getLocation().add(vec.getX(), 1.2, vec.getZ()), ArmorStand.class);
        armorStand.setGravity(false);
        armorStand.getEquipment().setArmorContents(armor);
        armorStand.setVisible(false);
        armorStand.setArms(false);

        player.getInventory().setArmorContents(null);

        // Play the sounds and start the blessing from inside the chest
        AnimationsUtils.playOpenSound(player, 1f, 0.793701f).runTaskLater(plugin, 5L);
        AnimationsUtils.playOpenSound(player, 1f, 0.890899f).runTaskLater(plugin, 10L);
        AnimationsUtils.playOpenSound(player, 1f, 1.0f).runTaskLater(plugin, 15L);
        AnimationsUtils.playOpenSound(player, 1f, 1.059463f).runTaskLater(plugin, 20L);
        AnimationsUtils.playOpenSound(player, 1f, 1.189207f).runTaskLater(plugin, 25L);
        AnimationsUtils.startFromBottom(armorStand);

        // Bobbing and rotation runnables
        new RotationRunnable(armorStand).runTaskTimer(plugin, 0, 0);
        new BobbingRunnable(armorStand, 0.3, 7).runTaskTimer(plugin, 20, 1);
        new ParticleRunnable(armorStand).runTaskTimer(plugin, 0, 10);

        // Despawn countdown (ew bad code, redo soon)
        new BukkitRunnable() {
            @Override
            public void run() {
                if (stopAnimation) {
                    armorStand.remove();
                    player.getInventory().setArmorContents(armor);
                    AnimationsUtils.getPlayerStands().remove(player.getUniqueId());
                    cancel();
                } else if (pause) {
                    armorStand.remove();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}
