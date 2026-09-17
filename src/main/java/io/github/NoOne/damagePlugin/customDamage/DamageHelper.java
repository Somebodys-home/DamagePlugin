package io.github.NoOne.damagePlugin.customDamage;

import io.github.NoOne.nMLItems.enums.ItemStat;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.github.NoOne.damagePlugin.customDamage.DamageType.*;
import static io.github.NoOne.nMLItems.enums.ItemStat.*;

public class DamageHelper {
    public static DamageType convertStat2DamageType(ItemStat stat) {
        return switch (stat) {
            case PHYSICALDAMAGE -> PHYSICAL;
            case FIREDAMAGE -> FIRE;
            case COLDDAMAGE -> COLD;
            case EARTHDAMAGE -> EARTH;
            case LIGHTNINGDAMAGE -> LIGHTNING;
            case AIRDAMAGE -> AIR;
            case RADIANTDAMAGE -> RADIANT;
            case NECROTICDAMAGE -> NECROTIC;
            case PUREDAMAGE -> PURE;
            default -> null;
        };
    }

    public static DamageType convertString2DamageType(String stat) {
        return switch (stat) {
            case "physicaldamage" -> PHYSICAL;
            case "firedamage" -> FIRE;
            case "colddamage" -> COLD;
            case "earthdamage" -> EARTH;
            case "lightningdamage" -> LIGHTNING;
            case "airdamage" -> AIR;
            case "radiantdamage" -> RADIANT;
            case "necroticdamage" -> NECROTIC;
            case "puredamage" -> PURE;
            default -> null;
        };
    }

    public static ItemStat convertDamageType2Stat(DamageType damageType) {
        return switch (damageType) {
            case PHYSICAL -> PHYSICALDAMAGE;
            case FIRE -> FIREDAMAGE;
            case COLD -> COLDDAMAGE;
            case EARTH -> EARTHDAMAGE;
            case LIGHTNING -> LIGHTNINGDAMAGE;
            case AIR -> AIRDAMAGE;
            case RADIANT -> RADIANTDAMAGE;
            case NECROTIC -> NECROTICDAMAGE;
            case PURE -> PUREDAMAGE;
        };
    }

    public static HashMap<DamageType, Double> convertStatMap2DamageTypes(HashMap<ItemStat, Double> statMap) {
        HashMap<DamageType, Double> damageMap = new HashMap<>();

        for (Map.Entry<ItemStat, Double> entry : statMap.entrySet()) {
            if (entry.getKey() == PHYSICALDAMAGE || entry.getKey() == FIREDAMAGE || entry.getKey() == COLDDAMAGE || entry.getKey() == EARTHDAMAGE ||
                entry.getKey() == LIGHTNINGDAMAGE || entry.getKey() == AIRDAMAGE || entry.getKey() == RADIANTDAMAGE || entry.getKey() == NECROTICDAMAGE ||
                entry.getKey() == PUREDAMAGE) {

                damageMap.put(convertStat2DamageType(entry.getKey()), entry.getValue());
            }
        }

        return damageMap;
    }

    public static HashMap<ItemStat, Double> convertDamageTypeMap2Stats(HashMap<DamageType, Double> statMap) {
        HashMap<ItemStat, Double> damageMap = new HashMap<>();

        for (Map.Entry<DamageType, Double> entry : statMap.entrySet()) {
            damageMap.put(convertDamageType2Stat(entry.getKey()), entry.getValue());
        }

        return damageMap;
    }

    public static HashMap<DamageType, Double> convertPlayerStats2Damage(Stats stats) {
        HashMap<DamageType, Double> damageMap = new HashMap<>();
        HashMap<DamageType, Double> highestElementalDamage = new HashMap<>();

        if (!stats.getHighestElementalDamage().isEmpty()) {
            Map.Entry<String, Integer> finalEntry = stats.getHighestElementalDamage().entrySet().iterator().next();
            highestElementalDamage = new HashMap<>(){{
                put(convertString2DamageType(finalEntry.getKey()), Double.valueOf(finalEntry.getValue()) + stats.getElementalDamage());
            }};
        }

        for (Map.Entry<String, Integer> statEntry : stats.getAllDamages().entrySet()) {
            damageMap.put(convertString2DamageType(statEntry.getKey()), statEntry.getValue().doubleValue());
        }

        damageMap.putAll(highestElementalDamage);

        return damageMap;
    }

    public static HashMap<DamageType, Double> convertPlayerStat2Damage(Stats stats, String stat) {
        HashMap<DamageType, Double> damage = new HashMap<>();

        for (Map.Entry<String, Integer> damageEntry : stats.getAllDamages().entrySet()) {
            if (Objects.equals(damageEntry.getKey(), stat)) {
                damage.put(convertString2DamageType(damageEntry.getKey()), damageEntry.getValue().doubleValue());
                return damage;
            }
        }

        return damage;
    }

    public static HashMap<DamageType, Double> multiplyDamageMap(HashMap<DamageType, Double> damageMap, double multiplier) {
        HashMap<DamageType, Double> copy =  new HashMap<>(damageMap);

        for (Map.Entry<DamageType, Double> damageEntry : copy.entrySet()) {
            damageEntry.setValue(damageEntry.getValue() * multiplier);
        }

        return copy;
    }

    public static HashMap<DamageType, Double> convertStringMap2DamageTypes(HashMap<String, Double> statMap) {
        HashMap<DamageType, Double> damageMap = new HashMap<>();

        for (Map.Entry<String, Double> entry : statMap.entrySet()) {
            damageMap.put(convertString2DamageType(entry.getKey()), entry.getValue());
        }

        return damageMap;
    }

    public static HashMap<DamageType, Double> convertStringIntMap2DamageTypes(HashMap<String, Integer> statMap) {
        HashMap<DamageType, Double> damageMap = new HashMap<>();

        for (Map.Entry<String, Integer> entry : statMap.entrySet()) {
            damageMap.put(convertString2DamageType(entry.getKey()), entry.getValue().doubleValue());
        }

        return damageMap;
    }

    public static boolean isMobDamageable(LivingEntity livingEntity) {
        return !livingEntity.hasMetadata("hologram") && !livingEntity.hasMetadata("garden_crop") &&
                !livingEntity.hasMetadata("invincible") && livingEntity.getNoDamageTicks() <= 0;
    }
}