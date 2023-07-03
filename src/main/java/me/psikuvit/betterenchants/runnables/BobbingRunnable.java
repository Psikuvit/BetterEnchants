package me.psikuvit.betterenchants.runnables;

import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

public class BobbingRunnable extends BukkitRunnable {

    private long currentTick = 0;
    private final ArmorStand armorStand;
    private final double amplitude;
    private final double frequency;
    private double lastYOffset = 0;

    public BobbingRunnable(ArmorStand armorStand, double amplitude, double frequency) {
        this.armorStand = armorStand;
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    @Override
    public void run() {
        if (armorStand.isDead())
            cancel();

        double yOffset = Math.sin(Math.toRadians(currentTick * frequency)) * amplitude;
        armorStand.teleport(armorStand.getLocation().add(0, (yOffset - lastYOffset)/*this is the difference*/, 0));
        lastYOffset = yOffset;
        currentTick++;
    }
}
