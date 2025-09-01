package io.github.Gabriel.damagePlugin.customDamage;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import io.github.NoOne.nMLItems.ItemStat;
import io.github.NoOne.nMLPlayerStats.profileSystem.Profile;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomDamager {
    private static DamagePlugin damagePlugin;
    public record DamageInstance(DamageType type, double damage) {}
    private static final Map<UUID, DamageInstance> damageInstanceMap = new HashMap<>();

    public CustomDamager(DamagePlugin damagePlugin) {
        this.damagePlugin = damagePlugin;
    }

    // todo: get rid of damage messages eventually
    public static void doDamage(LivingEntity target, LivingEntity damager, Map<DamageType, Double> damageSplits) {
        Profile targetProfile = new ProfileManager(DamagePlugin.getNmlPlayerStats()).getPlayerProfile(target.getUniqueId());

        // for things without a profile, like vanilla mobs
        if (targetProfile == null) {
            applyDamage(target, damager, damageSplits);
            return;
        }

        Stats targetStats = targetProfile.getStats();
        HashMap<ItemStat, Integer> resistedTypes = new HashMap<>();

        // evasion
        int evasion = targetStats.getEvasion();
        int random = (int) (Math.random() * 100) + 1;
        if (random <= evasion || evasion >= 100) {
            target.sendMessage("your evasion triggered! (" + evasion + "%)");
            return;
        }

        // elemental resistances
        if (targetStats.getDefense() != 0) {
            resistedTypes.put(ItemStat.DEFENSE, targetStats.getDefense());
        }
        if (targetStats.getPhysicalResist() != 0) {
            resistedTypes.put(ItemStat.PHYSICALRESIST, targetStats.getPhysicalResist());
        }
        if (targetStats.getFireResist() != 0) {
            resistedTypes.put(ItemStat.FIRERESIST, targetStats.getFireResist());
        }
        if (targetStats.getColdResist() != 0) {
            resistedTypes.put(ItemStat.COLDRESIST, targetStats.getColdResist());
        }
        if (targetStats.getEarthResist() != 0) {
            resistedTypes.put(ItemStat.EARTHRESIST, targetStats.getEarthResist());
        }
        if (targetStats.getLightningResist() != 0) {
            resistedTypes.put(ItemStat.LIGHTNINGRESIST, targetStats.getLightningResist());
        }
        if (targetStats.getAirResist() != 0) {
            resistedTypes.put(ItemStat.AIRRESIST, targetStats.getAirResist());
        }
        if (targetStats.getLightResist() != 0) {
            resistedTypes.put(ItemStat.LIGHTRESIST, targetStats.getLightResist());
        }
        if (targetStats.getDarkResist() != 0) {
            resistedTypes.put(ItemStat.DARKRESIST, targetStats.getDarkResist());
        }

        if (!resistedTypes.isEmpty()) { // has any resistances
            for (Map.Entry<DamageType, Double> entry : damageSplits.entrySet()) {
                double value = entry.getValue();

                if (entry.getKey() != DamageType.PURE) {
                    value -= resistedTypes.getOrDefault(ItemStat.DEFENSE, 0);
                }

                switch (entry.getKey()) {
                    case PHYSICAL -> value -= resistedTypes.getOrDefault(ItemStat.PHYSICALRESIST, 0);
                    case FIRE -> value -= resistedTypes.getOrDefault(ItemStat.FIRERESIST, 0);
                    case COLD -> value -= resistedTypes.getOrDefault(ItemStat.COLDRESIST, 0);
                    case EARTH -> value -= resistedTypes.getOrDefault(ItemStat.EARTHRESIST, 0);
                    case LIGHTNING -> value -= resistedTypes.getOrDefault(ItemStat.LIGHTNINGRESIST, 0);
                    case AIR -> value -= resistedTypes.getOrDefault(ItemStat.AIRRESIST, 0);
                    case LIGHT -> value -= resistedTypes.getOrDefault(ItemStat.LIGHTRESIST, 0);
                    case DARK -> value -= resistedTypes.getOrDefault(ItemStat.DARKRESIST, 0);
                }

                value = Math.max(0, value); // Ensure no negative damage
                entry.setValue(value);
            }
        }

        applyDamage(target, damager, damageSplits);
    }

    private static void applyDamage(LivingEntity target, LivingEntity damager, Map<DamageType, Double> damageSplits) {
        Profile damagerProfile = new ProfileManager(DamagePlugin.getNmlPlayerStats()).getPlayerProfile(damager.getUniqueId());
        Stats damagerStats = damagerProfile.getStats();
        double totalDamage = 0;
        boolean critHit = false;

        // critical hit case
        if (damagerStats != null) {
            int critChance = damagerStats.getCritChance();
            int random = (int) (Math.random() * 100) + 1;

            if (random <= critChance) {
                critHit = true;
            }
        }

        for (Map.Entry<DamageType, Double> entry : damageSplits.entrySet()) {
            if (critHit) {
                entry.setValue(entry.getValue() * ((double) damagerStats.getCritDamage()) / 100);
            }

            damageInstanceMap.put(target.getUniqueId(), new DamageInstance(entry.getKey(), entry.getValue()));
            totalDamage += entry.getValue();

            if (damager instanceof Player player && entry.getValue() > 0) {
                if (critHit) {
                    player.sendMessage(DamageType.getDamageColor(entry.getKey()) + "You did " + entry.getValue() + " " + DamageType.getDamageString(entry.getKey()) + " damage! (CRIT)");
                } else {
                    player.sendMessage(DamageType.getDamageColor(entry.getKey()) + "You did " + entry.getValue() + " " + DamageType.getDamageString(entry.getKey()) + " damage!");
                }

            }
        }

        // in damagelistener
        target.setMetadata("punched", new FixedMetadataValue(damagePlugin, true));
        target.damage(totalDamage, damager);
    }
}