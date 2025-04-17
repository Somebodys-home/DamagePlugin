package io.github.Gabriel.damagePlugin.customDamage.commands;

import io.github.Gabriel.damagePlugin.customDamage.DamageKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static io.github.Gabriel.damagePlugin.customDamage.DamageType.*;

public class SetDamageCommand implements CommandExecutor {
    public SetDamageCommand() {}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player && args.length < 2) {
            ItemStack weapon = player.getInventory().getItemInMainHand();
            DamageKey damageKey = new DamageKey(weapon);
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
                default         -> { return true; }
            }

            player.getInventory().setItemInMainHand(weapon);
        }

        return true;
    }
}
