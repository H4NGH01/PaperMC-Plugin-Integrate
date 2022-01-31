package me.core.cases;

import org.bukkit.inventory.ItemStack;


public abstract class Case {

    private final ItemStack item;

    public Case() {
        this.item = generate();
    }

    public abstract ItemStack generate();

    public abstract ItemStack[] getItems();

    public ItemStack getItem() {
        return item;
    }
}
