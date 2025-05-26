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
    private final CustomDamager customDamager;
    private final DamagePlugin plugin;

    public DamageListener(DamagePlugin plugin) {
        this.plugin = plugin;
        this.customDamager = plugin.getCustomDamager();
    }

    @EventHandler
    public void onEntityDamageByPlayer(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity target) || !(event.getDamager() instanceof Player player) || target.hasMetadata("custom-damage-processing")) {return;}

        target.setMetadata("custom-damage-processing", new FixedMetadataValue(plugin, true));

        try {
            ItemStack weapon = player.getInventory().getItemInMainHand();
            Map<DamageType, Double> damageMap = new HashMap<>();

            if (weapon.getType() == Material.AIR) {
                damageMap.put(DamageType.PHYSICAL, 1.0);
            } else {
                DamageKey damageKey = new DamageKey(weapon);
                for (DamageType type : DamageType.values()) {
                    if (damageKey.checkForDamageType(type) && damageKey.getDamageValue(type) > 0) {
                        damageMap.put(type, damageKey.getDamageValue(type));
                    }
                }
            }

            if (!damageMap.isEmpty()) {
                event.setCancelled(true);
            }

            customDamager.doDamage(target, player, damageMap);

        } finally {
            target.removeMetadata("custom-damage-processing", plugin);
        }
    }
}
