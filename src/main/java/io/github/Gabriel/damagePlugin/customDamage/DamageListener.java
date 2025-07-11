package io.github.Gabriel.damagePlugin.customDamage;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Map;

public class DamageListener implements Listener {
    private final DamagePlugin plugin;

    public DamageListener(DamagePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByPlayer(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity target) || !(event.getDamager() instanceof Player player)) return;

        // Check for custom pre-calculated damage
        if (target.hasMetadata("custom_damage")) {
            double custom = target.getMetadata("custom_damage").get(0).asDouble();
            event.setDamage(custom);
            event.setCancelled(false);
            target.removeMetadata("custom_damage", plugin);
            return;
        }

        // Avoid processing our own synthetic damage
        if (event.getDamager().hasMetadata("custom_damager")) return;

        if (target.hasMetadata("custom-damage-processing")) return;

        target.setMetadata("custom-damage-processing", new FixedMetadataValue(plugin, true));

        try {
            ItemStack weapon = player.getInventory().getItemInMainHand();
            DamageKey damageKey = new DamageKey();
            Map<DamageType, Double> damageMap = new HashMap<>();

            if (weapon.getType() == Material.AIR || !damageKey.doesHaveDamageStats(weapon)) {
                damageMap.put(DamageType.PHYSICAL, 1.0);
            } else {
                for (DamageType type : DamageType.values()) {
                    if (damageKey.checkForDamageType(weapon, type) && damageKey.getDamageValue(weapon, type) > 0) {
                        damageMap.put(type, damageKey.getDamageValue(weapon, type));
                    }
                }
            }

            if (!damageMap.isEmpty()) {
                event.setCancelled(true);
            }

            CustomDamager.doDamage(target, player, damageMap);

        } finally {
            target.removeMetadata("custom-damage-processing", plugin);
        }
    }
}
