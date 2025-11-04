package io.github.NoOne.damagePlugin.customDamage;

import io.github.NoOne.damagePlugin.DamagePlugin;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class DamageHologramGenerator {
    public static void createDamageHologram(DamagePlugin damagePlugin, LivingEntity damaged, Map<DamageType, Double> damageSplits) {
        Map<DamageType, Double> sortedDamageSplits = damageSplits.entrySet()
                .stream()
                .sorted(Map.Entry.<DamageType, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // merge function (not used)
                        LinkedHashMap::new // preserve order
                ));

        Location location = damaged.getLocation().add(
                ThreadLocalRandom.current().nextDouble(-1, 1),
                ThreadLocalRandom.current().nextDouble(.5, damaged.getHeight()),
                ThreadLocalRandom.current().nextDouble(-1, 1));

        ArmorStand hologram = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        String name = "";

        for (Map.Entry<DamageType, Double> damageEntry : sortedDamageSplits.entrySet()) {
            double displayValue = Math.round(damageEntry.getValue() * 10.0) / 10.0;
            String formatted = (displayValue % 1 == 0)
                    ? String.valueOf((int) displayValue)
                    : String.valueOf(displayValue);

            name += DamageType.getDamageColor(damageEntry.getKey()) + formatted + " " + DamageType.getDamageEmoji(damageEntry.getKey()) + " ";
        }

        hologram.setMetadata("hologram", new FixedMetadataValue(damagePlugin, true));
        hologram.setCustomName(name);
        hologram.setCustomNameVisible(true);
        hologram.setVisible(false);
        hologram.setGravity(false);
        hologram.setMarker(true); // Removes hitbox.
        hologram.setSmall(true); // Makes the text appear smaller and cleaner.
        hologram.setInvulnerable(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                hologram.remove();
            }
        }.runTaskLater(damagePlugin, 40L);
    }
}
