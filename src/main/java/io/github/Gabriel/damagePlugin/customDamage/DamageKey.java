package io.github.Gabriel.damagePlugin.customDamage;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import io.github.Gabriel.damagePlugin.customDamage.lore.DamageLoreUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static io.github.Gabriel.damagePlugin.customDamage.DamageType.*;

public class DamageKey {
    private final DamagePlugin plugin;

    public DamageKey() {
        this.plugin = DamagePlugin.getInstance();
    }

    private NamespacedKey getKeyFor(DamageType type) {
        return new NamespacedKey(plugin, DamageType.getDamageString(type));
    }

    public void setDamage(ItemStack weapon, DamageType type, double damage) {
        NamespacedKey key = getKeyFor(type);
        ItemMeta meta = weapon.getItemMeta();
        PersistentDataContainer weaponContainer = meta.getPersistentDataContainer();

        weaponContainer.set(key, PersistentDataType.DOUBLE, damage);
        weapon.setItemMeta(meta);
        DamageLoreUtil.updateLoreWithElementalDamage(weapon, meta);
    }

    public void setDamageWithoutUpdatingLore(ItemStack weapon, DamageType type, double damage) {
        NamespacedKey key = getKeyFor(type);
        ItemMeta meta = weapon.getItemMeta();
        PersistentDataContainer weaponContainer = meta.getPersistentDataContainer();

        weaponContainer.set(key, PersistentDataType.DOUBLE, damage);
    }

    public double getDamageValue(ItemStack weapon, DamageType type) {
        NamespacedKey key = getKeyFor(type);
        ItemMeta meta = weapon.getItemMeta();
        PersistentDataContainer weaponContainer = meta.getPersistentDataContainer();

        if (checkForDamageType(weapon, type)) {
            return weaponContainer.get(key, PersistentDataType.DOUBLE);
        }
        return -1;
    }

    public DamageType getDamageType(ItemStack weapon) {
        ItemMeta meta = weapon.getItemMeta();
        PersistentDataContainer weaponContainer = meta.getPersistentDataContainer();
        String storedType = weaponContainer.get(getMetaKey(), PersistentDataType.STRING);
        if (storedType == null) return null;

        return switch (storedType) {
            case "Physical" -> DamageType.PHYSICAL;
            case "Fire"     -> FIRE;
            case "Cold"     -> COLD;
            case "Earth"    -> DamageType.EARTH;
            case "Lightning"-> DamageType.LIGHTNING;
            case "Air"      -> DamageType.AIR;
            case "Light"    -> DamageType.LIGHT;
            case "Dark"     -> DamageType.DARK;
            case "Pure"     -> PURE;
            default         -> null;
        };
    }

    public boolean checkForDamageType(ItemStack weapon, DamageType type) {
        NamespacedKey key = getKeyFor(type);
        ItemMeta meta = weapon.getItemMeta();
        PersistentDataContainer weaponContainer = meta.getPersistentDataContainer();
        return weaponContainer.has(key, PersistentDataType.DOUBLE);
    }

    private NamespacedKey getMetaKey() {
        return new NamespacedKey(plugin, "damage_type");
    }

    public double getTotalDamageOfWeapon(ItemStack weapon) {
        double totalDamage = 0;

        for (DamageType type : DamageType.values()) {
            if (checkForDamageType(weapon, type)) {
                totalDamage += getDamageValue(weapon, type);
            }
        }

        return totalDamage;
    }

    public HashMap<DamageType, Double> getAllDamageStats(ItemStack weapon) {
        HashMap<DamageType, Double> damageStats = new HashMap<>();

        for (DamageType type : DamageType.values()) {
            if (checkForDamageType(weapon, type)) {
                damageStats.put(type, getDamageValue(weapon, type));
            } 
        }
        return damageStats;
    }

    public boolean doesHaveDamageStats(ItemStack weapon) {
        return !getAllDamageStats(weapon).isEmpty();
    }

    public HashMap<DamageType, Double> multiplyAllDamageStats(ItemStack weapon, double multiplier) {
        if (doesHaveDamageStats(weapon)) {
            HashMap<DamageType, Double> damageStats = getAllDamageStats(weapon);
            HashMap<DamageType, Double> multipliedStats = new HashMap<>();

            for (Map.Entry<DamageType, Double> entry : damageStats.entrySet()) {
                multipliedStats.put(entry.getKey(), entry.getValue() * multiplier);
            }

            return multipliedStats;
        }

        return null;
    }
}