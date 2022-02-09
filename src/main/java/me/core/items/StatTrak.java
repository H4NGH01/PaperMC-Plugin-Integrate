package me.core.items;

import me.core.utils.ComponentUtil;
import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatTrak extends PluginItem {

    private static final String WITH_DELIMITER = "(?=%1$s)";

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
        String s = stack.hasItemMeta() && stack.getItemMeta().hasDisplayName() ? stack.getItemMeta().getDisplayName() : "\\t" + stack.translationKey();
        ItemMeta meta = stack.getItemMeta();
        TextComponent.Builder display = Component.text().decoration(TextDecoration.ITALIC, false);
        display.append(Component.text("§6StatTrak™ "));
        display.append(stack.hasItemMeta() && stack.getItemMeta().hasDisplayName() ? Objects.requireNonNull(stack.getItemMeta().displayName()) : Component.translatable(stack.translationKey()));
        meta.displayName(display.build());
        stack.setItemMeta(meta);
        List<Component> components = new ArrayList<>();
        components.add(ComponentUtil.component(NamedTextColor.RED, Component.translatable("item.stattrak.count").args(Component.text(getKills(stack)))));
        List<Component> base = meta.lore();
        if (base != null && base.size() != 0) components.addAll(base);
        stack.lore(components);
        NBTHelper.setTag(stack, "stattrak", getKills(stack));
        setCustomName(stack, s);
    }

    public static <T extends ItemStack> void removeStatTrak(@NotNull T stack) {
        if (!stack.hasItemMeta() || !isStattrak(stack)) return;
        ItemMeta meta = stack.getItemMeta();
        boolean b = NBTHelper.getTag(stack).l("CustomName").equals("\\t" + stack.translationKey());
        meta.displayName(b ? null : getCustomName(stack).build());
        List<Component> lore = meta.lore();
        if (lore != null) {
            for (int i = 0; i < lore.size(); i++) {
                if (ComponentUtil.plainText(lore.get(i)).equals("item.stattrak.count")) {
                    lore.remove(i);
                    break;
                }
            }
            meta.lore(lore);
        }
        stack.setItemMeta(meta);
        /*a
        NBTTagCompound tag = NBTHelper.getTag(stack).p("plugin");
        tag.r("CustomName");
        NBTHelper.setTag(stack, "plugin", tag);*/
        NBTHelper.removeTag(stack, "CustomName");
        NBTHelper.removeTag(stack, "stattrak");
        if ((lore == null || lore.size() == 0) && !meta.hasDisplayName())
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

    private static void update(@NotNull ItemStack stack) {
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

    public static @NotNull TextComponent.Builder getCustomName(TextComponent.@NotNull Builder builder, ItemStack stack) {
        builder.decoration(TextDecoration.ITALIC, false);
        final String name = NBTHelper.getTag(stack).l("CustomName");
        if (name.equals("\\t" + stack.translationKey())) {
            return builder.append(ComponentUtil.translate(stack.translationKey()));
        } else {
            String[] ss = name.split(" ");
            for (int i = 0; i < ss.length; i++) {
                String s = ss[i];
                if (s.startsWith("\\t")) {
                    builder.append(Component.translatable(s.substring(2)));
                } else {
                    builder.append(Component.text(s.replaceAll("\\\\s", "\\\\")));
                }
                if (i + 1 < ss.length) builder.append(Component.text(" "));
            }
        }
        return builder;
    }

    public static @NotNull TextComponent.Builder getCustomName(ItemStack stack) {
        TextComponent.Builder builder = Component.text();
        return getCustomName(builder, stack);
    }

    public static void setCustomName(ItemStack stack, String s) {
        NBTHelper.setTag(stack, "CustomName", s);
    }
}
