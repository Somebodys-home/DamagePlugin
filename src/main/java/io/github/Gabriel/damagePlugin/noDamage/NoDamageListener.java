package io.github.Gabriel.damagePlugin.noDamage;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;


public class NoDamageListener implements Listener {
    private final NoDamageManager manager;

    public NoDamageListener(DamagePlugin plugin) {
        this.manager = plugin.getDamageAttributesManager();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        ItemStack current = event.getCurrentItem();
        ItemStack cursor = event.getCursor();

        manager.removeAttributes(current);
        manager.removeAttributes(cursor);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        for (ItemStack item : event.getNewItems().values()) {
            manager.removeAttributes(item);
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        manager.removeAttributes(event.getItem().getItemStack());
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        manager.removeAttributes(event.getMainHandItem());
        manager.removeAttributes(event.getOffHandItem());
    }
}