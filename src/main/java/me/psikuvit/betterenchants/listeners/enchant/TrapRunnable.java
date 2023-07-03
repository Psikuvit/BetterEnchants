package me.psikuvit.betterenchants.listeners.enchant;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TrapRunnable extends BukkitRunnable {

    private final int durationTicks;
    private final Entity entity;
    private int ticksPassed = 0;
    private final ArmorStand armorStand;
    
    public TrapRunnable(int durationTicks, Entity entity) {
        this.durationTicks = durationTicks;
        this.entity = entity;
        armorStand = entity.getWorld().spawn(entity.getLocation().subtract(0, 1.4, 0), ArmorStand.class);
        armorStand.setInvisible(true);
        armorStand.setPassenger(entity);
    }
    
    @Override
    public void run() {
        if (ticksPassed >= durationTicks) {
            armorStand.remove();
            cancel(); // Animation finished, cancel the task
            return;
        }

        Location location = entity.getLocation();

        double width = entity.getWidth(); // Get the width of the entity
        double height = entity.getHeight(); // Get the height of the entity

        // Calculate the coordinates for the corners of the box-shaped hitbox
        double minX = location.getX() - width / 2;
        double maxX = location.getX() + width / 2;
        double minY = location.getY();
        double maxY = location.getY() + height;
        double minZ = location.getZ() - width / 2;
        double maxZ = location.getZ() + width / 2;

        Location backLeftLow = new Location(entity.getWorld(), minX, minY, minZ), backRightLow = new Location(entity.getWorld(), maxX, minY, minZ),
                backLeftHigh = new Location(entity.getWorld(), minX, maxY, minZ), backRightHigh = new Location(entity.getWorld(), maxX, maxY, minZ),
                frontLeftLow = new Location(entity.getWorld(), minX, minY, maxZ), frontRightLow = new Location(entity.getWorld(), maxX, minY, maxZ),
                frontLeftHigh = new Location(entity.getWorld(), minX, maxY, maxZ), frontRightHigh = new Location(entity.getWorld(), maxX, maxY, maxZ);

        // back corners
        spawnCornerParticles(entity, backLeftLow); // left low corner
        spawnCornerParticles(entity, backLeftHigh); // right low corner
        spawnCornerParticles(entity, backRightLow); // left high corner
        spawnCornerParticles(entity, backRightHigh); // right high corner

        //front corners
        spawnCornerParticles(entity, frontLeftLow); // left low corner
        spawnCornerParticles(entity, frontLeftHigh); // right low corner
        spawnCornerParticles(entity, frontRightLow); // left high corner
        spawnCornerParticles(entity, frontRightHigh); // right high corner

        linkCorners(entity, backLeftLow, backLeftHigh);
        linkCorners(entity, backRightLow, backRightHigh);
        linkCorners(entity, backLeftLow, backRightLow);
        linkCorners(entity, backLeftHigh, backRightHigh);

        linkCorners(entity, frontLeftLow, frontLeftHigh);
        linkCorners(entity, frontRightLow, frontRightHigh);
        linkCorners(entity, frontLeftLow, frontRightLow);
        linkCorners(entity, frontLeftHigh, frontRightHigh);

        linkCorners(entity, backLeftLow, frontLeftLow);
        linkCorners(entity, backRightLow, frontRightLow);
        linkCorners(entity, backLeftHigh, frontLeftHigh);
        linkCorners(entity, backRightHigh, frontRightHigh);

        ticksPassed++;
    }

    private void spawnCornerParticles(Entity entity, Location location) {
        // Spawn particles for the player to see
        entity.getWorld().spawnParticle(Particle.FLAME, location, 0, 0, 0, 0);
    }

    private void linkCorners(Entity entity, Location location1, Location location2) {
        // Calculate the number of particles needed to connect the corners
        int particleCount = 10; // Adjust this value for more or fewer particles

        // Calculate the distance between the two corners
        double distanceX = (location2.getX() - location1.getX()) / particleCount;
        double distanceY = (location2.getY() - location1.getY()) / particleCount;
        double distanceZ = (location2.getZ() - location1.getZ()) / particleCount;

        // Spawn particles between the corners to create a linked effect
        for (int i = 0; i < particleCount; i++) {
            double offsetX = location1.getX() + (distanceX * i);
            double offsetY = location1.getY() + (distanceY * i);
            double offsetZ = location1.getZ() + (distanceZ * i);

            entity.getWorld().spawnParticle(Particle.FLAME, offsetX, offsetY, offsetZ, 0, 0, 0, 0);
        }
    }
}
