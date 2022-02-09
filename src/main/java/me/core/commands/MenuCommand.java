package me.core.commands;

import me.core.gui.menu.MenuGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MenuCommand extends PluginCommand {

    @Override
    public void onCommand(Player player, String[] args) {
        final MenuGUI gui = new MenuGUI(player);
        gui.openToPlayer();
    }

    @Override
    public String name() {
        return "menu";
    }

    @Override
    public String info() {
        return "Open menu";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
