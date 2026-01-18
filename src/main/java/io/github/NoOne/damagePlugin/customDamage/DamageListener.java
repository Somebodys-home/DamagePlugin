package io.github.NoOne.damagePlugin.customDamage;

import io.github.NoOne.damagePlugin.DamagePlugin;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

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

        if (!DamageHelper.isMobDamageable(target)) event.setCancelled(true);
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