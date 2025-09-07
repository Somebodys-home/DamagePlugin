package io.github.Gabriel.damagePlugin.customDamage;

import io.github.NoOne.nMLItems.ItemStat;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;

import java.util.HashMap;
import java.util.Map;
import static io.github.Gabriel.damagePlugin.customDamage.DamageType.*;
import static io.github.NoOne.nMLItems.ItemStat.*;

public class DamageConverter {

    public DamageConverter() {}

    public static DamageType convertStat2DamageType(ItemStat stat) {
        switch (stat) {
            case PHYSICALDAMAGE -> {
                return PHYSICAL;
            }
            case FIREDAMAGE -> {
                return FIRE;
            }
            case COLDDAMAGE -> {
                return COLD;
            }
            case EARTHDAMAGE -> {
                return EARTH;
            }
            case LIGHTNINGDAMAGE -> {
                return LIGHTNING;
            }
            case AIRDAMAGE -> {
                return AIR;
            }
            case LIGHTDAMAGE -> {
                return LIGHT;
            }
            case DARKDAMAGE -> {
                return DARK;
            }
            case PUREDAMAGE -> {
                return PURE;
            }
        }

        return null;
    }

    public static DamageType convertPlayerStat2DamageType(String stat) {
        switch (stat) {
            case "physicaldamage" -> {
                return PHYSICAL;
            }
            case "firedamage" -> {
                return FIRE;
            }
            case "colddamage" -> {
                return COLD;
            }
            case "earthdamage" -> {
                return EARTH;
            }
            case "lightningdamage" -> {
                return LIGHTNING;
            }
            case "airdamage" -> {
                return AIR;
            }
            case "lightdamage" -> {
                return LIGHT;
            }
            case "darkdamage" -> {
                return DARK;
            }
            case "puredamage" -> {
                return PURE;
            }
        }

        return null;
    }

    public static ItemStat convertDamageType2Stat(DamageType damageType) {
        switch (damageType) {
            case PHYSICAL -> {
                return PHYSICALDAMAGE;
            }
            case FIRE -> {
                return FIREDAMAGE;
            }
            case COLD -> {
                return COLDDAMAGE;
            }
            case EARTH -> {
                return EARTHDAMAGE;
            }
            case LIGHTNING -> {
                return LIGHTNINGDAMAGE;
            }
            case AIR -> {
                return AIRDAMAGE;
            }
            case LIGHT -> {
                return LIGHTDAMAGE;
            }
            case DARK -> {
                return DARKDAMAGE;
            }
            case PURE -> {
                return PUREDAMAGE;
            }
        }

        return null;
    }

    public static HashMap<DamageType, Double> convertStatMap2DamageTypes(HashMap<ItemStat, Double> statMap) {
        HashMap<DamageType, Double> damageMap = new HashMap<>();

        for (Map.Entry<ItemStat, Double> entry : statMap.entrySet()) {
            if (entry.getKey() == PHYSICALDAMAGE || entry.getKey() == FIREDAMAGE || entry.getKey() == COLDDAMAGE || entry.getKey() == EARTHDAMAGE ||
                entry.getKey() == LIGHTNINGDAMAGE || entry.getKey() == AIRDAMAGE || entry.getKey() == LIGHTDAMAGE || entry.getKey() == DARKDAMAGE ||
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

        for (Map.Entry<String, Integer> statEntry : stats.getAllDamageStats().entrySet()) {
            damageMap.put(convertPlayerStat2DamageType(statEntry.getKey()), statEntry.getValue().doubleValue());
        }

        return damageMap;
    }

    public static HashMap<DamageType, Double> multiplyDamageMap(HashMap<DamageType, Double> damageMap, double multiplier) {
        for (Map.Entry<DamageType, Double> damageEntry : damageMap.entrySet()) {
            damageEntry.setValue(damageEntry.getValue() * multiplier);
        }

        return damageMap;
    }
}