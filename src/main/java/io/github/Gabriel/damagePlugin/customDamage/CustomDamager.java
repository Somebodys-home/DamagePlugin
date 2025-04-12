package io.github.Gabriel.damagePlugin.customDamage;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import org.bukkit.Bukkit;
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

    public void doCustomDamage(LivingEntity target, LivingEntity damager, DamageType type, double damage, DamageSourceType source) {
        UUID uuid = target.getUniqueId();

        // Only fire your custom damage event once here, directly
        EntityTookCustomDamageEvent event = new EntityTookCustomDamageEvent(target, damager, type, damage);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) return;

        customDamageInstance.put(uuid, new DamageInstance(type, damage));
        target.damage(event.getDamage(), damager); // apply clean damage

        if (damager instanceof Player player && source == DamageSourceType.ABILITY) {
            player.sendMessage("§c[ABILITY] §fDealt " + event.getDamage() + " " + type.name().toLowerCase() + " damage.");
        }
    }


    public void clearDamageInstance(UUID uuid) {
        customDamageInstance.remove(uuid);
    }

    public DamageInstance getDamageInstance(UUID uuid) {
        return customDamageInstance.get(uuid);
    }
}
