package io.github.Gabriel.damagePlugin.customDamage;

import org.bukkit.ChatColor;

public enum DamageType {
    PHYSICAL,
    FIRE,
    COLD,
    EARTH,
    LIGHTNING,
    AIR,
    LIGHT,
    DARK,
    PURE;

    public static String getDamageString(DamageType type) {
        switch (type) {
            case PHYSICAL -> {
                return "Physical";
            }
            case FIRE -> {
                return "Fire";
            }
            case COLD -> {
                return "Cold";
            }
            case EARTH -> {
                return "Earth";
            }
            case LIGHTNING -> {
                return "Lightning";
            }
            case AIR -> {
                return "Air";
            }
            case LIGHT -> {
                return "Light";
            }
            case DARK -> {
                return "Dark";
            }
            case PURE -> {
                return "Pure";
            }
            default -> { return null; }
        }
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
            case LIGHT, PURE -> color = ChatColor.WHITE;
            case DARK -> color = ChatColor.DARK_PURPLE;
        }

        return color;
    }
}
