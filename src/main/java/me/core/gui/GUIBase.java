package me.core.gui;

import me.core.MCServerPlugin;
import me.core.item.MCServerItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class GUIBase {

    protected final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);
    protected final Player player;
    protected Inventory inventory;
    protected GUIBase lastInventory;

    public GUIBase(Player player) {
        this.player = player;
        this.inventory = plugin.getServer().createInventory(null, 54, getGUIName());
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

    public abstract Component getGUIName();

    public void openToPlayer() {
        this.player.closeInventory();
        this.setInventory();
        this.player.openInventory(this.inventory);
        this.player.playSound(this.player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 0.7f, 1f);
    }

    public void setLastInventory(GUIBase lastInventory) {
        this.lastInventory = lastInventory;
        this.inventory.setItem(48, this.lastInventory != null ? MCServerItems.back : MCServerItems.board);
    }

    public void openLastInventory() {
        if (this.lastInventory != null) {
            if (this.lastInventory instanceof MultiplePageGUI) ((MultiplePageGUI) this.lastInventory).setPage(1);
            this.lastInventory.openToPlayer();
        }
    }

    public void setTitle(Component... components) {
        ItemStack[] old = this.getContents();
        TextComponent.Builder builder = Component.text();
        for (Component component : components) {
            builder.append(component);
        }
        this.inventory = plugin.getServer().createInventory(null, this.inventory.getSize(), builder.build());
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
