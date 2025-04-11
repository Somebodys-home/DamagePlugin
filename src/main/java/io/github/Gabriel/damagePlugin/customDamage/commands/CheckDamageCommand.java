package io.github.Gabriel.damagePlugin.customDamage.commands;

import io.github.Gabriel.damagePlugin.DamagePlugin;
import io.github.Gabriel.damagePlugin.customDamage.DamageKeys;
import io.github.Gabriel.damagePlugin.customDamage.DamageType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CheckDamageCommand implements CommandExecutor {

    private final DamageKeys damageKeys;

    public CheckDamageCommand(DamagePlugin plugin) {
        this.damageKeys = plugin.getDamageKeys();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta()) return true;

        boolean found = false;
        player.sendMessage("§6Elemental Damage on Held Item:");

        for (DamageType type : DamageType.values()) {
            if (damageKeys.checkForDamageKey(item, type)) {
                double value = damageKeys.getDamageKeyValue(item, type);
                player.sendMessage("§7- §e" + type.name() + ": §f" + value);
                found = true;
            }
        }

        if (!found) {
            player.sendMessage("§7No elemental damage found.");
        }

        return true;
    }
}
