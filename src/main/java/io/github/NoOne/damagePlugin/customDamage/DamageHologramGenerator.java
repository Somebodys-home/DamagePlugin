package io.github.NoOne.damagePlugin.customDamage;

import io.github.NoOne.damagePlugin.DamagePlugin;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class DamageHologramGenerator {
    public static HashMap<TextDisplay, Integer> damageDisplays = new HashMap<>();

    public static void startDamageDisplayTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Iterator<Map.Entry<TextDisplay, Integer>> it = damageDisplays.entrySet().iterator();

                while (it.hasNext()) {
                    Map.Entry<TextDisplay, Integer> entry = it.next();
                    TextDisplay textDisplay = entry.getKey();
                    int secondsLeft = entry.getValue() - 1;

                    if (secondsLeft <= 0) {
                        textDisplay.remove();
                        damageDisplays.remove(textDisplay);
                    } else {
                        damageDisplays.put(textDisplay, secondsLeft);
                    }
                }
            }
        }.runTaskTimer(DamagePlugin.getInstance(), 0, 1);
    }
    public static void createDamageHologram(LivingEntity damager, LivingEntity damaged, Map<DamageType, Double> damageSplits, boolean critHit) {
        Map<DamageType, Double> sortedDamageSplits = damageSplits.entrySet()
                .stream()
                .sorted(Map.Entry.<DamageType, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new // preserve order
                ));

        // making location
        Vector direction = damaged.getLocation().toVector().subtract(damager.getLocation().toVector());

        if (direction.length() > 0) {
            direction.normalize().multiply(-1.5);
        }

        Location location = damaged.getLocation().add(direction).add(0, damaged.getHeight() * 0.5, 0);
        double xFactor = ThreadLocalRandom.current().nextDouble(-.35, .65);
        double yFactor = ThreadLocalRandom.current().nextDouble(-.33, .33);
        double zFactor = ThreadLocalRandom.current().nextDouble(-.35, .65);

        location.setX(location.getX() + xFactor);
        location.setY(location.getY() + yFactor);
        location.setZ(location.getZ() + zFactor);
        location.setYaw(0);
        location.setPitch(0);

        // making name
        String name = "";

        for (Map.Entry<DamageType, Double> damageEntry : sortedDamageSplits.entrySet()) {
            double displayValue = Math.round(damageEntry.getValue() * 10.0) / 10.0;
            String formatted = displayValue == (int) displayValue ? String.valueOf((int) displayValue) : String.valueOf(displayValue);

            if (critHit) {
                name += DamageType.toChatColor(damageEntry.getKey()) + "§l" + formatted + " " + DamageType.toEmoji(damageEntry.getKey()) + " ";
            } else {
                name += DamageType.toChatColor(damageEntry.getKey()) + formatted + " " + DamageType.toEmoji(damageEntry.getKey()) + " ";
            }

        }

        String finalName = name;
        TextDisplay display = location.getWorld().spawn(location, TextDisplay.class, entity -> {
            entity.setText(finalName);
            entity.setBillboard(Display.Billboard.VERTICAL);
        });

        damageDisplays.put(display, 40); // 40 tick timer b4 its removed
    }
}
