package io.github.Gabriel.damagePlugin.customDamage;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import io.github.NoOne.nMLItems.ItemSystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class DamageListener implements Listener {
    private DamagePlugin damagePlugin;
    private CustomDamager customDamager;

    public DamageListener(DamagePlugin damagePlugin) {
        this.damagePlugin = damagePlugin;
        customDamager = damagePlugin.getCustomDamager();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void doCustomDamage(CustomDamageEvent event) {
        if (CustomDamageEvent.isInsideCustomDamage()) return;
        if (!event.isCancelled()) {
            customDamager.doDamage(event.getTarget(), event.getDamager(), event.getDamageSplits());
        }
    }

    @EventHandler()
    public void onDamageWithoutWeapon(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity livingEntity) || !(event.getDamager() instanceof Player player)) return;
        if (event.getDamageSource().getDamageType() == org.bukkit.damage.DamageType.DRY_OUT) return;

        // Stops recursive loops in their tracks
        if (livingEntity.hasMetadata("punched")) {
            event.setCancelled(false);
            livingEntity.removeMetadata("punched", damagePlugin);
            return;
        }

        ItemStack weapon = player.getInventory().getItemInMainHand();
        event.setCancelled(true);

        if (weapon.getType() == Material.AIR || !ItemSystem.hasDamageStats(weapon)) {
            HashMap<DamageType, Double> fist = new HashMap<>();
            fist.put(DamageType.PHYSICAL, 1.0);
            Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, fist));
        }
    }
}