package io.github.Gabriel.damagePlugin.noDamage;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class NoDamageListener implements Listener {
    private NoDamageManager manager;

    public NoDamageListener(DamagePlugin damagePlugin) {
        manager = damagePlugin.getDamageAttributesManager();
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        manager.removeAttributes(event.getItem().getItemStack());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        manager.removeAttributes(event.getCurrentItem());
    }
}
