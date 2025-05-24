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
            ItemStack item = player.getInventory().getItemInMainHand();
            DamageKey damageKey = new DamageKey(item);

            if (item.hasItemMeta()) {
                boolean found = false;
                player.sendMessage("§6Elemental Damage on Held Item:");

                for (DamageType type : DamageType.values()) {
                    if (damageKey.checkForDamageType(type)) {
                        player.sendMessage(DamageType.getDamageColor(type) + "+" + damageKey.getDamageValue(type) + " " + DamageType.getDamageString(type) + " Damage");
                        found = true;
                    }
                }

                if (!found) {
                    player.sendMessage("§7No elemental damage found.");
                }
            }
        }

        return true;
    }
}