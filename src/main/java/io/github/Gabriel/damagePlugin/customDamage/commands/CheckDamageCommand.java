package io.github.Gabriel.damagePlugin.customDamage.commands;

import io.github.Gabriel.damagePlugin.customDamage.DamageKey;
import io.github.Gabriel.damagePlugin.customDamage.DamageType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CheckDamageCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            ItemStack weapon = player.getInventory().getItemInMainHand();
            DamageKey damageKey = new DamageKey(weapon);

            if (weapon.hasItemMeta()) {
                double totalDamage = damageKey.getTotalDamageOfWeapon();
                player.sendMessage("§6Total Elemental Damage on Weapon:");
                player.sendMessage(String.valueOf(totalDamage));
            }
        }

        return true;
    }
}