package io.github.Gabriel.damagePlugin.customDamage;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import io.github.NoOne.nMLDefenses.DefenseType;
import io.github.NoOne.nMLPlayerStats.profileSystem.Profile;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomDamager {
    private static DamagePlugin plugin;
    public record DamageInstance(DamageType type, double damage) {}
    private static final Map<UUID, DamageInstance> damageInstance = new HashMap<>();

    public CustomDamager(DamagePlugin plugin) {
        this.plugin = plugin;
    }

    // todo: get rid of damage messages eventually
    // for multiple damage types
    public static void doDamage(LivingEntity target, LivingEntity damager, Map<DamageType, Double> damageSplits) {
        Stats targetStats = new ProfileManager(plugin.getNmlPlayerStats()).getPlayerProfile(target.getUniqueId()).getStats();
        HashMap<DefenseType, Integer> resistedTypes = new HashMap<>();

        if (targetStats != null) {
            int evasion = targetStats.getEvasion();
            int random = (int) (Math.random() * 100) + 1;

            if (random <= evasion || evasion >= 100) {
                target.sendMessage("your evasion triggered! (" + evasion + "%)");
            } else {
                if (targetStats.getDefense() != 0) {
                    resistedTypes.put(DefenseType.DEFENSE, targetStats.getDefense());
                }
                if (targetStats.getPhysicalResist() != 0) {
                    resistedTypes.put(DefenseType.PHYSICALRESIST, targetStats.getPhysicalResist());
                }
                if (targetStats.getFireResist() != 0) {
                    resistedTypes.put(DefenseType.FIRERESIST, targetStats.getFireResist());
                }
                if (targetStats.getColdResist() != 0) {
                    resistedTypes.put(DefenseType.COLDRESIST, targetStats.getColdResist());
                }
                if (targetStats.getEarthResist() != 0) {
                    resistedTypes.put(DefenseType.EARTHRESIST, targetStats.getEarthResist());
                }
                if (targetStats.getLightningResist() != 0) {
                    resistedTypes.put(DefenseType.LIGHTNINGRESIST, targetStats.getLightningResist());
                }
                if (targetStats.getAirResist() != 0) {
                    resistedTypes.put(DefenseType.AIRRESIST, targetStats.getAirResist());
                }
                if (targetStats.getLightResist() != 0) {
                    resistedTypes.put(DefenseType.LIGHTRESIST, targetStats.getLightResist());
                }
                if (targetStats.getDarkResist() != 0) {
                    resistedTypes.put(DefenseType.DARKRESIST, targetStats.getDarkResist());
                }

                double totalDamage = 0;
                if (!resistedTypes.isEmpty()) { // has A resistance
                    for (Map.Entry<DamageType, Double> entry : damageSplits.entrySet()) {
                        double value = entry.getValue();

                        if (entry.getKey() != DamageType.PURE) {
                            value -= resistedTypes.getOrDefault(DefenseType.DEFENSE, 0);
                        }

                        switch (entry.getKey()) {
                            case PHYSICAL -> value -= resistedTypes.getOrDefault(DefenseType.PHYSICALRESIST, 0);
                            case FIRE -> value -= resistedTypes.getOrDefault(DefenseType.FIRERESIST, 0);
                            case COLD -> value -= resistedTypes.getOrDefault(DefenseType.COLDRESIST, 0);
                            case EARTH -> value -= resistedTypes.getOrDefault(DefenseType.EARTHRESIST, 0);
                            case LIGHTNING -> value -= resistedTypes.getOrDefault(DefenseType.LIGHTNINGRESIST, 0);
                            case AIR -> value -= resistedTypes.getOrDefault(DefenseType.AIRRESIST, 0);
                            case LIGHT -> value -= resistedTypes.getOrDefault(DefenseType.LIGHTRESIST, 0);
                            case DARK -> value -= resistedTypes.getOrDefault(DefenseType.DARKRESIST, 0);
                        }

                        value = Math.max(0, value); // Ensure no negative damage
                        entry.setValue(value);
                    }
                }

                for (Map.Entry<DamageType, Double> entry : damageSplits.entrySet()) {
                    damageInstance.put(target.getUniqueId(), new DamageInstance(entry.getKey(), entry.getValue()));
                    totalDamage += entry.getValue();

                    if (damager instanceof Player player && entry.getValue() > 0) {
                        player.sendMessage(DamageType.getDamageColor(entry.getKey()) + "You did " + entry.getValue() + " " + DamageType.getDamageString(entry.getKey()) + " damage!");
                    }
                }

                target.setMetadata("recursive_block", new FixedMetadataValue(plugin, true));
                target.damage(totalDamage, damager);
            }
        }
    }

    // for single type damage
    public static void doDamage(LivingEntity target, LivingEntity damager, double damage, DamageType damageType) {
        Profile targetProfile = new ProfileManager(DamagePlugin.getNmlPlayerStats()).getPlayerProfile(target.getUniqueId());
        HashMap<DefenseType, Integer> resistedTypes = new HashMap<>();

        if (targetProfile != null) { // if damaging a player / has playerstats
            Stats targetStats = targetProfile.getStats();
            int evasion = targetStats.getEvasion();
            int random = (int) (Math.random() * 100) + 1;

            if (random <= evasion || evasion >= 100) {
                target.sendMessage("your evasion triggered! (" + evasion + "%)");
            } else {
                if (targetStats.getDefense() != 0) {
                    resistedTypes.put(DefenseType.DEFENSE, targetStats.getDefense());
                }
                if (targetStats.getPhysicalResist() != 0) {
                    resistedTypes.put(DefenseType.PHYSICALRESIST, targetStats.getPhysicalResist());
                }
                if (targetStats.getFireResist() != 0) {
                    resistedTypes.put(DefenseType.FIRERESIST, targetStats.getFireResist());
                }
                if (targetStats.getColdResist() != 0) {
                    resistedTypes.put(DefenseType.COLDRESIST, targetStats.getColdResist());
                }
                if (targetStats.getEarthResist() != 0) {
                    resistedTypes.put(DefenseType.EARTHRESIST, targetStats.getEarthResist());
                }
                if (targetStats.getLightningResist() != 0) {
                    resistedTypes.put(DefenseType.LIGHTNINGRESIST, targetStats.getLightningResist());
                }
                if (targetStats.getAirResist() != 0) {
                    resistedTypes.put(DefenseType.AIRRESIST, targetStats.getAirResist());
                }
                if (targetStats.getLightResist() != 0) {
                    resistedTypes.put(DefenseType.LIGHTRESIST, targetStats.getLightResist());
                }
                if (targetStats.getDarkResist() != 0) {
                    resistedTypes.put(DefenseType.DARKRESIST, targetStats.getDarkResist());
                }

                if (!resistedTypes.isEmpty()) { // has A resistance
                    if (damageType != DamageType.PURE) {
                        damage -= resistedTypes.get(DefenseType.DEFENSE);
                    }

                    switch (damageType) {
                        case PHYSICAL -> damage -= resistedTypes.get(DefenseType.DEFENSE);
                        case FIRE -> damage -= resistedTypes.get(DefenseType.DEFENSE);
                        case COLD -> damage -= resistedTypes.get(DefenseType.DEFENSE);
                        case EARTH -> damage -= resistedTypes.get(DefenseType.DEFENSE);
                        case LIGHTNING -> damage -= resistedTypes.get(DefenseType.DEFENSE);
                        case AIR -> damage -= resistedTypes.get(DefenseType.DEFENSE);
                        case LIGHT -> damage -= resistedTypes.get(DefenseType.DEFENSE);
                        case DARK -> damage -= resistedTypes.get(DefenseType.DEFENSE);
                    }
                }

                damageInstance.put(target.getUniqueId(), new DamageInstance(damageType, damage));

                if (damager instanceof Player player) {
                    player.sendMessage(DamageType.getDamageColor(damageType) + "You did " + damage + " " + DamageType.getDamageString(damageType) + " damage!");
                }

                target.setMetadata("recursive_block", new FixedMetadataValue(plugin, true));
                target.damage(damage, damager);
            }
        }
    }
}