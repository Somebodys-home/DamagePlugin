package io.github.Gabriel.damagePlugin.customDamage;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CustomDamageEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String source;

    public CustomDamageEvent(@NotNull Player player, String source) {
        this.player = player;
        this.source = source;
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; } // deleting this breaks things, apparently

    public Player getPlayer() { return player; }

    public String getSource() { return source; }
}
