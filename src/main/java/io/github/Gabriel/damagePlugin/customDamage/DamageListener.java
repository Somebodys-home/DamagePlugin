package io.github.Gabriel.damagePlugin.customDamage;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamageByPlayer(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity livingEntity) || !(event.getDamager() instanceof Player player)) return;

        // Stops recursive loops in their tracks
        if (livingEntity.hasMetadata("recursive_block")) {
            event.setDamage(livingEntity.getMetadata("recursive_block").get(0).asDouble());
            livingEntity.removeMetadata("recursive_block", plugin);
            event.setCancelled(true);
            return;
        }
//        if (livingEntity.hasMetadata("custom_damage")) {
//            double custom = livingEntity.getMetadata("custom_damage").get(0).asDouble();
//            event.setDamage(custom);
//            event.setCancelled(false);
//            livingEntity.removeMetadata("custom_damage", plugin);
//            return;
//        }

        livingEntity.setMetadata("custom-damage-processing", new FixedMetadataValue(plugin, true));

        try {
            ItemStack weapon = player.getInventory().getItemInMainHand();
            DamageKey damageKey = new DamageKey();
            Map<DamageType, Double> damageMap = new HashMap<>();

            if (weapon.getType() == Material.AIR || !damageKey.doesHaveDamageStats(weapon)) {
                damageMap.put(DamageType.PHYSICAL, 1.0);
            } else {
                HashMap<DamageType, Double> damageStats = damageKey.getAllDamageStats(weapon);
                damageStats.forEach((type, value) -> {
                    damageMap.put(type, value);
                });
            }

            if (!damageMap.isEmpty()) {
                event.setCancelled(true);
            }

            CustomDamager.doDamage(livingEntity, player, damageMap);

        } finally {
            livingEntity.removeMetadata("custom-damage-processing", plugin);
        }
    }
}