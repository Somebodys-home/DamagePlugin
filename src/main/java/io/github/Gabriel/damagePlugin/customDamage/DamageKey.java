package io.github.Gabriel.damagePlugin.customDamage;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class DamageKey {
    private static NamespacedKey damageKey = new NamespacedKey(DamagePlugin.getInstance(), "no_damage_set");
    private final ItemStack weapon;
    private PersistentDataContainer weaponContainer;

    public DamageKey(ItemStack weapon) {
        this.weapon = weapon;
        weaponContainer = Objects.requireNonNull(weapon.getItemMeta()).getPersistentDataContainer();
    }

    public void setDamage(DamageType type, int damage) {
        damageKey = new NamespacedKey(DamagePlugin.getInstance(), DamageType.getDamageString(type));
        weaponContainer.set(damageKey, PersistentDataType.INTEGER, damage);
    }

    public int getDamageValue(DamageType type) {
        if (checkForDamageType(type)) {
            return weaponContainer.get(damageKey, PersistentDataType.INTEGER).intValue();
        }
        return -1;
    }

    public DamageType getDamageType() {
        switch (weaponContainer.get(damageKey, PersistentDataType.STRING)) {
            case "Physical" -> {return DamageType.PHYSICAL;}
            case "Fire" -> {return DamageType.FIRE;}
            case "Cold" -> {return DamageType.COLD;}
            case "Earth" -> {return DamageType.EARTH;}
            case "Lightning" -> {return DamageType.LIGHTNING;}
            case "Air" -> {return DamageType.AIR;}
            case "Light" -> {return DamageType.LIGHT;}
            case "Dark" -> {return DamageType.DARK;}
            case "Pure" -> {return DamageType.PURE;}
            case null, default -> {return null;}
        }
    }
    public boolean checkForDamageType(DamageType type) {
        String typeString = DamageType.getDamageString(type);

        if (weaponContainer.has(damageKey)) {
            return weaponContainer.get(damageKey, PersistentDataType.STRING).equals(typeString);
        }
        return false;
    }
}
