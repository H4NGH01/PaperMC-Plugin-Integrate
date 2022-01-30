package me.core.command;

import me.core.MCServerPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandManager implements CommandExecutor {

    private final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);
    private final List<PluginCommand> commands = new ArrayList<>();

    public void setup() {
        this.addCommand(new MailCommand());
        this.addCommand(new AdminMailCommand());
        for (PluginCommand command : commands) {
            Objects.requireNonNull(plugin.getCommand(command.name())).setExecutor(this);
        }
    }

    public void addCommand(PluginCommand command) {
        this.commands.add(command);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }
        Player player = (Player) sender;
        for (PluginCommand pluginCommand : commands) {
            if (command.getName().equalsIgnoreCase(pluginCommand.name())) {
                pluginCommand.onCommand(player, args);
                return true;
            }
        }
        return false;
    }
}
