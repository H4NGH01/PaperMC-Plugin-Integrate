package me.core.items;

import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.nbt.NBTBase;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PluginItem extends ItemStack {

    public PluginItem(final Material material) {
        this(material, 1);
    }

    public PluginItem(final Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public PluginItem(ItemStack stack) {
        super(stack);
    }

    public Component getDisplayName() {
        if (this.hasItemMeta() && this.getItemMeta().hasDisplayName()) return super.getItemMeta().displayName();
        return Component.translatable(this.translationKey());
    }

    public void setDisplayName(@NotNull Component displayName) {
        ItemMeta meta = this.getItemMeta();
        meta.displayName(displayName.decoration(TextDecoration.ITALIC, false));
        this.setItemMeta(meta);
    }

    public void setLore(Component @NotNull ... components) {
        List<Component> lore = new ArrayList<>();
        for (Component component : components) {
            lore.add(component.decoration(TextDecoration.ITALIC, false));
        }
        this.lore(lore);
    }

    public void addLore(Component component) {
        List<Component> lore = this.lore() != null ? this.lore() : new ArrayList<>();
        assert lore != null;
        lore.add(component);
        this.lore(lore);
    }

    public PluginItem setTag(String key, NBTBase value) {
        NBTHelper.setTag(this, key, value);
        return this;
    }

    public PluginItem setTag(String key, int value) {
        NBTHelper.setTag(this, key, value);
        return this;
    }

    public PluginItem setTag(String key, String value) {
        NBTHelper.setTag(this, key, value);
        return this;
    }

    public PluginItem setTag(String key, boolean value) {
        NBTHelper.setTag(this, key, value);
        return this;
    }

    public void setPlaceable(boolean placeable) {
        NBTHelper.setTag(this, "placeable", placeable);
    }

}
