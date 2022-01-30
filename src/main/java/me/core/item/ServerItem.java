package me.core.item;

import me.core.util.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.nbt.NBTBase;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ServerItem extends ItemStack {

    public ServerItem(final Material material) {
        this(material, 1);
    }

    public ServerItem(final Material material, int amount) {
        super(material, amount);
        NBTHelper.setTag(this, "serverItem", true);
    }

    public void setDisplayName(Component displayName) {
        ItemMeta meta = this.getItemMeta();
        meta.displayName(displayName.decoration(TextDecoration.ITALIC, false));
        this.setItemMeta(meta);
    }

    public void setLore(Component... components) {
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

    public ServerItem setTag(String key, NBTBase value) {
        NBTHelper.setTag(this, key, value);
        return this;
    }

    public ServerItem setTag(String key, int value) {
        NBTHelper.setTag(this, key, value);
        return this;
    }

    public ServerItem setTag(String key, String value) {
        NBTHelper.setTag(this, key, value);
        return this;
    }

    public ServerItem setTag(String key, boolean value) {
        NBTHelper.setTag(this, key, value);
        return this;
    }

}
