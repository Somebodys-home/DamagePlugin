package io.github.Gabriel.damagePlugin;

import io.github.Gabriel.damagePlugin.customDamage.*;
import io.github.Gabriel.damagePlugin.customDamage.commands.SetDamageCommand;
import io.github.Gabriel.damagePlugin.customDamage.DamageLoreListener;
import io.github.Gabriel.damagePlugin.noDamage.NoDamageListener;
import io.github.Gabriel.damagePlugin.noDamage.NoDamageManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DamagePlugin extends JavaPlugin {
    private static DamagePlugin damagePlugin;
    private NoDamageManager noDamageManager;
    private CustomDamager customDamager;

    @Override
    public void onEnable() {
        damagePlugin = this;
        noDamageManager = new NoDamageManager(this);
        customDamager = new CustomDamager(this);

        getCommand("setDamage").setExecutor(new SetDamageCommand());
        getServer().getPluginManager().registerEvents(new NoDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        getServer().getPluginManager().registerEvents(new DamageLoreListener(), this);
    }

    public NoDamageManager getDamageAttributesManager() {
        return noDamageManager;
    }

    public CustomDamager getCustomDamager() {
        return customDamager;
    }

    public static DamagePlugin getInstance() {
        return damagePlugin;
    }
}
