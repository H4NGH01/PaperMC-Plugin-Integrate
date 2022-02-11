package me.core.gui;

import me.core.containers.ContainerKey;
import me.core.containers.ContainerManager;
import me.core.containers.ContainerType;
import me.core.items.CaseKeyStack;
import me.core.items.DisplayCaseStack;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContainerMarketGUI extends MultiplePageGUI {

    private static final HashMap<Player, ContainerMarketGUI> VIEW_MAP = new HashMap<>();

    public ContainerMarketGUI(@NotNull Player player) {
        super(player);
        this.setDefault();
    }

    @Override
    public void setInventory() {
        List<ItemStack> stacks = new ArrayList<>();
        for (ContainerType type : ContainerType.values()) {
            stacks.add(new DisplayCaseStack(ContainerManager.getContainerByType(type)));
        }
        for (ContainerKey key : ContainerKey.values()) {
            stacks.add(new CaseKeyStack(key));
        }
        this.setContents(stacks);
        this.toArray(VIEW_MAP.containsKey(this.getPlayer()) ? VIEW_MAP.get(this.player).getPage() : 1);
    }

    @Override
    public Component getGUIName() {
        return Component.translatable("gui.container_market");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends GUIBase> HashMap<Player, T> getViewMap() {
        return (HashMap<Player, T>) VIEW_MAP;
    }

    public static HashMap<Player, ContainerMarketGUI> getViews() {
        return VIEW_MAP;
    }
}
