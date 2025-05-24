package io.github.Gabriel.damagePlugin.customDamage;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import io.github.Gabriel.damagePlugin.customDamage.damageLore.DamageLoreUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DamageKey {
    private final DamagePlugin plugin;
    private final ItemStack weapon;
    private final ItemMeta meta;
    private final PersistentDataContainer weaponContainer;

    public DamageKey(ItemStack weapon) {
        this.plugin = DamagePlugin.getInstance();
        this.weapon = weapon;
        this.meta = weapon.getItemMeta();
        assert meta != null;
        this.weaponContainer = meta.getPersistentDataContainer();
    }

    private NamespacedKey getKeyFor(DamageType type) {
        return new NamespacedKey(plugin, DamageType.getDamageString(type));
    }

    public void setDamage(DamageType type, double damage) {
        NamespacedKey key = getKeyFor(type);

        weaponContainer.set(key, PersistentDataType.DOUBLE, damage);
        weapon.setItemMeta(meta);
        DamageLoreUtil.updateLoreWithElementalDamage(weapon);
    }



    public double getDamageValue(DamageType type) {
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
        NamespacedKey key = getKeyFor(type);
        return weaponContainer.has(key, PersistentDataType.INTEGER);
    }

    private NamespacedKey getMetaKey() {
        return new NamespacedKey(plugin, "damage_type");
    }
}