package io.github.Gabriel.damagePlugin.customDamage;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

public class DamageLoreUtil {
    public static void updateLoreWithElementalDamage(ItemStack item) {
        DamageKey damageKey = new DamageKey();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        boolean found = false;

        if (item.getType().isAir()) return;
        if (meta == null) return;

        for (DamageType type : DamageType.values()) {
            if (damageKey.checkForDamageType(item, type) && damageKey.getDamageValue(item, type) > 0) {
                int value = (int) damageKey.getDamageValue(item, type);
                lore.add(DamageType.getDamageColor(type) + "+ " + value + " " + DamageType.getDamageString(type) + " Damage");
                found = true;
            }
        }

        if (found) {
            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static void updateLoreWithElementalDamage(ItemStack item, ItemMeta meta) {
        DamageKey damageKey = new DamageKey();
        List<String> originalLore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        List<String> damageLore = new ArrayList<>();

        if (item.getType().isAir() || meta == null) return;

        for (DamageType type : DamageType.values()) {
            if (damageKey.checkForDamageType(item, type) && damageKey.getDamageValue(item, type) > 0) {
                int value = (int) damageKey.getDamageValue(item, type);
                damageLore.add(DamageType.getDamageColor(type) + "+ " + value + " " + DamageType.getDamageString(type) + " Damage");
            }
        }

        // Combine existing lore with damage lore
        originalLore.addAll(damageLore);

        meta.setLore(originalLore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
    }

}
