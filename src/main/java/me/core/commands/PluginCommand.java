package me.core.commands;

import me.core.MCServerPlugin;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Objects;

public abstract class PluginCommand implements Listener, TabCompleter {

    protected final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);

    public PluginCommand() {
        Objects.requireNonNull(plugin.getCommand(this.name())).setExecutor(plugin.getCommandManager());
        Objects.requireNonNull(plugin.getCommand(this.name())).setTabCompleter(this);
    }

    public abstract void onCommand(Player player, String[] args);

    public abstract String name();

    public abstract String info();

    public abstract String[] aliases();
}
