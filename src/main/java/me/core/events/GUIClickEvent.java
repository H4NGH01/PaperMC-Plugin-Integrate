package me.core.events;

import me.core.gui.GUIBase;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GUIClickEvent extends InventoryClickEvent {

    private final InventoryClickEvent event;
    private final Player player;
    private final GUIBase gui;

    public GUIClickEvent(Player player, GUIBase gui, @NotNull InventoryClickEvent event) {
        super(event.getView(), event.getSlotType(), event.getSlot(), event.getClick(), event.getAction(), event.getHotbarButton());
        this.player = player;
        this.gui = gui;
        this.event = event;
    }

    public Player getPlayer() {
        return this.player;
    }

    public GUIBase getGUI() {
        return this.gui;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.event.setCancelled(cancel);
    }

    @Override
    public InventoryType.@NotNull SlotType getSlotType() {
        return this.event.getSlotType();
    }

    @Override
    public @Nullable ItemStack getCursor() {
        return this.event.getCursor();
    }

    @Override
    public @Nullable ItemStack getCurrentItem() {
        return this.event.getCurrentItem();
    }

    @Override
    public boolean isRightClick() {
        return this.event.isRightClick();
    }

    @Override
    public boolean isLeftClick() {
        return this.event.isLeftClick();
    }

    @Override
    public boolean isShiftClick() {
        return this.event.isShiftClick();
    }

    @Override
    public void setCurrentItem(@Nullable ItemStack stack) {
        this.event.setCurrentItem(stack);
    }

    @Override
    public @Nullable Inventory getClickedInventory() {
        return this.event.getClickedInventory();
    }

    @Override
    public int getSlot() {
        return this.event.getSlot();
    }

    @Override
    public int getRawSlot() {
        return this.event.getRawSlot();
    }

    @Override
    public int getHotbarButton() {
        return this.event.getHotbarButton();
    }

    @Override
    public @NotNull InventoryAction getAction() {
        return this.event.getAction();
    }

    @Override
    public @NotNull ClickType getClick() {
        return this.event.getClick();
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }
}
