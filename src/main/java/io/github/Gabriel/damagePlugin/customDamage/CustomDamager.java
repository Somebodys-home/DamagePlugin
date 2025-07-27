package io.github.Gabriel.damagePlugin.customDamage;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import io.github.NoOne.nMLDefenses.DefenseType;
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

        if (targetStats != null) { // if damaging a player / has playerstats
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
        }

        double totalDamage = 0;
        if (!resistedTypes.isEmpty()) { // has A resistance
            for (Map.Entry<DamageType, Double> entry : damageSplits.entrySet()) {
                if (entry.getKey() != DamageType.PURE) {
                    entry.setValue(entry.getValue() - resistedTypes.get(DefenseType.DEFENSE));
                }

                switch (entry.getKey()) {
                    case PHYSICAL -> entry.setValue(entry.getValue() - resistedTypes.get(DefenseType.PHYSICALRESIST));
                    case FIRE -> entry.setValue(entry.getValue() - resistedTypes.get(DefenseType.FIRERESIST));
                    case COLD -> entry.setValue(entry.getValue() - resistedTypes.get(DefenseType.COLDRESIST));
                    case EARTH -> entry.setValue(entry.getValue() - resistedTypes.get(DefenseType.EARTHRESIST));
                    case LIGHTNING -> entry.setValue(entry.getValue() - resistedTypes.get(DefenseType.LIGHTNINGRESIST));
                    case AIR -> entry.setValue(entry.getValue() - resistedTypes.get(DefenseType.AIRRESIST));
                    case LIGHT -> entry.setValue(entry.getValue() - resistedTypes.get(DefenseType.LIGHTRESIST));
                    case DARK -> entry.setValue(entry.getValue() - resistedTypes.get(DefenseType.DARKRESIST));
                }
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

    // for single type damage
    public static void doDamage(LivingEntity target, LivingEntity damager, double damage, DamageType damageType) {
        Stats targetStats = new ProfileManager(DamagePlugin.getNmlPlayerStats()).getPlayerProfile(target.getUniqueId()).getStats();
        HashMap<DefenseType, Integer> resistedTypes = new HashMap<>();

        if (targetStats != null) { // if damaging a player / has playerstats
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