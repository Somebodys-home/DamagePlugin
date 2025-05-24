package io.github.Gabriel.damagePlugin.customDamage;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import org.bukkit.Bukkit;
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
        if (event.getEntity() instanceof LivingEntity damagee && event.getDamager() instanceof Player player && !(damagee.hasMetadata("custom-damage-processing"))) {
            ItemStack weapon = player.getInventory().getItemInMainHand();
            Map<DamageType, Integer> appliedTypes = new HashMap<>();

            if (weapon.getType() == Material.AIR) {
                appliedTypes.put(DamageType.PHYSICAL, 1);
            } else {
                DamageKey damageKey = new DamageKey(weapon);
                for (DamageType type : DamageType.values()) {
                    if (damageKey.checkForDamageType(type)) {
                        int value = damageKey.getDamageValue(type);
                        appliedTypes.put(type, value);
                    }
                }
            }

            for (Map.Entry<DamageType, Integer> entry : appliedTypes.entrySet()) {
                customDamager.doDamage(damagee, player, entry.getKey(), entry.getValue());
            }

            damagee.setMetadata("custom-damage-processing", new FixedMetadataValue(plugin, true));
            Bukkit.getScheduler().runTask(plugin, () -> damagee.removeMetadata("custom-damage-processing", plugin));
        }
    }
}
