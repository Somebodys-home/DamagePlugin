package io.github.Gabriel.damagePlugin.customDamage.damageLore;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class DamageLoreListener implements Listener {
    private DamagePlugin plugin;

    public DamageLoreListener(DamagePlugin plugin) {
        this.plugin = plugin;
    }
    public DamageLoreListener() {}

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        update(event.getCurrentItem());
        update(event.getCursor());
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        for (ItemStack item : event.getNewItems().values()) {
            update(item);
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        update(event.getItem().getItemStack());
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        update(event.getMainHandItem());
        update(event.getOffHandItem());
    }

    private void update(ItemStack item) {
        if (item == null || item.getType().isAir()) return;
        DamageLoreUtil.updateLoreWithElementalDamage(item, plugin);
    }
}
