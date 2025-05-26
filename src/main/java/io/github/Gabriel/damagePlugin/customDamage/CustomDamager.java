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
    private final Map<UUID, DamageInstance> damageInstance = new HashMap<>();

    public CustomDamager(DamagePlugin plugin) {
        this.plugin = plugin;
    }

    public void doDamage(LivingEntity target, LivingEntity damager, Map<DamageType, Double> damageSplits, DamageType overrideDamageType) {
        UUID uuid = target.getUniqueId();
        double totalDamage = 0;

        if (overrideDamageType == null) {
            for (Map.Entry<DamageType, Double> entry : damageSplits.entrySet()) {
                damageInstance.put(uuid, new DamageInstance(entry.getKey(), entry.getValue()));
                totalDamage += entry.getValue();

                if (damager instanceof Player player) {
                    player.sendMessage(DamageType.getDamageColor(entry.getKey()) + "You did " + entry.getValue() + " " + DamageType.getDamageString(entry.getKey()) + " damage!");
                }
            }
        } else {
            for (Map.Entry<DamageType, Double> entry : damageSplits.entrySet()) {
                totalDamage += entry.getValue();
            }

            damageInstance.put(uuid, new DamageInstance(overrideDamageType, totalDamage));

            if (damager instanceof Player player) {
                player.sendMessage(DamageType.getDamageColor(overrideDamageType) + "You did " + totalDamage + " " + DamageType.getDamageString(overrideDamageType) + " damage!");
            }
        }

        target.damage(totalDamage, damager);
    }
}