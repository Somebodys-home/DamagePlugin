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
    private final ItemStack weapon;
    private final ItemMeta meta;
    private final PersistentDataContainer weaponContainer;

    public DamageKey(ItemStack weapon) {
        this.plugin = DamagePlugin.getInstance();
        if (plugin == null) {
            System.out.println("null plugin");
        }
        this.weapon = weapon;
        this.meta = weapon.getItemMeta();

        if (meta == null) {
            this.weaponContainer = null;
        } else {
            this.weaponContainer = meta.getPersistentDataContainer();
        }
    }

    private NamespacedKey getKeyFor(DamageType type) {
        return new NamespacedKey(plugin, DamageType.getDamageString(type));
    }

    public void setDamage(DamageType type, double damage) {
        NamespacedKey key = getKeyFor(type);

        weaponContainer.set(key, PersistentDataType.DOUBLE, damage);
        weapon.setItemMeta(meta);
        DamageLoreUtil.updateLoreWithElementalDamage(weapon, meta);
    }

    public void setRandomDamages(double damage, int instances) {
        List<DamageType> damageTypes = new ArrayList<>(List.of(PHYSICAL, FIRE, COLD, EARTH, LIGHTNING, AIR, LIGHT, DARK, PURE));

        if (instances == 1) {
            NamespacedKey key = getKeyFor(damageTypes.get(ThreadLocalRandom.current().nextInt(damageTypes.size())));
            weaponContainer.set(key, PersistentDataType.DOUBLE, damage);
        } else {
            for (int i = 0; i < instances - 1; i++) {
                int random = ThreadLocalRandom.current().nextInt(damageTypes.size());
                NamespacedKey key = getKeyFor(damageTypes.get(random));
                weaponContainer.set(key, PersistentDataType.DOUBLE, damage);
                damageTypes.remove(random);
            }
        }

        DamageLoreUtil.updateLoreWithElementalDamage(weapon, meta);
    }

    public void setRandomDamages(double damage, int instances, ItemMeta meta) {
        List<DamageType> damageTypes = new ArrayList<>(List.of(PHYSICAL, FIRE, COLD, EARTH, LIGHTNING, AIR, LIGHT, DARK, PURE));

        if (instances == 1) {
            NamespacedKey key = getKeyFor(damageTypes.get(ThreadLocalRandom.current().nextInt(damageTypes.size())));
            weaponContainer.set(key, PersistentDataType.DOUBLE, damage);
        } else {
            for (int i = 0; i < instances - 1; i++) {
                int random = ThreadLocalRandom.current().nextInt(damageTypes.size());
                NamespacedKey key = getKeyFor(damageTypes.get(random));
                weaponContainer.set(key, PersistentDataType.DOUBLE, damage);
                damageTypes.remove(random);
            }
        }

        DamageLoreUtil.updateLoreWithElementalDamage(weapon, meta);
    }

    public double getDamageValue(DamageType type) {
        NamespacedKey key = getKeyFor(type);
        if (checkForDamageType(type)) {
            return weaponContainer.get(key, PersistentDataType.DOUBLE);
        }
        return -1;
    }

    public DamageType getDamageType() {
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

    public boolean checkForDamageType(DamageType type) {
        NamespacedKey key = getKeyFor(type);
        return weaponContainer.has(key, PersistentDataType.DOUBLE);
    }

    private NamespacedKey getMetaKey() {
        return new NamespacedKey(plugin, "damage_type");
    }

    public double getTotalDamageOfWeapon() {
        double totalDamage = 0;

        for (DamageType type : DamageType.values()) {
            if (checkForDamageType(type)) {
                totalDamage += getDamageValue(type);
            }
        }

        return totalDamage;
    }

    public HashMap<DamageType, Double> getAllDamageStats() {
        HashMap<DamageType, Double> damageStats = new HashMap<>();

        for (DamageType type : DamageType.values()) {
            if (checkForDamageType(type)) {
                damageStats.put(type, getDamageValue(type));
            } 
        }
        return damageStats;
    }

    public boolean doesHaveDamageStats() {
        return !getAllDamageStats().isEmpty();
    }

    public HashMap<DamageType, Double> multiplyAllDamageStats(double multiplier) {
        if (doesHaveDamageStats()) {
            HashMap<DamageType, Double> damageStats = getAllDamageStats();
            HashMap<DamageType, Double> multipliedStats = new HashMap<>();

            for (Map.Entry<DamageType, Double> entry : damageStats.entrySet()) {
                multipliedStats.put(entry.getKey(), entry.getValue() * multiplier);
            }

            return multipliedStats;
        }

        return null;
    }
}