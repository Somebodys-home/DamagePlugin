package io.github.Gabriel.damagePlugin.customDamage;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import io.github.NoOne.nMLItems.ItemStat;
import io.github.NoOne.nMLPlayerStats.profileSystem.Profile;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomDamager {
    private static DamagePlugin plugin;
    public record DamageInstance(DamageType type, double damage) {}
    private static final Map<UUID, DamageInstance> damageInstance = new HashMap<>();

    public CustomDamager(DamagePlugin plugin) {
        this.plugin = plugin;
    }

    // todo: get rid of damage messages eventually
    // for multiple damage types
    public static void doDamage(LivingEntity target, LivingEntity damager, Map<DamageType, Double> damageSplits) {
        Profile profile = new ProfileManager(plugin.getNmlPlayerStats()).getPlayerProfile(target.getUniqueId());

        if (profile == null) {
            double totalDamage = 0;
            for (Map.Entry<DamageType, Double> entry : damageSplits.entrySet()) {
                damageInstance.put(target.getUniqueId(), new DamageInstance(entry.getKey(), entry.getValue()));
                totalDamage += entry.getValue();

                if (damager instanceof Player player && entry.getValue() > 0) {
                    player.sendMessage(DamageType.getDamageColor(entry.getKey()) + "You did " + entry.getValue() + " " + DamageType.getDamageString(entry.getKey()) + " damage!");
                }
            }

            target.setMetadata("recursive_block", new FixedMetadataValue(plugin, true));
            target.damage(totalDamage, damager);
            return;
        }

        Stats targetStats = profile.getStats();
        HashMap<ItemStat, Integer> resistedTypes = new HashMap<>();

        if (targetStats != null) {
            int evasion = targetStats.getEvasion();
            int random = (int) (Math.random() * 100) + 1;

            if (random <= evasion || evasion >= 100) {
                target.sendMessage("your evasion triggered! (" + evasion + "%)");
            } else {
                if (targetStats.getDefense() != 0) {
                    resistedTypes.put(ItemStat.DEFENSE, targetStats.getDefense());
                }
                if (targetStats.getPhysicalResist() != 0) {
                    resistedTypes.put(ItemStat.PHYSICALRESIST, targetStats.getPhysicalResist());
                }
                if (targetStats.getFireResist() != 0) {
                    resistedTypes.put(ItemStat.FIRERESIST, targetStats.getFireResist());
                }
                if (targetStats.getColdResist() != 0) {
                    resistedTypes.put(ItemStat.COLDRESIST, targetStats.getColdResist());
                }
                if (targetStats.getEarthResist() != 0) {
                    resistedTypes.put(ItemStat.EARTHRESIST, targetStats.getEarthResist());
                }
                if (targetStats.getLightningResist() != 0) {
                    resistedTypes.put(ItemStat.LIGHTNINGRESIST, targetStats.getLightningResist());
                }
                if (targetStats.getAirResist() != 0) {
                    resistedTypes.put(ItemStat.AIRRESIST, targetStats.getAirResist());
                }
                if (targetStats.getLightResist() != 0) {
                    resistedTypes.put(ItemStat.LIGHTRESIST, targetStats.getLightResist());
                }
                if (targetStats.getDarkResist() != 0) {
                    resistedTypes.put(ItemStat.DARKRESIST, targetStats.getDarkResist());
                }

                double totalDamage = 0;
                if (!resistedTypes.isEmpty()) { // has A resistance
                    for (Map.Entry<DamageType, Double> entry : damageSplits.entrySet()) {
                        double value = entry.getValue();

                        if (entry.getKey() != DamageType.PURE) {
                            value -= resistedTypes.getOrDefault(ItemStat.DEFENSE, 0);
                        }

                        switch (entry.getKey()) {
                            case PHYSICAL -> value -= resistedTypes.getOrDefault(ItemStat.PHYSICALRESIST, 0);
                            case FIRE -> value -= resistedTypes.getOrDefault(ItemStat.FIRERESIST, 0);
                            case COLD -> value -= resistedTypes.getOrDefault(ItemStat.COLDRESIST, 0);
                            case EARTH -> value -= resistedTypes.getOrDefault(ItemStat.EARTHRESIST, 0);
                            case LIGHTNING -> value -= resistedTypes.getOrDefault(ItemStat.LIGHTNINGRESIST, 0);
                            case AIR -> value -= resistedTypes.getOrDefault(ItemStat.AIRRESIST, 0);
                            case LIGHT -> value -= resistedTypes.getOrDefault(ItemStat.LIGHTRESIST, 0);
                            case DARK -> value -= resistedTypes.getOrDefault(ItemStat.DARKRESIST, 0);
                        }

                        value = Math.max(0, value); // Ensure no negative damage
                        entry.setValue(value);
                    }
                }

                for (Map.Entry<DamageType, Double> entry : damageSplits.entrySet()) {
                    damageInstance.put(target.getUniqueId(), new DamageInstance(entry.getKey(), entry.getValue()));
                    totalDamage += entry.getValue();

                    if (damager instanceof Player player && entry.getValue() > 0) {
                        player.sendMessage(DamageType.getDamageColor(entry.getKey()) + "You did " + entry.getValue() + " " + DamageType.getDamageString(entry.getKey()) + " damage!");
                    }
                }

                target.setMetadata("recursive_block", new FixedMetadataValue(plugin, true));
                target.damage(totalDamage, damager);
            }
        }
    }
}