package me.core.items;

import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MCServerItems {

    public final static InventoryItem air = inventoryItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, Component.text(" ")).setTag("ItemTag", "gui.air");
    public final static InventoryItem board = inventoryItem(Material.GRAY_STAINED_GLASS_PANE, Component.text(" ")).setTag("ItemTag", "gui.board");
    public final static InventoryItem next = inventoryItem(Material.ARROW, Component.translatable("gui.next")).setTag("ItemTag", "gui.next");
    public final static InventoryItem prev = inventoryItem(Material.ARROW, Component.translatable("gui.prev")).setTag("ItemTag", "gui.prev");
    public final static InventoryItem back = inventoryItem(Material.ARROW, Component.translatable("gui.back")).setTag("ItemTag", "gui.back");
    public final static InventoryItem close = inventoryItem(Material.BARRIER, Component.translatable("gui.close")).setTag("ItemTag", "gui.close");

    public static boolean isInventoryItem(ItemStack stack) {
        return NBTHelper.hasTag(stack, "InventoryItem") && NBTHelper.getTag(stack).q("InventoryItem");
    }

    public static boolean equalWithTag(ItemStack stack, String key, String value) {
        return NBTHelper.hasTag(stack) && NBTHelper.getTag(stack).l(key).equals(value);
    }

    public static boolean equalWithKey(ItemStack source, ItemStack stack, String key) {
        return NBTHelper.hasTag(source) && NBTHelper.hasTag(stack) && NBTHelper.getTag(source).l(key).equals(NBTHelper.getTag(stack).l(key));
    }

    private static InventoryItem inventoryItem(Material material, Component displayName) {
        InventoryItem item = new InventoryItem(material);
        item.setDisplayName(displayName);
        return item;
    }
}
