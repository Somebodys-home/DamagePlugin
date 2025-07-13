package io.github.Gabriel.damagePlugin;

import io.github.Gabriel.damagePlugin.customDamage.*;
import io.github.Gabriel.damagePlugin.customDamage.CustomDamager;
import io.github.Gabriel.damagePlugin.customDamage.DamageLoreUtil;
import io.github.Gabriel.damagePlugin.noDamage.NoDamageListener;
import io.github.Gabriel.damagePlugin.noDamage.NoDamageManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DamagePlugin extends JavaPlugin {
    private static DamagePlugin damagePlugin;
    private NoDamageManager noDamageManager;
    private CustomDamager customDamager;
    private DamageLoreUtil damageLoreUtil;

    @Override
    public void onEnable() {
        damagePlugin = this;
        noDamageManager = new NoDamageManager(this);
        customDamager = new CustomDamager(this);

        getServer().getPluginManager().registerEvents(new NoDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
    }

    public static DamagePlugin getInstance() {
        return damagePlugin;
    }

    public NoDamageManager getDamageAttributesManager() {
        return noDamageManager;
    }

    public CustomDamager getCustomDamager() {
        return customDamager;
    }

    public DamageLoreUtil getDamageLoreUtil() {return damageLoreUtil;}
}
