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

public class MoneyCommand extends PluginCommand {

    @Override
    public void onCommand(@NotNull Player player, String[] args) {
        player.sendMessage(Component.translatable("command.money.query").args(Component.text(ServerPlayer.getServerPlayer(player).getMoney().toString())));
    }

    @Override
    public String name() {
        return "money";
    }

    @Override
    public String info() {
        return "Query money";
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
