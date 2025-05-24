package io.github.Gabriel.damagePlugin.customDamage;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

public class DamageLoreUtil {

    public static void updateLoreWithElementalDamage(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        boolean found = false;
        DamageKey damageKey = new DamageKey(item);

        if (item.getType().isAir()) return;
        for (DamageType type : DamageType.values()) {
            if (damageKey.checkForDamageType(type)) {
                int value = damageKey.getDamageValue(type);
                lore.add(DamageType.getDamageColor(type) + "+ " + value + " " + DamageType.getDamageString(type) + " Damage");
                found = true;
            }
        }

        if (found) {
            lore.add("bleh");
            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }

    }
}
