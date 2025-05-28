package io.github.Gabriel.damagePlugin.customDamage.commands;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import io.github.Gabriel.damagePlugin.customDamage.DamageKey;
import io.github.Gabriel.damagePlugin.customDamage.DamageType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static io.github.Gabriel.damagePlugin.customDamage.DamageType.*;

public class SetDamageCommand implements CommandExecutor {
    private static DamagePlugin plugin;

    public SetDamageCommand(DamagePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player && args.length >= 2) {
            ItemStack item = player.getInventory().getItemInMainHand();
            DamageKey damageKey = new DamageKey(item, plugin);
            String inputType = args[0].toLowerCase();
            int damage = Integer.parseInt(args[1]);

            switch (inputType) {
                case "physical" -> damageKey.setDamage(PHYSICAL, damage);
                case "fire"     -> damageKey.setDamage(FIRE, damage);
                case "cold"     -> damageKey.setDamage(COLD, damage);
                case "earth"    -> damageKey.setDamage(EARTH, damage);
                case "lightning"-> damageKey.setDamage(LIGHTNING, damage);
                case "air"      -> damageKey.setDamage(AIR, damage);
                case "light"    -> damageKey.setDamage(LIGHT, damage);
                case "dark"     -> damageKey.setDamage(DARK, damage);
                case "pure"     -> damageKey.setDamage(PURE, damage);
                default         -> {
                    player.sendMessage("Invalid damage type.");
                    return true;
                }
            }

            player.getInventory().setItemInMainHand(item);
            player.sendMessage("Set " + DamageType.getDamageString(damageKey.getDamageType()) + " damage to " + damage + "!");
        } else {
            sender.sendMessage("Usage: /setdamage <type> <amount>");
        }

        return true;
    }
}
