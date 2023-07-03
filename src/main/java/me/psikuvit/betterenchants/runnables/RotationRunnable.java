package me.psikuvit.betterenchants.runnables;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

public class RotationRunnable extends BukkitRunnable {

    private final ArmorStand armorStand;

    public RotationRunnable(ArmorStand armorStand) {
        this.armorStand = armorStand;
    }

    @Override
    public void run() {
        if (armorStand.isDead())
            cancel();
       rotateArmorStand(armorStand);

    }
    private void rotateArmorStand(ArmorStand armorStand) {
        Location location = armorStand.getLocation();
        float currentYaw = location.getYaw();
        float newYaw = (currentYaw + (float) 2) % 360;
        location.setYaw(newYaw);
        armorStand.teleport(location);
    }
}
