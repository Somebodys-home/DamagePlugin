package io.github.Gabriel.damagePlugin.customDamage;

import io.github.NoOne.nMLItems.ItemStat;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.github.Gabriel.damagePlugin.customDamage.DamageType.*;
import static io.github.NoOne.nMLItems.ItemStat.*;

public class DamageConverter {

    public DamageConverter() {}

    public static DamageType convertStat2DamageType(ItemStat stat) {
        DamageType damageType = null;

        switch (stat) {
            case PHYSICALDAMAGE -> damageType = PHYSICAL;
            case FIREDAMAGE -> damageType = FIRE;
            case COLDDAMAGE -> damageType = COLD;
            case EARTHDAMAGE -> damageType = EARTH;
            case LIGHTNINGDAMAGE -> damageType = LIGHTNING;
            case AIRDAMAGE -> damageType = AIR;
            case RADIANTDAMAGE -> damageType = RADIANT;
            case NECROTICDAMAGE -> damageType = NECROTIC;
            case PUREDAMAGE -> damageType = PURE;
        }

        return damageType;
    }

    public static DamageType convertString2DamageType(String stat) {
        DamageType damageType = null;

        switch (stat) {
            case "physicaldamage" -> damageType = PHYSICAL;
            case "firedamage" -> damageType = FIRE;
            case "colddamage" -> damageType = COLD;
            case "earthdamage" -> damageType = EARTH;
            case "lightningdamage" -> damageType = LIGHTNING;
            case "airdamage" -> damageType = AIR;
            case "radiantdamage" -> damageType = RADIANT;
            case "necroticdamage" -> damageType = NECROTIC;
            case "puredamage" -> damageType = PURE;
        }

        return damageType;
    }

    public static ItemStat convertDamageType2Stat(DamageType damageType) {
        ItemStat itemStat = null;

        switch (damageType) {
            case PHYSICAL -> itemStat = PHYSICALDAMAGE;
            case FIRE -> itemStat = FIREDAMAGE;
            case COLD -> itemStat = COLDDAMAGE;
            case EARTH -> itemStat = EARTHDAMAGE;
            case LIGHTNING -> itemStat = LIGHTNINGDAMAGE;
            case AIR -> itemStat = AIRDAMAGE;
            case RADIANT -> itemStat = RADIANTDAMAGE;
            case NECROTIC -> itemStat = NECROTICDAMAGE;
            case PURE -> itemStat = PUREDAMAGE;
        }

        return itemStat;
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

        for (Map.Entry<String, Integer> statEntry : stats.getAllDamages().entrySet()) {
            damageMap.put(convertString2DamageType(statEntry.getKey()), statEntry.getValue().doubleValue());
        }

        return damageMap;
    }

    public static HashMap<DamageType, Double> convertPlayerStat2Damage(Stats stats, String stat) {
        HashMap<DamageType, Double> damage = new HashMap<>();
        HashMap<String, Integer> damageMap = stats.getAllDamages();

        for (Map.Entry<String, Integer> damageEntry : damageMap.entrySet()) {
            if (Objects.equals(damageEntry.getKey(), stat)) {
                damage.put(convertString2DamageType(damageEntry.getKey()), damageEntry.getValue().doubleValue());
                return damage;
            }
        }

        return damage;
    }

    public static HashMap<DamageType, Double> multiplyDamageMap(HashMap<DamageType, Double> damageMap, double multiplier) {
        for (Map.Entry<DamageType, Double> damageEntry : damageMap.entrySet()) {
            damageEntry.setValue(damageEntry.getValue() * multiplier);
        }

        return damageMap;
    }

    public static HashMap<DamageType, Double> convertStringMap2DamageTypes(HashMap<String, Double> statMap) {
        HashMap<DamageType, Double> damageMap = new HashMap<>();

        for (Map.Entry<String, Double> entry : statMap.entrySet()) {
            damageMap.put(convertString2DamageType(entry.getKey()), entry.getValue());
        }

        return damageMap;
    }
}