package io.github.Gabriel.damagePlugin;

import io.github.Gabriel.damagePlugin.customDamage.*;
import io.github.Gabriel.damagePlugin.customDamage.CustomDamager;
import io.github.Gabriel.damagePlugin.noDamage.NoDamageListener;
import io.github.Gabriel.damagePlugin.noDamage.NoDamageManager;
import io.github.NoOne.nMLMobs.NMLMobs;
import io.github.NoOne.nMLMobs.mobstats.MobStatsYMLManager;
import io.github.NoOne.nMLPlayerStats.NMLPlayerStats;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class DamagePlugin extends JavaPlugin {
    private ProfileManager profileManager;
    private MobStatsYMLManager mobStatsYMLManager;
    private NoDamageManager noDamageManager;
    private CustomDamager customDamager;
    private DamageConverter damageConverter;

    @Override
    public void onEnable() {
        profileManager = JavaPlugin.getPlugin(NMLPlayerStats.class).getProfileManager();
        mobStatsYMLManager = JavaPlugin.getPlugin(NMLMobs.class).getMobStatsYMLManager();

        noDamageManager = new NoDamageManager();
        customDamager = new CustomDamager(this);
        damageConverter = new DamageConverter();

        getServer().getPluginManager().registerEvents(new NoDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
    }

    public NoDamageManager getNoDamageManager() {
        return noDamageManager;
    }

    public CustomDamager getCustomDamager() {
        return customDamager;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public DamageConverter getDamageManager() {
        return damageConverter;
    }

    public MobStatsYMLManager getMobStatsYMLManager() {
        return mobStatsYMLManager;
    }
}
