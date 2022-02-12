package me.core.commands;

import me.core.gui.market.ContainerMarketGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MarketCommand extends PluginCommand {

    @Override
    public void onCommand(Player player, String[] args) {
        final ContainerMarketGUI gui = new ContainerMarketGUI(player);
        gui.openToPlayer();
    }

    @Override
    public String name() {
        return "market";
    }

    @Override
    public String info() {
        return "Open market GUI";
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
