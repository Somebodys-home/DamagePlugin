package io.github.Gabriel.damagePlugin;

import io.github.Gabriel.damagePlugin.noDamage.NoDamageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class testcommand implements CommandExecutor {
    private NoDamageManager manager;

    public testcommand(DamagePlugin plugin) {
        manager = plugin.getDamageAttributesManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            ItemStack item = player.getInventory().getItemInMainHand();
            manager.removeAttributes(item);
        }
        return true;
    }
}
