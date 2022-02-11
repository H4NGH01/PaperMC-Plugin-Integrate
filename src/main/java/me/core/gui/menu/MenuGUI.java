package me.core.gui.menu;

import me.core.gui.GUIBase;
import me.core.items.InventoryItem;
import me.core.utils.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
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

    @Override
    public void openToPlayer() {
        super.openToPlayer();
        this.player.playSound(this.player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 0.7f, 1f);
    }

    private @NotNull InventoryItem icon() {
        InventoryItem item = new InventoryItem(Material.PLAYER_HEAD).setTag("ItemTag", "gui.menu.icon");
        item.setDisplayName(Component.translatable("gui.menu.info"));
        item.addLore(ComponentUtil.text(NamedTextColor.GRAY, "UUID: ", this.player.getUniqueId().toString()));
        item.addLore(ComponentUtil.translate(NamedTextColor.GRAY, "gui.menu.level").args(Component.text(this.player.getLevel()).color(NamedTextColor.YELLOW)));
        item.addLore(ComponentUtil.translate(NamedTextColor.GRAY, "gui.menu.exp").args(Component.text((int) (this.player.getExp() * this.player.getExpToLevel()) + "/" + this.player.getExpToLevel()).color(NamedTextColor.YELLOW)));
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
