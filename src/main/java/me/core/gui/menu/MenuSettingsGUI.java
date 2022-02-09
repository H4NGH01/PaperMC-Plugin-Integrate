package me.core.gui.menu;

import me.core.PlayerSettings;
import me.core.ServerPlayer;
import me.core.gui.GUIBase;
import me.core.items.InventoryItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class MenuSettingsGUI extends GUIBase implements MenuGUIInterface {

    private static final HashMap<Player, MenuSettingsGUI> VIEW_MAP = new HashMap<>();

    public MenuSettingsGUI(@NotNull Player player) {
        super(player);
        setDefault();
    }

    @Override
    public void setInventory() {
        this.inventory.setItem(10, mailHint());
        this.inventory.setItem(19, toggle(ServerPlayer.getServerPlayer(this.player).getSettings().get(PlayerSettings.NEW_MAIL_HINT)));
    }

    @Override
    public Component getGUIName() {
        return Component.translatable("gui.settings");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends GUIBase> HashMap<Player, T> getViewMap() {
        return (HashMap<Player, T>) VIEW_MAP;
    }

    public static HashMap<Player, MenuSettingsGUI> getViews() {
        return VIEW_MAP;
    }

    private @NotNull InventoryItem mailHint() {
        InventoryItem item = new InventoryItem(Material.PAPER).setTag("ItemTag", "gui.settings.mail_hint");
        item.setDisplayName(Component.translatable("gui.settings.mail_hint"));
        item.setLore(Component.translatable("gui.settings.mail_hint_lore"));
        return item;
    }

    private @NotNull InventoryItem toggle(boolean b) {
        InventoryItem item = new InventoryItem(b ? Material.LIME_DYE : Material.GRAY_DYE).setTag("ItemTag", "gui.settings.toggle.new_mail_hint");
        item.setDisplayName(Component.translatable("gui.settings.status").args(Component.translatable(b ? "gui.enable" : "gui.disable")));
        item.setLore(Component.translatable("gui.toggle"));
        return item;
    }
}
