package io.github.Gabriel.damagePlugin.customDamage;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomDamager {
    private final DamagePlugin plugin;
    public record DamageInstance(DamageType type, int damage) {}
    private final Map<UUID, DamageInstance> customDamageInstance = new HashMap<>();

    public CustomDamager(DamagePlugin plugin) {
        this.plugin = plugin;
    }

    public void doCustomDamage(LivingEntity target, LivingEntity damager, DamageType type, int damage) {
        UUID uuid = target.getUniqueId();

        customDamageInstance.put(uuid, new DamageInstance(type, damage));
        target.damage(damage, damager);

        if (damager instanceof Player player) {
            ChatColor color = null;

            switch (type) {
                case PHYSICAL -> color = ChatColor.DARK_RED;
                case FIRE -> color = ChatColor.RED;
                case COLD -> color = ChatColor.AQUA;
                case EARTH -> color = ChatColor.DARK_GREEN;
                case LIGHTNING -> color = ChatColor.YELLOW;
                case AIR -> color = ChatColor.GRAY;
                case LIGHT -> color = ChatColor.WHITE;
                case DARK -> color = ChatColor.DARK_PURPLE;
                case PURE -> color = ChatColor.WHITE;
            }

            player.sendMessage(color + "You just did " + damage + " " + type.name().toLowerCase() + " damage!");
        }
    }

    public void clearDamageInstance(UUID uuid) {
        customDamageInstance.remove(uuid);
    }

    public DamageInstance getDamageInstance(UUID uuid) {
        return customDamageInstance.get(uuid);
    }
}
