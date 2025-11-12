package io.github.NoOne.damagePlugin.customDamage;

import io.github.NoOne.damagePlugin.DamagePlugin;
import io.github.NoOne.nMLItems.ItemStat;
import io.github.NoOne.nMLMobs.mobstats.MobStats;
import io.github.NoOne.nMLMobs.mobstats.MobStatsYMLManager;
import io.github.NoOne.nMLPlayerStats.profileSystem.Profile;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static io.github.NoOne.nMLItems.ItemStat.*;

public class CustomDamager {
    private DamagePlugin damagePlugin;
    private ProfileManager profileManager;
    private MobStatsYMLManager mobStatsYMLManager;

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
        Map<DamageType, Double> effectiveDamageSplits = new EnumMap<>(damageSplits); // shallow copy for enum keys
        Stats damagerStats = profileManager.getPlayerProfile(damager.getUniqueId()).getStats();
        double totalDamage = 0;
        boolean critHit = false;

        // critical hit case
        if (damagerStats != null) critHit = ThreadLocalRandom.current().nextInt(1, 100) <= damagerStats.getCritChance();
        if (critHit) {
            double critMultiplier = damagerStats.getCritDamage() / 100.0;

            effectiveDamageSplits.replaceAll((k, v) -> v * critMultiplier);
        }

        for (Map.Entry<DamageType, Double> entry : damageSplits.entrySet()) {
           totalDamage += entry.getValue();
        }

        // in damage listener
        target.setMetadata("punched", new FixedMetadataValue(damagePlugin, true));
        target.damage(totalDamage, damager);
        DamageHologramGenerator.createDamageHologram(damagePlugin, damager, target, effectiveDamageSplits, critHit);
    }

    private void applyDamageFromMob(LivingEntity target, LivingEntity damager, MobStats mobStats, Map<DamageType, Double> damageSplits) {
        Map<DamageType, Double> effectiveDamageSplits = new EnumMap<>(damageSplits); // shallow copy for enum keys
        double totalDamage = 0;
        boolean critHit = false;

        // critical hit case
        if (mobStats != null) critHit = ThreadLocalRandom.current().nextInt(1, 100) <= mobStats.getCritChance();
        if (critHit) {
            double critMultiplier = mobStats.getCritDamage() / 100.0;

            effectiveDamageSplits.replaceAll((k, v) -> v * critMultiplier);
        }

        // in damagelistener
        target.setMetadata("punched", new FixedMetadataValue(damagePlugin, true));
        target.damage(totalDamage, damager);
    }
}