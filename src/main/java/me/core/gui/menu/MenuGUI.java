package me.core.gui.menu;

import me.core.gui.GUIBase;
import me.core.items.InventoryItem;
import me.core.utils.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class MenuGUI extends GUIBase implements MenuGUIInterface {

    private static final HashMap<Player, MenuGUI> VIEW_MAP = new HashMap<>();

    public MenuGUI(@NotNull Player player) {
        super(player);
        setDefault();
    }

    @Override
    public void setInventory() {
        this.inventory.setItem(9, icon());
        this.inventory.setItem(18, mail());
        this.inventory.setItem(44, settings());
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

    private @NotNull InventoryItem icon() {
        InventoryItem item = new InventoryItem(Material.PLAYER_HEAD).setTag("ItemTag", "gui.menu.icon");
        item.setDisplayName(Component.text("君の名は: ").append(this.player.displayName()));
        item.setLore(ComponentUtil.text(NamedTextColor.GRAY, "UUID: ", this.player.getUniqueId().toString()));
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(this.player);
        item.setItemMeta(meta);
        return item;
    }

    private @NotNull InventoryItem mail() {
        InventoryItem item = new InventoryItem(Material.BEEHIVE).setTag("ItemTag", "gui.menu.mail");
        item.setDisplayName(Component.translatable("gui.menu.mail"));
        item.setLore(Component.translatable("gui.menu.mail_lore"));
        return item;
    }

    private @NotNull InventoryItem settings() {
        InventoryItem item = new InventoryItem(Material.COMPARATOR).setTag("ItemTag", "gui.menu.settings");
        item.setDisplayName(Component.translatable("gui.menu.settings"));
        item.setLore(Component.translatable("gui.menu.settings_lore"));
        return item;
    }
}
