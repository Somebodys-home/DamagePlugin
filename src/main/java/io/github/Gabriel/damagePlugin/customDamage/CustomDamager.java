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
    private final Map<UUID, DamageInstance> customDamageInstance = new HashMap<>();

    public CustomDamager(DamagePlugin plugin) {
        this.plugin = plugin;
    }

    public void doDamage(LivingEntity target, LivingEntity damager, Map<DamageType, Double> damageSplits, DamageType overrideDamageType) {
        UUID uuid = target.getUniqueId();
        double totalDamage = 0;

        if (overrideDamageType != null) {
            for (Map.Entry<DamageType, Double> entry : damageSplits.entrySet()) {
                customDamageInstance.put(uuid, new DamageInstance(entry.getKey(), entry.getValue()));
                totalDamage += entry.getValue();

                if (damager instanceof Player player) {
                    player.sendMessage(DamageType.getDamageColor(entry.getKey()) + "You did " + entry.getValue() + " damage!");
                }
            }
        } else {
            for (Map.Entry<DamageType, Double> entry : damageSplits.entrySet()) {
                totalDamage += entry.getValue();
            }
            customDamageInstance.put(uuid, new DamageInstance(overrideDamageType, totalDamage));

            if (damager instanceof Player player) {
                player.sendMessage(DamageType.getDamageColor(overrideDamageType) + "You did " + totalDamage + " damage!");
            }
        }

        if (totalDamage > 0 && !target.hasMetadata("custom-damage-processing")) {
            target.damage(totalDamage, damager);
            target.setMetadata("custom-damage-processing", new FixedMetadataValue(plugin, true));
        }

        target.removeMetadata("custom-damage-processing", plugin);
    }

    public void clearDamageInstance(UUID uuid) {
        customDamageInstance.remove(uuid);
    }

    public DamageInstance getDamageInstance(UUID uuid) {
        return customDamageInstance.get(uuid);
    }
}
