package me.core.gui;

import me.core.MCServerPlugin;
import me.core.events.GUIOpenEvent;
import me.core.items.MCServerItems;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public abstract class GUIBase {

    protected final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);
    protected final Player player;
    protected final int size;
    protected Inventory inventory = null;
    protected GUIBase lastInventory;

    public GUIBase(@NotNull Player player, int size) {
        this.player = player;
        this.size = size;
    }

    public GUIBase(@NotNull Player player) {
        this(player, 54);
    }

    protected void setDefault() {
        this.inventory = this.inventory == null ? plugin.getServer().createInventory(null, this.size, this.getGUIName()) : this.inventory;
        for (int i = 0; i < 9; i++) {
            this.inventory.setItem(i, MCServerItems.board);
            this.inventory.setItem(i + this.size - 9, MCServerItems.board);
        }
        for (int i = 9; i < this.size - 9; i++) {
            this.inventory.setItem(i, MCServerItems.air);
        }
        this.inventory.setItem(8, MCServerItems.close);
    }

    public abstract void setInventory();

    public abstract Component getGUIName();

    public abstract <T extends GUIBase> HashMap<Player, T> getViewMap();

    public void openToPlayer() {
        this.player.closeInventory();
        this.setInventory();
        this.player.openInventory(this.inventory);
        this.getViewMap().put(this.player, this);
        Bukkit.getPluginManager().callEvent(new GUIOpenEvent(this.player, this));
    }

    public void setLastInventory(GUIBase lastInventory) {
        this.lastInventory = lastInventory;
        this.inventory.setItem(this.size - 5, this.lastInventory != null ? MCServerItems.back : MCServerItems.board);
    }

    public void openLastInventory() {
        if (this.lastInventory != null) {
            if (this.lastInventory instanceof MultiplePageGUI) ((MultiplePageGUI) this.lastInventory).setPage(1);
            this.lastInventory.openToPlayer();
        }
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public Player getPlayer() {
        return this.player;
    }
}
