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
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player && args.length >= 2) {
            ItemStack item = player.getInventory().getItemInMainHand();
            DamageKey damageKey = new DamageKey(item);
            String inputType = args[0].toLowerCase();
            int damage = Integer.parseInt(args[1]);
            DamageType damageType = null;

            switch (inputType) {
                case "physical" -> damageType = PHYSICAL;
                case "fire"     -> damageType = FIRE;
                case "cold"     -> damageType = COLD;
                case "earth"    -> damageType = EARTH;
                case "lightning"-> damageType = LIGHTNING;
                case "air"      -> damageType = AIR;
                case "light"    -> damageType = LIGHT;
                case "dark"     -> damageType = DARK;
                case "pure"     -> damageType = PURE;
                default         -> {
                    player.sendMessage("Invalid damage type.");
                    return true;
                }
            }

            damageKey.setDamage(damageType, damage);
            player.getInventory().setItemInMainHand(item);
            player.sendMessage("Set " + DamageType.getDamageString(damageType) + " damage to " + damage + "!");
        } else {
            sender.sendMessage("Usage: /setdamage <type> <amount>");
        }

        return true;
    }
}
