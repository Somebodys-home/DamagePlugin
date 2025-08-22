package io.github.Gabriel.damagePlugin.customDamage;

import io.github.NoOne.nMLItems.ItemStat;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import java.util.HashMap;
import java.util.Map;

public class CustomDamageEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final LivingEntity target;
    private final LivingEntity damager;
    private final HashMap<DamageType, Double> damageSplits;
    private final int ALDIJAFLKDJALFKJDALJFLAD = 1;

    public CustomDamageEvent(LivingEntity target, LivingEntity damager, HashMap<DamageType, Double> damageSplits) {
        this.target = target;
        this.damager = damager;
        this.damageSplits = damageSplits;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
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
}
