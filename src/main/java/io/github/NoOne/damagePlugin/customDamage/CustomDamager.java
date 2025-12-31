package io.github.NoOne.damagePlugin.customDamage;

import io.github.NoOne.damagePlugin.DamagePlugin;
import io.github.NoOne.nMLItems.ItemStat;
import io.github.NoOne.nMLMobs.mobstats.MobStats;
import io.github.NoOne.nMLMobs.mobstats.MobStatsYMLManager;
import io.github.NoOne.nMLPlayerStats.profileSystem.Profile;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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

    public void doDamage(LivingEntity target, LivingEntity attacker, Map<DamageType, Double> damageSplits, boolean isMobDamager) {
        Profile targetProfile = profileManager.getPlayerProfile(target.getUniqueId());

        if (targetProfile == null) { // for things without a player profile, like mobs
            applyDamage(target, attacker, damageSplits);
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
            MobStats mobStats = mobStatsYMLManager.getMobStatsFromYml(attacker.getName());
            applyDamageFromMob(target, attacker, mobStats, damageSplits);
        } else {
            applyDamage(target, attacker, damageSplits);
        }
    }

    private void applyDamage(LivingEntity target, LivingEntity attacker, Map<DamageType, Double> damageSplits) {
        Map<DamageType, Double> effectiveDamageSplits = new EnumMap<>(damageSplits); // shallow copy for enum keys
        Stats damagerStats = profileManager.getPlayerProfile(attacker.getUniqueId()).getStats();
        double totalDamage = 0;
        boolean critHit = false;

        // sorting damage by highest to lowest
        effectiveDamageSplits = effectiveDamageSplits.entrySet().stream()
                .sorted(Map.Entry.<DamageType, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        // crit chance & elemental damage
        if (damagerStats != null) {
            int elementalDamage = damagerStats.getElementalDamage();
            Map.Entry<DamageType, Double> highestDamage = effectiveDamageSplits.entrySet().iterator().next();

            critHit = ThreadLocalRandom.current().nextDouble(1, 100) <= damagerStats.getCritChance();

            // elemental damage doesn't apply to physical or pure damage
            if (highestDamage.getKey() != DamageType.PHYSICAL && highestDamage.getKey() != DamageType.PURE) {
                highestDamage.setValue(highestDamage.getValue() + elementalDamage);
            }

            effectiveDamageSplits.put(highestDamage.getKey(), highestDamage.getValue());
        }

        if (critHit) {
            double critMultiplier = damagerStats.getCritDamage() / 100.0;

            effectiveDamageSplits.replaceAll((k, v) -> v * critMultiplier);
        }

        for (Map.Entry<DamageType, Double> entry : damageSplits.entrySet()) {
           totalDamage += entry.getValue();
        }

        target.damage(totalDamage, attacker);
        DamageHologramGenerator.createDamageHologram(damagePlugin, attacker, target, effectiveDamageSplits, critHit);
    }

    private void applyDamageFromMob(LivingEntity target, LivingEntity attacker, MobStats mobStats, Map<DamageType, Double> damageSplits) {
        Map<DamageType, Double> effectiveDamageSplits = new EnumMap<>(damageSplits); // shallow copy for enum keys
        double totalDamage = 0;
        boolean critHit = false;

        // critical hit case
        if (mobStats != null) critHit = ThreadLocalRandom.current().nextDouble(1, 100) <= mobStats.getCritChance();
        if (critHit) {
            double critMultiplier = mobStats.getCritDamage() / 100.0;

            effectiveDamageSplits.replaceAll((k, v) -> v * critMultiplier);
        }

        for (double damage : effectiveDamageSplits.values()) {
            totalDamage += damage;
        }

        // knockback
        Vector knockbackDirection = target.getLocation().toVector().subtract(attacker.getLocation().toVector()).normalize();
        Vector knockback = knockbackDirection.multiply(.4).setY(.35);

        target.damage(totalDamage);
        target.setVelocity(knockback);
    }
}