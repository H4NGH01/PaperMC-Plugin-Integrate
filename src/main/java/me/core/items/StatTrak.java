package me.core.items;

import me.core.utils.ComponentUtil;
import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatTrak extends PluginItem {

    public StatTrak(Material material) {
        this(material, 1);
    }

    public StatTrak(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public <T extends ItemStack> StatTrak(T stack) {
        super(stack);
        addStatTrak(this);
    }

    @SuppressWarnings("deprecation")
    public static <T extends ItemStack> void addStatTrak(@NotNull T stack) {
        if (stack.hasItemMeta() && isStattrak(stack)) return;
        NBTHelper.setTag(stack, "CustomName", stack.hasItemMeta() && stack.getItemMeta().hasDisplayName() ? stack.getItemMeta().getDisplayName() : stack.translationKey());
        ItemMeta meta = stack.getItemMeta();
        TextComponent.Builder display = Component.text().decoration(TextDecoration.ITALIC, false);
        display.append(Component.text(ChatColor.GOLD + "StatTrak™ "));
        display.append(stack.hasItemMeta() && stack.getItemMeta().hasDisplayName() ? Objects.requireNonNull(stack.getItemMeta().displayName()) : Component.translatable(stack.translationKey()));
        meta.displayName(display.build());
        stack.setItemMeta(meta);
        List<Component> components = new ArrayList<>();
        components.add(ComponentUtil.component(NamedTextColor.RED, Component.translatable("item.stattrak.count").args(Component.text(getKills(stack)))));
        List<Component> base = meta.lore();
        if (base != null && base.size() != 0) components.addAll(base);
        stack.lore(components);
        NBTHelper.setTag(stack, "stattrak", getKills(stack));
    }

    public static <T extends ItemStack> void removeStatTrak(@NotNull T stack) {
        if (!stack.hasItemMeta() || !isStattrak(stack)) return;
        ItemMeta meta = stack.getItemMeta();
        boolean b = NBTHelper.getTag(stack).l("CustomName").equals(stack.translationKey());
        meta.displayName(b ? null : Component.text(NBTHelper.getTag(stack).l("CustomName")));
        List<Component> components = meta.lore();
        if (components != null) {
            for (int i = 0; i < components.size(); i++) {
                if (ComponentUtil.plainText(components.get(i)).equals("item.stattrak.count")) {
                    components.remove(i);
                    break;
                }
            }
            meta.lore(components);
        }
        stack.setItemMeta(meta);
        NBTHelper.removeTag(stack, "CustomName");
        NBTHelper.removeTag(stack, "stattrak");
        if ((components == null || components.size() == 0) && !meta.hasDisplayName())
            NBTHelper.removeTag(stack, "display");
    }

    public static <T extends ItemStack> boolean isStattrak(T stack) {
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
        List<Component> base = meta.lore();
        assert base != null;
        for (int i = 0; i < base.size(); i++) {
            if (ComponentUtil.plainText(base.get(i)).equals("item.stattrak.count")) {
                base.set(i, ComponentUtil.component(NamedTextColor.RED, Component.translatable("item.stattrak.count").args(Component.text(getKills(stack)))));
                break;
            }
        }
        meta.lore(base);
        stack.setItemMeta(meta);
    }
}
