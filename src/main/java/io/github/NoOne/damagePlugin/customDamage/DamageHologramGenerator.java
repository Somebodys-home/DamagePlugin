package io.github.NoOne.damagePlugin.customDamage;

import io.github.NoOne.damagePlugin.DamagePlugin;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class DamageHologramGenerator {
    public static void createDamageHologram(DamagePlugin damagePlugin, LivingEntity damager, LivingEntity damaged, Map<DamageType, Double> damageSplits, boolean critHit) {
        Map<DamageType, Double> sortedDamageSplits = damageSplits.entrySet()
                .stream()
                .sorted(Map.Entry.<DamageType, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // merge function (not used)
                        LinkedHashMap::new // preserve order
                ));

        Vector direction = damaged.getLocation().toVector().subtract(damager.getLocation().toVector()).normalize().multiply(-1.5);
        Location location = damaged.getLocation().add(direction).add(0, damaged.getHeight() * 0.5, 0);
        double xFactor = ThreadLocalRandom.current().nextDouble(-.35, .65);
        double yFactor = ThreadLocalRandom.current().nextDouble(-.33, .33);
        double zFactor = ThreadLocalRandom.current().nextDouble(-.35, .65);

        location.setX(location.getX() + xFactor);
        location.setY(location.getY() + yFactor);
        location.setZ(location.getZ() + zFactor);

        ArmorStand hologram = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        String name = "";

        for (Map.Entry<DamageType, Double> damageEntry : sortedDamageSplits.entrySet()) {
            double displayValue = Math.round(damageEntry.getValue() * 10.0) / 10.0;
            String formatted = (displayValue % 1 == 0)
                    ? String.valueOf((int) displayValue)
                    : String.valueOf(displayValue);

            if (critHit) {
                name += DamageType.getDamageColor(damageEntry.getKey()) + "§l" + formatted + " " + DamageType.getDamageEmoji(damageEntry.getKey()) + " ";
            } else {
                name += DamageType.getDamageColor(damageEntry.getKey()) + formatted + " " + DamageType.getDamageEmoji(damageEntry.getKey()) + " ";
            }

        }

        hologram.setMetadata("hologram", new FixedMetadataValue(damagePlugin, true));
        hologram.setCustomName(name);
        hologram.setCustomNameVisible(true);
        hologram.setVisible(false);
        hologram.setGravity(false);
        hologram.setMarker(true); // Removes hitbox.
        hologram.setInvulnerable(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                hologram.remove();
            }
        }.runTaskLater(damagePlugin, 40L);
    }
}
