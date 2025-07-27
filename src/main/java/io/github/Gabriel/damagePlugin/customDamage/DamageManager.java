package io.github.Gabriel.damagePlugin.customDamage;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

import static io.github.Gabriel.damagePlugin.customDamage.DamageType.*;

public class DamageManager {
    private final DamagePlugin plugin;

    public DamageManager() {
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

    public boolean checkForDamageType(ItemStack weapon, DamageType type) {
        NamespacedKey key = getKeyFor(type);
        ItemMeta meta = weapon.getItemMeta();
        PersistentDataContainer weaponContainer = meta.getPersistentDataContainer();
        return weaponContainer.has(key, PersistentDataType.DOUBLE);
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