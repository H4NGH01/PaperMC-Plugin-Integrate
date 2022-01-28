package me.core.gui;

import me.core.MCServerPlugin;
import me.core.item.MCServerItems;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class GUIBase {

    protected final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);
    protected Inventory inventory;
    protected final Player player;

    public GUIBase(Player player) {
        this.player = player;
        this.inventory = plugin.getServer().createInventory(null, 54, Component.translatable(getGUIName()));
        for (int i = 0; i < 9; i++) {
            this.inventory.setItem(i, MCServerItems.board);
            this.inventory.setItem(i + 45, MCServerItems.board);
        }
        for (int i = 9; i < 45; i++) {
            this.inventory.setItem(i, MCServerItems.air);
        }
        this.inventory.setItem(49, MCServerItems.close);
    }

    public abstract void setInventory();

    public abstract String getGUIName();

    public void openToPlayer() {
        this.player.closeInventory();
        this.setInventory();
        this.player.openInventory(this.inventory);
        this.player.playSound(this.player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 0.7f, 1f);
    }

    public void setTitle(String translatable, String text) {
        ItemStack[] old = this.getContents();
        this.inventory = plugin.getServer().createInventory(null, this.inventory.getSize(), Component.translatable(translatable).append(Component.text(text)));
        this.inventory.setContents(old);
    }

    public ItemStack[] getContents() {
        return this.inventory.getContents();
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    protected Player getPlayer() {
        return this.player;
    }
}
