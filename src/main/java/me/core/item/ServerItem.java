package me.core.item;

import me.core.util.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.minecraft.nbt.NBTBase;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class ServerItem extends ItemStack {

    public ServerItem(final Material material) {
        super(material);
        NBTHelper.addTag(this, "serverItem", true);
    }

    public ServerItem(final Material material, Component name) {
        this(material);
        ItemMeta meta = this.getItemMeta();
        assert meta != null;
        meta.displayName(name);
        this.setItemMeta(meta);
    }

    public ServerItem(final Material material, Component name, Component... lore) {
        this(material);
        ItemMeta meta = this.getItemMeta();
        assert meta != null;
        meta.displayName(name);
        meta.lore(new ArrayList<>(Arrays.asList(lore)));
        this.setItemMeta(meta);
    }

    public ServerItem addTag(String key, NBTBase value) {
        NBTHelper.addTag(this, key, value);
        return this;
    }

    public ServerItem addTag(String key, int value) {
        NBTHelper.addTag(this, key, value);
        return this;
    }

    public ServerItem addTag(String key, String value) {
        NBTHelper.addTag(this, key, value);
        return this;
    }

    public ServerItem addTag(String key, boolean value) {
        NBTHelper.addTag(this, key, value);
        return this;
    }

}
