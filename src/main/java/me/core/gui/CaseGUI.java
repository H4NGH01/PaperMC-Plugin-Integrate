package me.core.gui;

import me.core.cases.Case;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class CaseGUI extends GUIBase {

    private final Case caseIn;

    public CaseGUI(Player player, Case caseIn) {
        super(player);
        this.caseIn = caseIn;
    }

    @Override
    public void setInventory() {
        if (this.caseIn.getItems().length < 36) {
            for (int i = 0; i < this.caseIn.getItems().length; i++) {
                this.inventory.setItem(i + 9, this.caseIn.getItems()[i]);
            }
        }
    }

    @Override
    public Component getGUIName() {
        return Component.text("Weapon Case");
    }

    public void playOpenAnimation() {
        this.player.playSound(this.getPlayer().getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 0.7f, 1f);
    }
}
