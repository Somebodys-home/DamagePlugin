package io.github.Gabriel.damagePlugin.customDamage;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomDamager {
    private final DamagePlugin plugin;
    public record DamageInstance(DamageType type, double damage) {}
    private static final Map<UUID, DamageInstance> damageInstance = new HashMap<>();

    public CustomDamager(DamagePlugin plugin) {
        this.plugin = plugin;
    }

    // todo: get rid of damage messages here eventually, theyre just debug anyways
    // for multiple damage types
    public static void doDamage(LivingEntity target, LivingEntity damager, Map<DamageType, Double> damageSplits) {
        double totalDamage = 0;

        for (Map.Entry<DamageType, Double> entry : damageSplits.entrySet()) {
            damageInstance.put(target.getUniqueId(), new DamageInstance(entry.getKey(), entry.getValue()));
            totalDamage += entry.getValue();

            if (damager instanceof Player player && entry.getValue() > 0) {
                player.sendMessage(DamageType.getDamageColor(entry.getKey()) + "You did " + entry.getValue() + " " + DamageType.getDamageString(entry.getKey()) + " damage!");
            }
        }

        target.damage(totalDamage);
    }

    // for single type damage
    public static void doDamage(LivingEntity target, LivingEntity damager, double damage, DamageType damageType) {
        damageInstance.put(target.getUniqueId(), new DamageInstance(damageType, damage));

        if (damager instanceof Player player) {
            player.sendMessage(DamageType.getDamageColor(damageType) + "You did " + damage + " " + DamageType.getDamageString(damageType) + " damage!");
        }

        target.damage(damage);
    }
}