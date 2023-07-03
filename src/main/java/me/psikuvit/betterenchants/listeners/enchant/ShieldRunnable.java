package me.psikuvit.betterenchants.listeners.enchant;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ShieldRunnable extends BukkitRunnable {

    private final int durationTicks;
    private final Player player;
    private int ticksPassed = 0;
    private final int level;

    public ShieldRunnable(int durationTicks, Player player, int level) {
        this.durationTicks = durationTicks;
        this.player = player;
        this.level = level;
    }

    @Override
    public void run() {
        if (ticksPassed >= durationTicks) {
            cancel();
            return;
        }
        Location location = player.getLocation().add(0, 1, 0);
        List<Entity> entities = player.getNearbyEntities(5, 5, 5);
        for (double theta = 0; theta <= Math.PI; theta += Math.PI / 10) {
            double sinTheta = Math.sin(theta);
            double cosTheta = Math.cos(theta);

            for (double phi = 0; phi <= 2 * Math.PI; phi += Math.PI / 10) {
                double sinPhi = Math.sin(phi);
                double cosPhi = Math.cos(phi);

                double x = 1.2 * sinTheta * cosPhi;
                double y = 1.2 * sinTheta * sinPhi;
                double z = 1.2 * cosTheta;

                Location particleLocation = location.clone().add(x, y, z);
                player.getWorld().spawnParticle(Particle.WATER_WAKE, particleLocation, 0);
            }
        }
        for (Entity entity : entities) {
            if (entity instanceof Damageable damageable) {
                double result = 0.05 * level + 0.05;
                damageable.damage(result * 2);
                double playerHealth = player.getHealth();
                double newHealth = result * 2 + playerHealth;
                if (newHealth > 20) {
                    newHealth = 20;
                }
                player.setHealth(newHealth);
            }
        }
        ticksPassed++;
    }
}
