package io.github.NoOne.damagePlugin;

import io.github.NoOne.damagePlugin.customDamage.*;
import io.github.NoOne.damagePlugin.customDamage.CustomDamager;
import io.github.NoOne.damagePlugin.noDamage.NoDamageListener;
import io.github.NoOne.damagePlugin.noDamage.NoDamageManager;
import io.github.NoOne.nMLMobs.NMLMobs;
import io.github.NoOne.nMLMobs.mobstats.MobStatsYMLManager;
import io.github.NoOne.nMLPlayerStats.NMLPlayerStats;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DamagePlugin extends JavaPlugin {
    private ProfileManager profileManager;
    private MobStatsYMLManager mobStatsYMLManager;
    private NoDamageManager noDamageManager;
    private CustomDamager customDamager;
    private DamageHelper damageHelper;

    @Override
    public void onEnable() {
        profileManager = JavaPlugin.getPlugin(NMLPlayerStats.class).getProfileManager();
        mobStatsYMLManager = JavaPlugin.getPlugin(NMLMobs.class).getMobStatsYMLManager();

        noDamageManager = new NoDamageManager();
        customDamager = new CustomDamager(this);
        damageHelper = new DamageHelper();

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

    public DamageHelper getDamageManager() {
        return damageHelper;
    }

    public MobStatsYMLManager getMobStatsYMLManager() {
        return mobStatsYMLManager;
    }
}
