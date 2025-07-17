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

    @EventHandler()
    public void onEntityDamageByPlayerWithoutWeapon(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity livingEntity) || !(event.getDamager() instanceof Player player)) return;
        if (event.getDamageSource().getDamageType() == org.bukkit.damage.DamageType.DRY_OUT) return;

        // Stops recursive loops in their tracks
        if (livingEntity.hasMetadata("recursive_block")) {
            event.setCancelled(false);
            livingEntity.removeMetadata("recursive_block", plugin);
            return;
        }

        livingEntity.setMetadata("custom-damage-processing", new FixedMetadataValue(plugin, true));

        try {
            ItemStack weapon = player.getInventory().getItemInMainHand();
            DamageKey damageKey = new DamageKey();

            event.setCancelled(true);

            if (weapon.getType() == Material.AIR || !damageKey.doesHaveDamageStats(weapon)) {
                CustomDamager.doDamage(livingEntity, player, 1, DamageType.PHYSICAL);
            }

        } finally {
            livingEntity.removeMetadata("custom-damage-processing", plugin);
        }
    }
}