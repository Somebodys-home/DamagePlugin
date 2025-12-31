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
    private CustomDamager customDamager;
    private ProfileManager profileManager;

    public DamageListener(DamagePlugin damagePlugin) {
        customDamager = damagePlugin.getCustomDamager();
        profileManager = damagePlugin.getProfileManager();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void doCustomDamage(CustomDamageEvent event) {
        LivingEntity target = event.getTarget();

        if (target.hasMetadata("hologram") || target.getNoDamageTicks() > 0) event.setCancelled(true);
        if (!event.isCancelled()) {
            if (event.isMobDamager()) {
                if (target instanceof Player player) {
                    Stats targetStats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();
                    int evasion = targetStats.getEvasion();
                    int random = (int) (Math.random() * 100) + 1;

                    if (random <= evasion || evasion >= 100) { // evasion
                        player.sendMessage("your evasion triggered! (" + evasion + "%)");
                        event.setCancelled(true);
                        return;
                    }

                    customDamager.doDamage(player, event.getDamager(), event.getDamageSplits(), true);
                    player.setNoDamageTicks(20);
                }
            } else {
                customDamager.doDamage(target, event.getDamager(), event.getDamageSplits(), false);
                target.setNoDamageTicks(0);
            }
        }
    }
}