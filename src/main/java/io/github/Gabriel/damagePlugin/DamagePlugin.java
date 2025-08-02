package io.github.Gabriel.damagePlugin;

import io.github.Gabriel.damagePlugin.customDamage.*;
import io.github.Gabriel.damagePlugin.customDamage.CustomDamager;
import io.github.Gabriel.damagePlugin.customDamage.DamageLore;
import io.github.Gabriel.damagePlugin.noDamage.NoDamageListener;
import io.github.Gabriel.damagePlugin.noDamage.NoDamageManager;
import io.github.NoOne.nMLPlayerStats.NMLPlayerStats;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class DamagePlugin extends JavaPlugin {
    private static DamagePlugin damagePlugin;
    private NoDamageManager noDamageManager;
    private CustomDamager customDamager;
    private DamageLore damageLore;
    private static NMLPlayerStats nmlPlayerStats;
    private ProfileManager profileManager;
    private DamageManager damageManager;

    @Override
    public void onEnable() {
        damagePlugin = this;
        noDamageManager = new NoDamageManager(this);
        customDamager = new CustomDamager(this);
        damageLore = new DamageLore();
        damageManager = new DamageManager();

        Plugin plugin = Bukkit.getPluginManager().getPlugin("NMLPlayerStats");
        if (plugin instanceof NMLPlayerStats statsPlugin) {
            nmlPlayerStats = statsPlugin;
            profileManager = nmlPlayerStats.getProfileManager();
        } else {
            getLogger().severe("Failed to find NMLPlayerStats! Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new NoDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
    }

    public static DamagePlugin getInstance() {
        return damagePlugin;
    }

    public NoDamageManager getNoDamageManager() {
        return noDamageManager;
    }

    public CustomDamager getCustomDamager() {
        return customDamager;
    }

    public DamageLore getDamageLore() {
        return damageLore;
    }

    public static NMLPlayerStats getNmlPlayerStats() {
        return nmlPlayerStats;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public DamageManager getDamageManager() {
        return damageManager;
    }
}
