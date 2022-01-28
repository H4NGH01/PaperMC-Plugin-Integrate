package me.core.item;

import me.core.util.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.minecraft.nbt.NBTBase;
import org.bukkit.Material;

public class InventoryItem extends ServerItem {

    public InventoryItem(final Material material) {
        super(material);
    }

    public InventoryItem(final Material material, Component name) {
        super(material, name);
    }

    public InventoryItem(final Material material, Component name, Component... lore) {
        super(material, name, lore);
    }

    public InventoryItem addTag(String key, NBTBase value) {
        NBTHelper.addTag(this, key, value);
        return this;
    }

    public InventoryItem addTag(String key, int value) {
        NBTHelper.addTag(this, key, value);
        return this;
    }

    public InventoryItem addTag(String key, String value) {
        NBTHelper.addTag(this, key, value);
        return this;
    }

    public InventoryItem addTag(String key, boolean value) {
        NBTHelper.addTag(this, key, value);
        return this;
    }

}
