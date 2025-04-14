package io.github.Gabriel.damagePlugin.customDamage.damageLore;

import io.github.Gabriel.damagePlugin.customDamage.DamageKeys;
import io.github.Gabriel.damagePlugin.customDamage.DamageType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DamageLoreUtil {

    public static void updateLoreWithElementalDamage(ItemStack item, DamageKeys damageKeys) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (item.getType().isAir()) return;
        if (meta == null) return;

        boolean found = false;
        for (DamageType type : DamageType.values()) {
            if (damageKeys.checkForDamageKey(item, type)) {
                int value = damageKeys.getDamageKeyValue(item, type);
                lore.add(DamageType.getDamageColor(type) + "+ " + value + " " + DamageType.getDamageString(type) + " Damage");
                found = true;
            }
        }

        if (found) {
            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }
    }
}
