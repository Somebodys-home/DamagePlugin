package io.github.NoOne.damagePlugin.customDamage;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import java.util.HashMap;

public class CustomDamageEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final LivingEntity target;
    private final LivingEntity damager;
    private final HashMap<DamageType, Double> damageSplits;
    private boolean cancelled;
    private boolean mobDamager = false;

    public CustomDamageEvent(LivingEntity target, LivingEntity damager, HashMap<DamageType, Double> damageSplits) {
        this.target = target;
        this.damager = damager;
        this.damageSplits = damageSplits;
        this.cancelled = false;

    }

    public CustomDamageEvent(LivingEntity target, LivingEntity damager, HashMap<DamageType, Double> damageSplits, boolean mobDamager) {
        this.target = target;
        this.damager = damager;
        this.damageSplits = damageSplits;
        this.cancelled = false;
        this.mobDamager = mobDamager;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public LivingEntity getDamager() {
        return damager;
    }

    public HashMap<DamageType, Double> getDamageSplits() {
        return damageSplits;
    }

    public boolean isMobDamager() {
        return mobDamager;
    }

    public void setMobDamager(boolean mobDamager) {
        this.mobDamager = mobDamager;
    }
}