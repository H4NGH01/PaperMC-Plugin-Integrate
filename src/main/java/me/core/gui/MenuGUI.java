package me.core.gui;

import me.core.items.InventoryItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class MenuGUI extends GUIBase {

    private static final HashMap<Player, MenuGUI> VIEW_MAP = new HashMap<>();

    public MenuGUI(@NotNull Player player) {
        super(player);
        setDefault();
    }

    @Override
    public void setInventory() {
        this.inventory.setItem(18, mail());
    }

    @Override
    public Component getGUIName() {
        return Component.translatable("gui.menu");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends GUIBase> HashMap<Player, T> getViewMap() {
        return (HashMap<Player, T>) VIEW_MAP;
    }

    public static HashMap<Player, MenuGUI> getViews() {
        return VIEW_MAP;
    }

    private @NotNull InventoryItem mail() {
        InventoryItem item = new InventoryItem(Material.PAPER);
        item.setDisplayName(Component.translatable("gui.menu.mail"));
        item.setLore(Component.translatable("gui.menu.mail_lore"));
        return item;
    }
}
