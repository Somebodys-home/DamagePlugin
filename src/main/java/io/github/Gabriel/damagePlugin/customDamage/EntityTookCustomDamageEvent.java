package io.github.Gabriel.damagePlugin.customDamage;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;

public class EntityTookCustomDamageEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final LivingEntity target;
    private final LivingEntity damager;
    private final DamageType damageType;
    private double damage;
    private boolean cancelled;

    public EntityTookCustomDamageEvent(LivingEntity target, LivingEntity damager, DamageType type, double damage) {
        this.target = target;
        this.damager = damager;
        this.damageType = type;
        this.damage = damage;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public LivingEntity getDamager() {
        return damager;
    }

    public DamageType getDamageType() {
        return damageType;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
