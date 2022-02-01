package me.core.gui;

import me.core.cases.Case;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class CaseGUI extends GUIBase {

    private final Case caseIn;

    public CaseGUI(Player player, Case caseIn) {
        super(player);
        this.caseIn = caseIn;
    }

    @Override
    public void setInventory() {
        for (int i = 0; i < this.caseIn.getItems().length; i++) {
            this.inventory.setItem(i + 9, this.caseIn.getItems()[i]);
        }
    }

    @Override
    public Component getGUIName() {
        return Component.text("Weapon Case");
    }
}
