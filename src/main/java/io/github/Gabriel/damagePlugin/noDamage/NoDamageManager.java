package io.github.Gabriel.damagePlugin;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

public class DamageAttributesManager {
    private DamagePlugin plugin;

    public DamageAttributesManager(DamagePlugin plugin) {
        this.plugin = plugin;
    }

    public void removeAttributes(ItemStack item, Player player) {
        if (item == null || item.getType().isAir()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        AttributeModifier damageModifier = new AttributeModifier(new NamespacedKey(plugin, "no_damage"), 0.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ANY);

        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);
        item.setItemMeta(meta);
    }
}