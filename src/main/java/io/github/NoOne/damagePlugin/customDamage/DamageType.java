package io.github.NoOne.damagePlugin.customDamage;

import org.bukkit.ChatColor;

public enum DamageType {
    PHYSICAL,
    FIRE,
    COLD,
    EARTH,
    LIGHTNING,
    AIR,
    RADIANT,
    NECROTIC,
    PURE;

    public static String getDamageString(DamageType type) {
        String damage = null;

        switch (type) {
            case PHYSICAL -> damage = "Physical";
            case FIRE -> damage = "Fire";
            case COLD -> damage = "Cold";
            case EARTH -> damage = "Earth";
            case LIGHTNING -> damage = "Lightning";
            case AIR -> damage = "Air";
            case RADIANT -> damage = "Radiant";
            case NECROTIC -> damage = "Necrotic";
            case PURE -> damage = "Pure";
        }

        return damage;
    }

    public static ChatColor getDamageColor(DamageType type) {
        ChatColor color = null;

        switch (type) {
            case PHYSICAL -> color = ChatColor.DARK_RED;
            case FIRE -> color = ChatColor.RED;
            case COLD -> color = ChatColor.AQUA;
            case EARTH -> color = ChatColor.DARK_GREEN;
            case LIGHTNING -> color = ChatColor.YELLOW;
            case AIR -> color = ChatColor.GRAY;
            case RADIANT, PURE -> color = ChatColor.WHITE;
            case NECROTIC -> color = ChatColor.DARK_PURPLE;
        }

        return color;
    }
}
