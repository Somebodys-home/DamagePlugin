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
    private final DamageKeys damageKeys;
    private final CustomDamager customDamager;
    private final DamagePlugin plugin;

    public DamageListener(DamagePlugin plugin) {
        this.plugin = plugin;
        this.damageKeys = plugin.getDamageKeys();
        this.customDamager = plugin.getCustomDamager();
    }

    @EventHandler
    public void onEntityDamageByPlayer(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity damagee)) return;
        if (!(event.getDamager() instanceof Player player)) return;
        if (damagee.hasMetadata("custom-damage-processing")) return;

        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon == null || weapon.getType() == Material.AIR) return;

        int totalDamage = 0;
        Map<DamageType, Integer> appliedTypes = new HashMap<>();

        for (DamageType type : DamageType.values()) {
            if (damageKeys.checkForDamageKey(weapon, type)) {
                int value = damageKeys.getDamageKeyValue(weapon, type);
                totalDamage += value;
                appliedTypes.put(type, value);
            }
        }

        if (appliedTypes.isEmpty()) {
            return;
        }

        // You could fire an event here with full type breakdown if needed

        // Prevent recursion
        damagee.setMetadata("custom-damage-processing", new FixedMetadataValue(plugin, true));

        // Apply each elemental type separately (or summed, depending on how CustomDamager is set up)
        for (Map.Entry<DamageType, Integer> entry : appliedTypes.entrySet()) {
            customDamager.doCustomDamage(damagee, player, entry.getKey(), entry.getValue());
        }

        event.setCancelled(true);
        Bukkit.getScheduler().runTask(plugin, () -> damagee.removeMetadata("custom-damage-processing", plugin));
    }
}
