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
        if (damageSplits == null || damageSplits.isEmpty()) {
            return;
        }

        UUID uuid = target.getUniqueId();
        double totalDamage = damageSplits.values().stream().mapToDouble(Double::doubleValue).sum();

        // Determine the damage type to use
        DamageType damageTypeToUse = overrideDamageType != null ? overrideDamageType : damageSplits.size() == 1 ? damageSplits.keySet().iterator().next() :
                DamageType.PHYSICAL; // Default fallback

        // Store damage instance
        customDamageInstance.put(uuid, new DamageInstance(damageTypeToUse, totalDamage));

        // Send damage message if damager is a player
        if (damager instanceof Player player) {
            String message = DamageType.getDamageColor(damageTypeToUse) +
                    "You did " + totalDamage + " " +
                    DamageType.getDamageString(damageTypeToUse) + " damage!";
            player.sendMessage(message);
        }

        // Apply the damage
        if (totalDamage > 0 && !target.hasMetadata("custom-damage-processing")) {
            try {
                target.setMetadata("custom-damage-processing", new FixedMetadataValue(plugin, true));
                target.damage(totalDamage, damager);
            } finally {
                target.removeMetadata("custom-damage-processing", plugin);
            }
        }
    }
}