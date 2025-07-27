package io.github.Gabriel.damagePlugin.customDamage;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DamageLore {
    public static void updateLoreWithElementalDamage(ItemStack weapon, ItemMeta meta) {
        DamageManager damageManager = new DamageManager();
        List<String> originalLore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        List<String> damageLore = new ArrayList<>();
        HashMap<DamageType, Double> damageStats = damageManager.getAllDamageStats(weapon);

        damageStats.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue())) // Descending sort
                .forEachOrdered(entry -> {
                    double value = entry.getValue();
                    int valueInt = (int) value;
                    damageLore.add(DamageType.getDamageColor(entry.getKey()) + "+ " + valueInt + " " + DamageType.getDamageString(entry.getKey()) + " Damage");
                });


        // Combine existing lore with damage lore
        originalLore.addAll(damageLore);
        meta.setLore(originalLore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        weapon.setItemMeta(meta);
    }

}
