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

        if (event.getDamageSource().getDamageType() == org.bukkit.damage.DamageType.BAD_RESPAWN_POINT) {
            player.sendMessage("canceled cuz of main attack");
            event.setCancelled(true);
            return;
        }

        // Check for custom pre-calculated damage
        if (target.hasMetadata("custom_damage")) {
            double custom = target.getMetadata("custom_damage").get(0).asDouble();
            event.setDamage(custom);
            event.setCancelled(false);
            target.removeMetadata("custom_damage", plugin);
            return;
        }

        target.setMetadata("custom-damage-processing", new FixedMetadataValue(plugin, true));

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

            CustomDamager.doDamage(target, player, damageMap);

        } finally {
            target.removeMetadata("custom-damage-processing", plugin);
        }
    }
}
