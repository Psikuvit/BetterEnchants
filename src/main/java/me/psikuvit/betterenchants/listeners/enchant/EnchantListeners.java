package me.psikuvit.betterenchants.listeners.enchant;

import com.google.common.collect.ImmutableMap;
import me.psikuvit.betterenchants.BetterEnchants;
import me.psikuvit.betterenchants.EnchantingSystem;
import me.psikuvit.betterenchants.armorequip.ArmorEquipEvent;
import me.psikuvit.betterenchants.utils.CustomEnchantment;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class EnchantListeners implements Listener {

    private final Map<UUID, BukkitTask> playerTasks = new HashMap<>();
    private final Map<Material, Material> oreToIngot = new ImmutableMap.Builder<Material, Material>()
            .put(Material.IRON_ORE, Material.IRON_INGOT).put(Material.GOLD_ORE, Material.GOLD_INGOT).put(Material.DEEPSLATE_IRON_ORE, Material.IRON_INGOT)
            .put(Material.DEEPSLATE_GOLD_ORE, Material.GOLD_INGOT).build();

    @EventHandler //Life steal enchant listener
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player player) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();

            if (EnchantingSystem.hasCustomEnchantment(itemStack, CustomEnchantment.THUNDER_STRIKE)) {
                int level = EnchantingSystem.getCustomEnchantmentLevel(itemStack, CustomEnchantment.THUNDER_STRIKE);

                player.getWorld().strikeLightningEffect(e.getEntity().getLocation());
                Damageable damageable = (Damageable) e.getEntity();
                damageable.damage(0.35 * level);
            } else if (EnchantingSystem.hasCustomEnchantment(itemStack, CustomEnchantment.TRAP)) {
                new TrapRunnable(10 * 20, e.getEntity()).runTaskTimer(BetterEnchants.getPlugin(), 0, 1);

            } else if (EnchantingSystem.hasCustomEnchantment(itemStack, CustomEnchantment.LIFE_STEAL)) {
                int level = EnchantingSystem.getCustomEnchantmentLevel(itemStack, CustomEnchantment.LIFE_STEAL);

                double result = (0.05 * level + 0.05) * 2;
                double playerHealth = player.getHealth();
                double newHealth = result + playerHealth;
                if (newHealth > 20) {
                    newHealth = 20;
                }
                player.setHealth(newHealth);
            }
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (EnchantingSystem.hasCustomEnchantment(itemStack, CustomEnchantment.SHIELD)) {
            int level = EnchantingSystem.getCustomEnchantmentLevel(itemStack, CustomEnchantment.SHIELD);
            new ShieldRunnable(15 * 20, player, level).runTaskTimer(BetterEnchants.getPlugin(), 0, 1);
        }
    }
    @EventHandler
    public void onEquip(ArmorEquipEvent e) {
        ItemStack itemStack = e.getNewArmorPiece();

        if (EnchantingSystem.hasCustomEnchantment(itemStack, CustomEnchantment.WATER_BREATHING)) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, PotionEffect.INFINITE_DURATION, 200, false, false));
        } else {
            e.getPlayer().removePotionEffect(PotionEffectType.WATER_BREATHING);
        }
    }


    @EventHandler //Growth enchant
    public void onHold(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();

        // Stop the previous runnable if it exists
        BukkitTask task = playerTasks.get(playerUUID);
        if (task != null) task.cancel();


        ItemStack itemStack = e.getPlayer().getInventory().getItem(e.getNewSlot());
        if (EnchantingSystem.hasCustomEnchantment(itemStack, CustomEnchantment.GROWTH)) {

            int i = EnchantingSystem.getCustomEnchantmentLevel(itemStack, CustomEnchantment.GROWTH);

            BukkitTask newTask = new BukkitRunnable() {
                @Override
                public void run() {
                    // Get the player's updated location
                    Location center = player.getLocation();
                    int centerX = center.getBlockX();
                    int centerY = center.getBlockY();
                    int centerZ = center.getBlockZ();

                    for (int x = centerX - i; x <= centerX + i; x++) {
                        for (int y = centerY - i; y <= centerY + i; y++) {
                            for (int z = centerZ - i; z <= centerZ + i; z++) {
                                Location currentLocation = new Location(player.getWorld(), x, y, z);
                                Block block = currentLocation.getBlock();
                                if (block.getBlockData() instanceof Ageable ageable) {
                                    if (ageable.getAge() != ageable.getMaximumAge()) {
                                        ageable.setAge(ageable.getAge() + 1);
                                        block.setBlockData(ageable);
                                    }
                                }
                            }
                        }
                    }
                }
            }.runTaskTimer(BetterEnchants.getPlugin(), 0, 40); // 2 seconds (20 ticks = 1 second)
            playerTasks.put(playerUUID, newTask);

        } else if (EnchantingSystem.hasCustomEnchantment(itemStack, CustomEnchantment.TELEKINESIS)) {
            BukkitTask newTask = new BukkitRunnable() {
                @Override
                public void run() {
                    // Get the player's updated location
                    List<Entity> entityList = player.getNearbyEntities(4,4,4);
                    for (Entity entity : entityList) {
                        if (entity instanceof Item item) {
                            player.getInventory().addItem(item.getItemStack());
                        }
                    }
                }
            }.runTaskTimer(BetterEnchants.getPlugin(), 0, 2); // 2 seconds (20 ticks = 1 second)
            playerTasks.put(playerUUID, newTask);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (EnchantingSystem.hasCustomEnchantment(itemStack, CustomEnchantment.HARVESTING)) {

            if (block.getBlockData() instanceof Ageable) {
                Material mat = block.getBlockData().getMaterial();
                Bukkit.getScheduler().runTaskLater(BetterEnchants.getPlugin(), () -> block.setType(mat), 5);
            }
        } else if (EnchantingSystem.hasCustomEnchantment(itemStack, CustomEnchantment.AUTO_SMELT)) {
            if (oreToIngot.containsKey(block.getType())) {
                event.setCancelled(true);
                int dropAmount = 1;

                ItemStack hand = player.getItemInHand();
                if (!hand.containsEnchantment(Enchantment.SILK_TOUCH)) {
                    Material drop = oreToIngot.get(block.getType());
                    if (drop.equals(Material.AIR) || block.getDrops(hand).isEmpty())
                        return;

                    if (hand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                        Random rand = new Random();
                        dropAmount = rand.nextInt(hand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 1) + 1;
                    }

                    block.breakNaturally(new ItemStack(Material.AIR));
                    block.getWorld().dropItemNaturally(block.getLocation().add(0.5D, 0.5D, 0.5D), new ItemStack(drop, dropAmount));
                }
            }
        }
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        ItemStack itemStack = e.getBow();
        if (EnchantingSystem.hasCustomEnchantment(itemStack, CustomEnchantment.EXPLOSIVE_SHOTS)) {
            int level = EnchantingSystem.getCustomEnchantmentLevel(itemStack, CustomEnchantment.EXPLOSIVE_SHOTS);
            e.getProjectile().setCustomName("ExplosiveShot:" + level);
        }
    }
    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        if (e.getEntity().getCustomName() != null) {
            String[] strings = e.getEntity().getCustomName().split(":");
            if (strings[0].equals("ExplosiveShot")) {
                float level = Integer.parseInt(strings[1]);
                e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), level * 1.2F, false, false);
                e.getEntity().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, e.getEntity().getLocation(), 25, 0.5,0.5, 0.5);
            }
        }
    }
}