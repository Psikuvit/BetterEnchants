package me.psikuvit.betterenchants.runnables;

import me.psikuvit.betterenchants.BetterEnchants;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class ParticleRunnable extends BukkitRunnable {

    private final ArmorStand armorStand;

    public ParticleRunnable(ArmorStand armorStand) {
        this.armorStand = armorStand;
    }
    @Override
    public void run() {
        if (armorStand.isDead()) cancel();

        Location loc = armorStand.getLocation();

        loc.add(0.0D, 1, 0.0D);
        for (double i = 0.0D; i <= Math.PI; i += 0.5235987755982988D) {
            double radius = Math.sin(i);
            double y = Math.cos(i) + 0.2;
            for (double a = 0.0D; a < 6.283185307179586D; a += 0.3141592653589793D) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                loc.add(x, y, z);
                loc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 1,0,0,1);
                loc.subtract(x, y, z);
            }
        }
    }
}
