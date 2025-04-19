package io.github.Gabriel.damagePlugin.customDamage;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class DamageKey {
    private final ItemStack weapon;
    private final PersistentDataContainer weaponContainer;

    public DamageKey(ItemStack weapon) {
        this.weapon = weapon;
        this.weaponContainer = Objects.requireNonNull(weapon.getItemMeta()).getPersistentDataContainer();
    }

    private NamespacedKey getKeyFor(DamageType type) {
        return new NamespacedKey(DamagePlugin.getInstance(), DamageType.getDamageString(type));
    }

    public void setDamage(DamageType type, int damage) {
        NamespacedKey key = getKeyFor(type);
        weaponContainer.set(key, PersistentDataType.INTEGER, damage);
        // Store damage type as string too
        weaponContainer.set(getMetaKey(), PersistentDataType.STRING, DamageType.getDamageString(type));
    }

    public int getDamageValue(DamageType type) {
        NamespacedKey key = getKeyFor(type);
        if (checkForDamageType(type)) {
            return weaponContainer.get(key, PersistentDataType.INTEGER);
        }
        return -1;
    }

    public DamageType getDamageType() {
        String storedType = weaponContainer.get(getMetaKey(), PersistentDataType.STRING);
        if (storedType == null) return null;

        return switch (storedType) {
            case "Physical" -> DamageType.PHYSICAL;
            case "Fire"     -> DamageType.FIRE;
            case "Cold"     -> DamageType.COLD;
            case "Earth"    -> DamageType.EARTH;
            case "Lightning"-> DamageType.LIGHTNING;
            case "Air"      -> DamageType.AIR;
            case "Light"    -> DamageType.LIGHT;
            case "Dark"     -> DamageType.DARK;
            case "Pure"     -> DamageType.PURE;
            default         -> null;
        };
    }

    public boolean checkForDamageType(DamageType type) {
        String typeString = DamageType.getDamageString(type);
        String stored = weaponContainer.get(getMetaKey(), PersistentDataType.STRING);
        return typeString.equals(stored);
    }

    private NamespacedKey getMetaKey() {
        return new NamespacedKey(DamagePlugin.getInstance(), "damage_type");
    }
}
