package me.core.events;

import me.core.guis.GUIBase;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GUIOpenEvent extends Event {

    private final Player player;
    private final GUIBase gui;

    public GUIOpenEvent(Player player, GUIBase gui) {
        this.player = player;
        this.gui = gui;
        this.gui.getViewMap().put(player, gui);
    }

    public Player getPlayer() {
        return this.player;
    }

    public GUIBase getGUI() {
        return this.gui;
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
