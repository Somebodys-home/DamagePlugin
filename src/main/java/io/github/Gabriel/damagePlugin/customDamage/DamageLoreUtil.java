package io.github.Gabriel.damagePlugin.customDamage;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

public class DamageLoreUtil {
    public static void updateLoreWithElementalDamage(ItemStack item, ItemMeta meta) {
        DamageKey damageKey = new DamageKey();
        List<String> originalLore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        List<String> damageLore = new ArrayList<>();

        for (DamageType type : DamageType.values()) {
            if (damageKey.checkForDamageType(item, type)) {
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
