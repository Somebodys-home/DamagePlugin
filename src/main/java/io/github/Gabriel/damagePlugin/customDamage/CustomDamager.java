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

    // todo: get rid of damage messages here eventually, theyre just debug anyways
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
            for (double value : damageSplits.values()) {
                totalDamage += value;
            }

            damageInstance.put(uuid, new DamageInstance(overrideDamageType, totalDamage));

            // ✅ Only send **one** message here
            if (damager instanceof Player player) {
                player.sendMessage(DamageType.getDamageColor(overrideDamageType) + "You did " + totalDamage + " " + DamageType.getDamageString(overrideDamageType) + " damage!");
            }
        }

        target.damage(totalDamage, damager);
    }

}