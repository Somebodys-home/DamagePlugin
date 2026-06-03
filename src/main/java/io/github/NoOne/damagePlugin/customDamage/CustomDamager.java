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

    public void doDamage(LivingEntity target, LivingEntity attacker, Map<DamageType, Double> damageSplits, boolean isMobDamager, int noDamageTicks) {
        Profile targetProfile = profileManager.getPlayerProfile(target.getUniqueId());
        Profile attackerProfile = profileManager.getPlayerProfile(attacker.getUniqueId());

        // sorting the damage map from highest to lowest
        damageSplits = damageSplits.entrySet().stream()
                .sorted(Map.Entry.<DamageType, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                )
        );

        if (attackerProfile != null) { // if attacker has stats, apply raw elemental damage to damage map
            Stats attackerStats = attackerProfile.getStats();
            int elementalDamage = attackerStats.getElementalDamage();
            Map.Entry<DamageType, Double> highestDamage = damageSplits.entrySet().iterator().next();

            // elemental damage doesn't apply to physical or pure damage
            if (highestDamage.getKey() != DamageType.PHYSICAL && highestDamage.getKey() != DamageType.PURE) {
                highestDamage.setValue(highestDamage.getValue() + elementalDamage);
            }

            damageSplits.put(highestDamage.getKey(), highestDamage.getValue());
        }

        if (targetProfile == null) { // if target doesn't have stats, just apply damage now
            applyDamage(target, attacker, damageSplits, noDamageTicks);
            return;
        }

        Stats targetStats = targetProfile.getStats();
        HashMap<ItemStat, Integer> resistedTypes = new HashMap<>();

        // elemental resistances
        if (targetStats.getDefense() != 0) resistedTypes.put(DEFENSE, targetStats.getDefense());
        if (targetStats.getPhysicalResist() != 0) resistedTypes.put(PHYSICALRESIST, targetStats.getPhysicalResist());
        if (targetStats.getFireResist() != 0) resistedTypes.put(FIRERESIST, targetStats.getFireResist());
        if (targetStats.getColdResist() != 0) resistedTypes.put(COLDRESIST, targetStats.getColdResist());
        if (targetStats.getEarthResist() != 0) resistedTypes.put(EARTHRESIST, targetStats.getEarthResist());
        if (targetStats.getLightningResist() != 0) resistedTypes.put(LIGHTNINGRESIST, targetStats.getLightningResist());
        if (targetStats.getAirResist() != 0) resistedTypes.put(AIRRESIST, targetStats.getAirResist());
        if (targetStats.getRadiantResist() != 0) resistedTypes.put(RADIANTRESIST, targetStats.getRadiantResist());
        if (targetStats.getNecroticResist() != 0) resistedTypes.put(NECROTICRESIST, targetStats.getNecroticResist());

        if (!resistedTypes.isEmpty()) { // if target has any resistances
            for (Map.Entry<DamageType, Double> entry : damageSplits.entrySet()) { // for every damage type
                double value = entry.getValue();
                double damageReductionPercent = 0;

                if (entry.getKey() != DamageType.PURE) { // apply defense reduction on everything except pure damage
                    damageReductionPercent += resistedTypes.getOrDefault(DEFENSE, 0) * .005; // .5% : 1
                }

                switch (entry.getKey()) { // then go into elemental resistances (1% : 1)
                    case PHYSICAL -> damageReductionPercent += resistedTypes.getOrDefault(PHYSICALRESIST, 0) * .01;
                    case FIRE -> damageReductionPercent += resistedTypes.getOrDefault(FIRERESIST, 0) * .01;
                    case COLD -> damageReductionPercent += resistedTypes.getOrDefault(COLDRESIST, 0) * .01;
                    case EARTH -> damageReductionPercent += resistedTypes.getOrDefault(EARTHRESIST, 0) * .01;
                    case LIGHTNING -> damageReductionPercent += resistedTypes.getOrDefault(LIGHTNINGRESIST, 0) * .01;
                    case AIR -> damageReductionPercent += resistedTypes.getOrDefault(AIRRESIST, 0) * .01;
                    case RADIANT -> damageReductionPercent += resistedTypes.getOrDefault(RADIANTRESIST, 0) * .01;
                    case NECROTIC -> damageReductionPercent += resistedTypes.getOrDefault(NECROTICRESIST, 0) * .01;
                }

                value = Math.max(0, value * damageReductionPercent);
                entry.setValue(value);
            }
        }

        // if the damager is a mob
        if (isMobDamager) {
            MobStats mobStats = mobStatsYMLManager.getMobStatsFromYml(attacker.getName());
            applyDamageFromMob(target, attacker, mobStats, damageSplits);
        } else {
            applyDamage(target, attacker, damageSplits, noDamageTicks);
        }
    }

    private void applyDamage(LivingEntity target, LivingEntity attacker, Map<DamageType, Double> damageSplits, int noDamageTicks) {
        Map<DamageType, Double> effectiveDamageSplits = new EnumMap<>(damageSplits); // shallow copy for enum keys
        Stats damagerStats = profileManager.getPlayerProfile(attacker.getUniqueId()).getStats();
        double totalDamage = 0;
        boolean critHit = false;

        // crit chance
        if (damagerStats != null) critHit = ThreadLocalRandom.current().nextDouble(1, 100) <= damagerStats.getCritChance();
        if (critHit) {
            double critMultiplier = damagerStats.getCritDamage() / 100.0;

            effectiveDamageSplits.replaceAll((k, v) -> v * critMultiplier);
            attacker.getWorld().playSound(attacker, Sound.ENTITY_PLAYER_ATTACK_CRIT, 2f, 1f);
            attacker.getWorld().playSound(attacker, Sound.ENTITY_PLAYER_ATTACK_CRIT, 2f, 1f);
        }

        for (Map.Entry<DamageType, Double> entry : effectiveDamageSplits.entrySet()) {
           totalDamage += entry.getValue();
        }

        target.damage(totalDamage, attacker);
        target.setNoDamageTicks(noDamageTicks);
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