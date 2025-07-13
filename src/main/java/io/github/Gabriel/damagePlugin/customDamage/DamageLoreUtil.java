package io.github.Gabriel.damagePlugin.customDamage;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DamageLoreUtil {
    public static void updateLoreWithElementalDamage(ItemStack weapon, ItemMeta meta) {
        DamageKey damageKey = new DamageKey();
        List<String> originalLore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        List<String> damageLore = new ArrayList<>();
        HashMap<DamageType, Double> damageStats = damageKey.getAllDamageStats(weapon);

//        for (DamageType type : DamageType.values()) {
//            if (damageKey.checkForDamageType(weapon, type)) {
//                int value = (int) damageKey.getDamageValue(weapon, type);
//                damageLore.add(DamageType.getDamageColor(type) + "+ " + value + " " + DamageType.getDamageString(type) + " Damage");
//            }
//        }
        for (Map.Entry<DamageType, Double> damageEntry : damageStats.entrySet()) {
            double value = damageEntry.getValue();
            int valueInt = (int) value;
            damageLore.add(DamageType.getDamageColor(damageEntry.getKey()) + "+ " + valueInt + " " + DamageType.getDamageString(damageEntry.getKey()) + " Damage");
        }

        // Combine existing lore with damage lore
        originalLore.addAll(damageLore);
        meta.setLore(originalLore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        weapon.setItemMeta(meta);
    }

}
