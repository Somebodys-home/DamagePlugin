package io.github.NoOne.damagePlugin.customDamage;

import io.github.NoOne.damagePlugin.DamagePlugin;
import io.github.NoOne.nMLItems.ItemStat;
import io.github.NoOne.nMLMobs.mobstats.MobStats;
import io.github.NoOne.nMLMobs.mobstats.MobStatsYMLManager;
import io.github.NoOne.nMLPlayerStats.profileSystem.Profile;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.github.NoOne.nMLItems.ItemStat.*;

public class CustomDamager {
    private DamagePlugin damagePlugin;
    private ProfileManager profileManager;
    private MobStatsYMLManager mobStatsYMLManager;
    public record DamageInstance(DamageType type, double damage) {}
    private final Map<UUID, DamageInstance> damageInstanceMap = new HashMap<>();

    public CustomDamager(DamagePlugin damagePlugin) {
        this.damagePlugin = damagePlugin;
        profileManager = damagePlugin.getProfileManager();
        mobStatsYMLManager = damagePlugin.getMobStatsYMLManager();
    }

    public void doDamage(LivingEntity target, LivingEntity damager, Map<DamageType, Double> damageSplits, boolean isMobDamager) {
        Profile targetProfile = profileManager.getPlayerProfile(target.getUniqueId());

        if (targetProfile == null) { // for things without a player profile, like mobs
            applyDamage(target, damager, damageSplits);
            return;
        }

        Stats targetStats = targetProfile.getStats();
        HashMap<ItemStat, Integer> resistedTypes = new HashMap<>();

        // elemental resistances
        if (targetStats.getDefense() != 0) {
            resistedTypes.put(DEFENSE, targetStats.getDefense());
        }
        if (targetStats.getPhysicalResist() != 0) {
            resistedTypes.put(PHYSICALRESIST, targetStats.getPhysicalResist());
        }
        if (targetStats.getFireResist() != 0) {
            resistedTypes.put(FIRERESIST, targetStats.getFireResist());
        }
        if (targetStats.getColdResist() != 0) {
            resistedTypes.put(COLDRESIST, targetStats.getColdResist());
        }
        if (targetStats.getEarthResist() != 0) {
            resistedTypes.put(EARTHRESIST, targetStats.getEarthResist());
        }
        if (targetStats.getLightningResist() != 0) {
            resistedTypes.put(LIGHTNINGRESIST, targetStats.getLightningResist());
        }
        if (targetStats.getAirResist() != 0) {
            resistedTypes.put(AIRRESIST, targetStats.getAirResist());
        }
        if (targetStats.getRadiantResist() != 0) {
            resistedTypes.put(RADIANTRESIST, targetStats.getRadiantResist());
        }
        if (targetStats.getNecroticResist() != 0) {
            resistedTypes.put(NECROTICRESIST, targetStats.getNecroticResist());
        }

        if (!resistedTypes.isEmpty()) { // has any resistances
            for (Map.Entry<DamageType, Double> entry : damageSplits.entrySet()) {
                double value = entry.getValue();

                if (entry.getKey() != DamageType.PURE) {
                    value -= resistedTypes.getOrDefault(DEFENSE, 0);
                }

                switch (entry.getKey()) {
                    case PHYSICAL -> value -= resistedTypes.getOrDefault(PHYSICALRESIST, 0);
                    case FIRE -> value -= resistedTypes.getOrDefault(FIRERESIST, 0);
                    case COLD -> value -= resistedTypes.getOrDefault(COLDRESIST, 0);
                    case EARTH -> value -= resistedTypes.getOrDefault(EARTHRESIST, 0);
                    case LIGHTNING -> value -= resistedTypes.getOrDefault(LIGHTNINGRESIST, 0);
                    case AIR -> value -= resistedTypes.getOrDefault(AIRRESIST, 0);
                    case RADIANT -> value -= resistedTypes.getOrDefault(RADIANTRESIST, 0);
                    case NECROTIC -> value -= resistedTypes.getOrDefault(NECROTICRESIST, 0);
                }

                value = Math.max(0, value); // Ensure no negative damage
                entry.setValue(value);
            }
        }

        // if the damager is a mob
        if (isMobDamager) {
            MobStats mobStats = mobStatsYMLManager.getMobStatsFromYml(damager.getName());
            applyDamageFromMob(target, damager, mobStats, damageSplits);
        } else {
            applyDamage(target, damager, damageSplits);
        }
    }

    private void applyDamage(LivingEntity target, LivingEntity damager, Map<DamageType, Double> damageSplits) {
        Stats damagerStats = profileManager.getPlayerProfile(damager.getUniqueId()).getStats();
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
            double value = entry.getValue();

            if (critHit) value *= (damagerStats.getCritDamage() / 100.0);

            damageInstanceMap.put(target.getUniqueId(), new DamageInstance(entry.getKey(), value));
            totalDamage += value;
        }

        // in damage listener
        target.setMetadata("punched", new FixedMetadataValue(damagePlugin, true));
        target.damage(totalDamage, damager);
        DamageHologramGenerator.createDamageHologram(damagePlugin, target, damageSplits);
    }

    private void applyDamageFromMob(LivingEntity target, LivingEntity damager, MobStats mobStats, Map<DamageType, Double> damageSplits) {
        double totalDamage = 0;
        boolean critHit = false;

        // critical hit case
        if (mobStats != null) {
            int critChance = mobStats.getCritChance();
            int random = (int) (Math.random() * 100) + 1;

            if (random <= critChance) {
                critHit = true;
            }
        }

        for (Map.Entry<DamageType, Double> entry : damageSplits.entrySet()) {
            double value = entry.getValue();

            if (critHit) {
                value *= (mobStats.getCritDamage() / 100.0);
            }

            damageInstanceMap.put(target.getUniqueId(), new DamageInstance(entry.getKey(), value));
            totalDamage += value;
        }

        // in damagelistener
        target.setMetadata("punched", new FixedMetadataValue(damagePlugin, true));
        target.damage(totalDamage, damager);
    }
}