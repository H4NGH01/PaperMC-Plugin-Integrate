package me.core.item;

import me.core.util.ComponentUtil;
import me.core.util.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class StatTrak extends ItemStack {

    public StatTrak(Material material) {
        this(material, 1);
    }

    public StatTrak(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public StatTrak(ItemStack stack) throws IllegalArgumentException {
        super(stack);
        NBTHelper.setTag(this, "CustomName", this.getItemMeta().hasDisplayName() ? this.getItemMeta().getDisplayName() : this.translationKey());
        ItemMeta meta = this.getItemMeta();
        TextComponent.Builder display = Component.text().decoration(TextDecoration.ITALIC, false);
        display.append(Component.text(ChatColor.GOLD + "StatTrak™ "));
        Component c = this.getItemMeta().hasDisplayName() ? Component.text(this.getItemMeta().getDisplayName()) : Component.translatable(this.translationKey());
        display.append(c);
        meta.displayName(display.build());
        List<Component> components = new ArrayList<>();
        components.add(Component.translatable("item.stattrak.count").args(ComponentUtil.text(ChatColor.RED.toString() + getKills(this))));
        List<Component> base = meta.lore();
        components.addAll(base != null ? base : new ArrayList<>());
        meta.lore(components);
        this.setItemMeta(meta);
        NBTHelper.setTag(this, "stattrak", isStattrak(this) ? getKills(stack) : 0);
    }

    public static boolean isStattrak(ItemStack stack) {
        return NBTHelper.hasTag(stack, "stattrak");
    }

    public static int getKills(ItemStack stack) {
        return NBTHelper.getTag(stack).h("stattrak");
    }

    public static void addKills(ItemStack stack) {
        NBTHelper.setTag(stack, "stattrak", getKills(stack) + 1);
        update(stack);
    }

    public static void resetKills(ItemStack stack) {
        NBTHelper.setTag(stack, "stattrak", 0);
        update(stack);
    }

    private static void update(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        List<Component> components = new ArrayList<>();
        components.add(Component.translatable("item.stattrak.count").args(ComponentUtil.text(ChatColor.RED.toString() + getKills(stack))));
        List<Component> base = meta.lore();
        assert base != null;
        base.remove(0);
        components.addAll(base);
        meta.lore(components);
        stack.setItemMeta(meta);
    }
}
