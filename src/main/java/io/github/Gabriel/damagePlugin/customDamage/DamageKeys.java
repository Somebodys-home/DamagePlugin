package io.github.Gabriel.damagePlugin.customDamage;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DamageKeys {
    private DamagePlugin plugin;

    public DamageKeys(DamagePlugin plugin) {
        this.plugin = plugin;
    }

    public NamespacedKey damageKey(DamageType type) {
        return new NamespacedKey(plugin, DamageType.getDamageString(type).toLowerCase());
    }

    public void setDamageKey(ItemStack itemStack, DamageType type, int damage) {
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(damageKey(type), PersistentDataType.INTEGER, damage);
        }

        itemStack.setItemMeta(meta);
    }

    public int getDamageKeyValue(ItemStack itemStack, DamageType type) {
        ItemMeta meta = itemStack.getItemMeta();
            assert meta != null;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        return pdc.get(damageKey(type), PersistentDataType.INTEGER).intValue();
    }

    public boolean checkForDamageKey(ItemStack itemStack, DamageType type) {
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            return pdc.has(damageKey(type), PersistentDataType.INTEGER);
        }

        return false;
    }
}
