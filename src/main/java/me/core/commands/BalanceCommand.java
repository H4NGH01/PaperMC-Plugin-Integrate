package me.core.commands;

import me.core.ServerPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BalanceCommand extends PluginCommand {

    @Override
    public void onCommand(@NotNull Player player, String[] args) {
        player.sendMessage(Component.translatable("command.balance.query").args(Component.text("$" +ServerPlayer.getServerPlayer(player).getMoney())));
    }

    @Override
    public String name() {
        return "balance";
    }

    @Override
    public String info() {
        return "Query money";
    }

    @Override
    public String[] aliases() {
        return new String[]{"bal", "money"};
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
