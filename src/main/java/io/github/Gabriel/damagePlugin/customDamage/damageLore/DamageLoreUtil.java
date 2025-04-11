package io.github.Gabriel.damagePlugin.customDamage.damageLore;

import io.github.Gabriel.damagePlugin.customDamage.DamageKeys;
import io.github.Gabriel.damagePlugin.customDamage.DamageType;
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
        if (item.getType().isAir()) return;
        if (meta == null) return;

        String element = "";
        boolean found = false;
        ChatColor color = null;
        for (DamageType type : DamageType.values()) {
            switch (type.name()) {
                case "PHYSICAL" -> {
                    element = "Physical";
                    color = ChatColor.DARK_RED;
                }
                case "FIRE" -> {
                    element = "Fire";
                    color = ChatColor.RED;
                }
                case "COLD" -> {
                    element = "Cold";
                    color = ChatColor.AQUA;
                }
                case "EARTH" -> {
                    element = "Earth";
                    color = ChatColor.DARK_GREEN;
                }
                case "LIGHTNING" -> {
                    element = "Lightning";
                    color = ChatColor.YELLOW;
                }
                case "AIR" -> {
                    element = "Air";
                    color = ChatColor.GRAY;
                }
                case "LIGHT" -> {
                    element = "Light";
                    color = ChatColor.WHITE;
                }
                case "DARK" -> {
                    element = "Dark";
                    color = ChatColor.DARK_PURPLE;
                }
                case "PURE" -> {
                    element = "Pure";
                    color = ChatColor.WHITE;
                }
            }
            if (damageKeys.checkForDamageKey(item, type)) {
                int value = damageKeys.getDamageKeyValue(item, type);
                lore.add(color + "+ " + value + " " + element + " Damage");
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
