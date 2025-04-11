package io.github.Gabriel.damagePlugin.customDamage;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DamageLoreUtil {

    public static void updateLoreWithElementalDamage(ItemStack item, DamageKeys damageKeys) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (item == null || item.getType().isAir()) return;
        if (meta == null) return;

        boolean found = false;
        String element = "";
        ChatColor color = null;
        for (DamageType type : DamageType.values()) {
            switch (type.name()) {
                case "PHYSICAL" -> {
                    element = "Physical";
                    color = 'e';
                }
                case "PHYSICAL" -> {

                }
                case "PHYSICAL" -> {

                }
                case "PHYSICAL" -> {

                }
                case "PHYSICAL" -> {

                }
                case "PHYSICAL" -> {

                }
                case "PHYSICAL" -> {

                }
                case "PHYSICAL" -> {

                }
                case "PHYSICAL" -> {

                }
            }
            if (damageKeys.checkForDamageKey(item, type)) {
                double value = damageKeys.getDamageKeyValue(item, type);
                lore.add(ChatColor.translateAlternateColorCodes('&', "&7&e"))
                lore.add("§7• §e" + element + ": §f" + value);
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
