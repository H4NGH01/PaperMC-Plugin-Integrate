package me.core.guis;

import me.core.cases.Case;
import me.core.items.InventoryItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CaseGUI extends GUIBase {

    private static final HashMap<Player, CaseGUI> VIEW_MAP = new HashMap<>();
    private final Case caseIn;

    public CaseGUI(Player player, Case caseIn) {
        super(player);
        this.caseIn = caseIn;
        this.setDefault();
    }

    @Override
    public void setInventory() {
        this.inventory.setItem(0, requireKey());
        for (int i = 0; i < this.caseIn.getDisplayDrops().size(); i++) {
            if (i >= 36) break;
            this.inventory.setItem(i + 9, this.caseIn.getDisplayDrops().get(i));
        }
    }

    @Override
    public Component getGUIName() {
        return Component.translatable(caseIn.getCaseType().getTranslationKey());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends GUIBase> HashMap<Player, T> getViewMap() {
        return (HashMap<Player, T>) VIEW_MAP;
    }

    public void openCase() {
        this.player.getInventory();
        this.playOpenAnimation();
    }

    private void playOpenAnimation() {
        this.player.playSound(this.getPlayer().getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 0.7f, 1f);

    }

    private InventoryItem requireKey() {
        InventoryItem item = new InventoryItem(Material.TRIPWIRE_HOOK);
        item.setDisplayName(Component.translatable("gui.case.require_key").args(Component.translatable(caseIn.getCaseKey().getTranslationKey())));
        return item;
    }
}
