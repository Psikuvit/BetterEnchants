package me.psikuvit.betterenchants.utils;

import me.psikuvit.betterenchants.BetterEnchants;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public enum CustomEnchantment {
    // Define your custom enchantments here
    SHIELD("Shield", 3, Target.WEAPON, "SHIELD"),
    GROWTH("Growth", 5, Target.HOE, "GROWTH"),
    HARVESTING("Harvesting", 1, Target.HOE, "HARVESTING"),
    THUNDER_STRIKE("Thunder Strike", 3, Target.AXE, "THUNDER_STRIKE"),
    TRAP("Trap", 1, Target.WEAPON, "TRAP"),
    AUTO_SMELT("Auto Smelt", 1, Target.PICKAXE, "AUTO_SMELT"),
    WATER_BREATHING("Water Breathing", 1, Target.ARMOR_HEAD, "WATER_BREATHING", Enchantment.OXYGEN),
    TELEKINESIS("Telekinesis", 1, Target.TOOLS, "TELEKINESIS"),
    EXPLOSIVE_SHOTS("Explosive Shots", 5, Target.BOW, "EXPLOSIVE_SHOTS"),
    LIFE_STEAL("Life Steal", 3, Target.WEAPON, "LIFE_STEAL");


    private final String displayName;
    private final int maxLevel;
    private final Target target;
    private final NamespacedKey key;
    private final Enchantment toReplace;

    CustomEnchantment(String displayName, int maxLevel, Target target, String key) {
        this(displayName, maxLevel, target, key, null);
    }

    CustomEnchantment(String displayName, int maxLevel, Target target, String key, Enchantment toReplace) {
        this.displayName = displayName;
        this.maxLevel = maxLevel;
        this.target = target;
        this.key = new NamespacedKey(BetterEnchants.getPlugin(), key);
        this.toReplace = toReplace;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public Target getTarget() {
        return target;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public Enchantment getToReplace() {
        return toReplace;
    }

    @Override
    public String toString() {
        return "CustomEnchantment{" +
                "displayName='" + displayName + '\'' +
                ", maxLevel=" + maxLevel +
                ", target=" + target +
                ", key=" + key +
                ", toReplace=" + toReplace +
                '}';
    }
}