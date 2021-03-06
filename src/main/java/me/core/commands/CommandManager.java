package me.core.commands;

import me.core.commands.admin.AdminContainerCommand;
import me.core.commands.admin.AdminMailCommand;
import me.core.commands.admin.AdminEconomyCommand;
import me.core.commands.admin.AdminStatTrakCommand;
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
        this.addCommand(new MenuCommand());
        this.addCommand(new BalanceCommand());
        this.addCommand(new MarketCommand());
        this.addCommand(new MailCommand());
        this.addCommand(new AdminEconomyCommand());
        this.addCommand(new AdminMailCommand());
        this.addCommand(new AdminContainerCommand());
        this.addCommand(new AdminStatTrakCommand());
    }

    public void addCommand(PluginCommand command) {
        this.commands.add(command);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }
        for (PluginCommand pluginCommand : commands) {
            if (command.getName().equalsIgnoreCase(pluginCommand.name())) {
                pluginCommand.onCommand(player, args);
                return true;
            }
        }
        return false;
    }
}
