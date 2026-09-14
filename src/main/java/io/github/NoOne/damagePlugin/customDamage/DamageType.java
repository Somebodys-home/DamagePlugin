package io.github.NoOne.damagePlugin.customDamage;

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

    public static String toString(DamageType damageType) {
        return switch (damageType) {
            case PHYSICAL -> "Physical";
            case FIRE -> "Fire";
            case COLD -> "Cold";
            case EARTH -> "Earth";
            case LIGHTNING -> "Lightning";
            case AIR -> "Air";
            case RADIANT -> "Radiant";
            case NECROTIC -> "Necrotic";
            case PURE -> "Pure";
        };
    }

    public static String toChatColor(DamageType damageType) {
        return switch (damageType) {
            case PHYSICAL -> "§4";
            case FIRE -> "§c";
            case COLD -> "§b";
            case EARTH -> "§2";
            case LIGHTNING -> "§e";
            case AIR -> "§7";
            case RADIANT, PURE -> "§f";
            case NECROTIC -> "§5";
        };
    }

    public static String toEmoji(DamageType damageType) {
        return switch (damageType) {
            case PHYSICAL -> "⚔";
            case FIRE -> "\uD83D\uDD25";
            case COLD -> "❄";
            case EARTH -> "\uD83E\uDEA8";
            case LIGHTNING -> "\uD83D\uDDF2";
            case AIR -> "☁";
            case RADIANT -> "✦";
            case NECROTIC -> "\uD83C\uDF00";
            case PURE -> "\uD83D\uDCA2";
        };
    }
}
