package me.core.gui;

import me.core.containers.Container;
import me.core.items.InventoryItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ContainerGUI extends GUIBase {

    private static final HashMap<Player, ContainerGUI> VIEW_MAP = new HashMap<>();
    private final Container container;

    public ContainerGUI(Player player, Container container) {
        super(player);
        this.container = container;
        this.setDefault();
    }

    @Override
    public void setInventory() {
        this.inventory.setItem(0, requireKey());
        for (int i = 0; i < this.container.getDisplayDrops().size(); i++) {
            if (i >= 36) break;
            this.inventory.setItem(i + 9, this.container.getDisplayDrops().get(i));
        }
    }

    @Override
    public Component getGUIName() {
        return Component.translatable(container.getContainerType().getTranslationKey());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends GUIBase> HashMap<Player, T> getViewMap() {
        return (HashMap<Player, T>) VIEW_MAP;
    }

    public void openContainer() {
        this.player.getInventory();
        this.playOpenAnimation();
    }

    private void playOpenAnimation() {
        this.player.playSound(this.getPlayer().getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 0.7f, 1f);

    }

    private InventoryItem requireKey() {
        InventoryItem item = new InventoryItem(Material.TRIPWIRE_HOOK);
        item.setDisplayName(Component.translatable("gui.container.require_key").args(Component.translatable(container.getKeyType().getTranslationKey())));
        return item;
    }
}
