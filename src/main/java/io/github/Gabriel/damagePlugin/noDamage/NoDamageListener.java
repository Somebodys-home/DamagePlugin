package io.github.Gabriel.damagePlugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class DamageAttributesListener implements Listener {
    private DamageAttributesManager manager;

    public DamageAttributesListener(DamagePlugin damagePlugin) {
        manager = damagePlugin.getDamageAttributesManager();
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        manager.removeAttributes(event.getItem().getItemStack(), event.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        manager.removeAttributes(event.getCurrentItem(), (Player) event.getClickedInventory().getHolder());
    }
}
