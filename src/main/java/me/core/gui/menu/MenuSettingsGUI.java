package me.core.gui.menu;

import me.core.PlayerSettings;
import me.core.ServerPlayer;
import me.core.gui.GUIBase;
import me.core.items.InventoryItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
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
        this.inventory.setItem(10, mailMessage());
        this.inventory.setItem(19, toggle(ServerPlayer.getServerPlayer(this.player).getSettings().get(PlayerSettings.NEW_MAIL_MESSAGE)));
        this.inventory.setItem(12, containerAnimation());
        this.inventory.setItem(21, toggle(ServerPlayer.getServerPlayer(this.player).getSettings().get(PlayerSettings.CONTAINER_ANIMATION)));
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

    @Override
    public void openToPlayer() {
        super.openToPlayer();
        this.player.playSound(this.player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 0.7f, 1f);
    }

    private @NotNull InventoryItem mailMessage() {
        InventoryItem item = new InventoryItem(Material.BELL).setTag("ItemTag", "gui.settings.new_mail_message");
        item.setDisplayName(Component.translatable("gui.settings.new_mail_message"));
        item.setLore(Component.translatable("gui.settings.new_mail_message_lore"));
        return item;
    }

    private @NotNull InventoryItem containerAnimation() {
        InventoryItem item = new InventoryItem(Material.CHEST).setTag("ItemTag", "gui.settings.container_animation");
        item.setDisplayName(Component.translatable("gui.settings.container_animation"));
        item.setLore(Component.translatable("gui.settings.container_animation_lore"));
        return item;
    }

    private @NotNull InventoryItem toggle(boolean b) {
        InventoryItem item = new InventoryItem(b ? Material.LIME_DYE : Material.GRAY_DYE).setTag("ItemTag", "gui.settings.toggle");
        item.setDisplayName(Component.translatable("gui.settings.status").args(Component.translatable(b ? "gui.enable" : "gui.disable")));
        item.setLore(Component.translatable("gui.toggle"));
        return item;
    }
}
