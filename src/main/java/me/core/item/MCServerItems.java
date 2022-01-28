package me.core.item;

import me.core.util.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MCServerItems {

    public final static InventoryItem air = new InventoryItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, Component.text(" ")).addTag("ItemTag", "gui.air");
    public final static InventoryItem board = new InventoryItem(Material.GRAY_STAINED_GLASS_PANE, Component.text(" ")).addTag("ItemTag", "gui.board");
    public final static InventoryItem next = new InventoryItem(Material.ARROW, Component.translatable("gui.next")).addTag("ItemTag", "gui.next");
    public final static InventoryItem prev = new InventoryItem(Material.ARROW, Component.translatable("gui.prev")).addTag("ItemTag", "gui.prev");
    public final static InventoryItem back = new InventoryItem(Material.ARROW, Component.translatable("gui.back")).addTag("ItemTag", "gui.back");
    public final static InventoryItem close = new InventoryItem(Material.BARRIER, Component.translatable("gui.close")).addTag("ItemTag", "gui.close");

    public static boolean isServerItem(ItemStack stack) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound compound = (nmsStack.r()) ? nmsStack.s() : new NBTTagCompound();
        assert compound != null;
        return compound.p("serverItem") != null && compound.q("serverItem");
    }

    public static boolean equalItem(@NotNull ItemStack source, @NotNull ItemStack stack) {
        if (source.getItemMeta() == null || stack.getItemMeta() == null) return Objects.equals(source.getType(), source.getType()) && source.getAmount() == stack.getAmount();
        return source.getItemMeta().equals(stack.getItemMeta()) && Objects.equals(source.getType(), stack.getType()) && source.getAmount() == stack.getAmount();
    }

    public static boolean equalWithTag(ItemStack stack, String key, String value) {
        return NBTHelper.hasTag(stack) && NBTHelper.getTag(stack).l(key).equals(value);
    }

    public static boolean equalWithTag(ItemStack source, ItemStack stack, String key) {
        return NBTHelper.hasTag(source) && NBTHelper.hasTag(stack) && NBTHelper.getTag(source).l(key).equals(NBTHelper.getTag(stack).l(key));
    }
}
