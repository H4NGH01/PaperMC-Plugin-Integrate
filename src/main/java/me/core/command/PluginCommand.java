package me.core.command;

import org.bukkit.entity.Player;

public abstract class PluginCommand {

    public PluginCommand() {}

    public abstract void onCommand(Player player, String[] args);

    public abstract String name();

    public abstract String info();

    public abstract String[] aliases();
}
