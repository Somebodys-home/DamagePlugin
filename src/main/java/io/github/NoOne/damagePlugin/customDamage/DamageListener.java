package io.github.NoOne.damagePlugin.customDamage;

import io.github.NoOne.damagePlugin.DamagePlugin;
import io.github.NoOne.nMLItems.ItemSystem;
import io.github.NoOne.nMLPlayerStats.profileSystem.Profile;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
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
    private ProfileManager profileManager;

    public DamageListener(DamagePlugin damagePlugin) {
        this.damagePlugin = damagePlugin;
        customDamager = damagePlugin.getCustomDamager();
        profileManager = damagePlugin.getProfileManager();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void doCustomDamage(CustomDamageEvent event) {
        LivingEntity target = event.getTarget();
        Profile targetProfile = profileManager.getPlayerProfile(target.getUniqueId());

        if (target.hasMetadata("hologram")) {
            event.setCancelled(true);
        }

        // evasion
        if (targetProfile != null) {
            Stats targetStats = targetProfile.getStats();
            int evasion = targetStats.getEvasion();
            int random = (int) (Math.random() * 100) + 1;

            if (random <= evasion || evasion >= 100) {
                target.sendMessage("your evasion triggered! (" + evasion + "%)");
                event.setCancelled(true);
                return;
            }
        }

        if (!event.isCancelled()) customDamager.doDamage(target, event.getDamager(), event.getDamageSplits(), event.isMobDamager());
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
            Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, fist, false));
        }
    }
}