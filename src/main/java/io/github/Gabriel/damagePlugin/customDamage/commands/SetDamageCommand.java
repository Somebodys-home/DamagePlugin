package io.github.Gabriel.damagePlugin.customDamage.commands;

import io.github.Gabriel.damagePlugin.customDamage.DamageKey;
import io.github.Gabriel.damagePlugin.customDamage.DamageType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.github.Gabriel.damagePlugin.customDamage.DamageType.*;

public class SetDamageCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            DamageKey damageKey = new DamageKey(player.getInventory().getItemInMainHand());
            damageKey.setDamage(PHYSICAL, 5);
            player.sendMessage("Set " + DamageType.getDamageString(PHYSICAL) + " damage to 5!");
        }

//        if (sender instanceof Player player && args.length < 2) {
//            DamageKey damageKey = new DamageKey(player.getInventory().getItemInMainHand());
//            String inputType = args[0].toLowerCase();
//            int damage = Integer.parseInt(args[1]);
//
//            switch (inputType) {
//                case "physical" -> damageKey.setDamage(PHYSICAL, damage);
//                case "fire"     -> damageKey.setDamage(FIRE, damage);
//                case "cold"     -> damageKey.setDamage(COLD, damage);
//                case "earth"    -> damageKey.setDamage(EARTH, damage);
//                case "lightning"-> damageKey.setDamage(LIGHTNING, damage);
//                case "air"      -> damageKey.setDamage(AIR, damage);
//                case "light"    -> damageKey.setDamage(LIGHT, damage);
//                case "dark"     -> damageKey.setDamage(DARK, damage);
//                case "pure"     -> damageKey.setDamage(PURE, damage);
//                default         -> { return true; }
//            }
//
//            player.sendMessage("Set " + DamageType.getDamageString(damageKey.getDamageType()) + " damage to " + damage + "!");
//        }

        return true;
    }
}
