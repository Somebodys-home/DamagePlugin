package io.github.Gabriel.damagePlugin.noDamage;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NoDamageManager {
    private DamagePlugin plugin;

    public NoDamageManager(DamagePlugin plugin) {
        this.plugin = plugin;
    }

    public void removeAttributes(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
    }
}