package io.github.Gabriel.damagePlugin.customDamage.commands;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import io.github.Gabriel.damagePlugin.customDamage.DamageKeys;
import io.github.Gabriel.damagePlugin.customDamage.DamageType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SetDamageCommand implements CommandExecutor {

    private final DamageKeys damageKeys;

    public SetDamageCommand(DamagePlugin plugin) {
        this.damageKeys = plugin.getDamageKeys();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (args.length < 2) return true;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.hasItemMeta()) return true;

        String inputType = args[0].toLowerCase();
        String inputAmount = args[1];

        int damage;
        try {
            damage = Integer.parseInt(inputAmount);
        } catch (NumberFormatException e) {
            return true;
        }

        switch (inputType) {
            case "physical" -> damageKeys.setDamageKey(item, DamageType.PHYSICAL, damage);
            case "fire"     -> damageKeys.setDamageKey(item, DamageType.FIRE, damage);
            case "cold"     -> damageKeys.setDamageKey(item, DamageType.COLD, damage);
            case "earth"    -> damageKeys.setDamageKey(item, DamageType.EARTH, damage);
            case "lightning"-> damageKeys.setDamageKey(item, DamageType.LIGHTNING, damage);
            case "air"      -> damageKeys.setDamageKey(item, DamageType.AIR, damage);
            case "light"    -> damageKeys.setDamageKey(item, DamageType.LIGHT, damage);
            case "dark"     -> damageKeys.setDamageKey(item, DamageType.DARK, damage);
            case "pure"     -> damageKeys.setDamageKey(item, DamageType.PURE, damage);
            default         -> { return true; } // silently ignore invalid types
        }

        player.getInventory().setItemInMainHand(item);
        return true;
    }
}
