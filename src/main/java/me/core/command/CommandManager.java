package me.core.command;

import me.core.command.admin.AdminMailCommand;
import me.core.command.admin.AdminStatTrakCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor {

    private final List<PluginCommand> commands = new ArrayList<>();

    public void setup() {
        this.addCommand(new MailCommand());
        this.addCommand(new AdminMailCommand());
        this.addCommand(new AdminStatTrakCommand());
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
