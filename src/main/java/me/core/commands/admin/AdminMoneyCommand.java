package me.core.commands.admin;

import me.core.ServerPlayer;
import me.core.commands.PluginCommand;
import me.core.economies.Economy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AdminMoneyCommand extends PluginCommand {

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.translatable("command.usages"));
            player.sendMessage("/admin-money query [Player]");
            player.sendMessage("/admin-money add [Player] [value]");
            player.sendMessage("/admin-money set [Player] [value]");
            return;
        }
        if (args[0].equalsIgnoreCase("query")) {
            Player target = plugin.getServer().getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(Component.translatable("argument.player.unknown").color(NamedTextColor.RED));
                return;
            }
            ServerPlayer sp = ServerPlayer.getServerPlayer(target);
            player.sendMessage(Component.translatable("command.money.query_player").args(target.displayName(), Component.text(sp.getMoney().toString())));
            return;
        }
        if (args.length > 2 && args[0].equalsIgnoreCase("add")) {
            Player target = plugin.getServer().getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(Component.translatable("argument.player.unknown").color(NamedTextColor.RED));
                return;
            }
            BigDecimal b;
            try {
                b = new BigDecimal(args[2]);
            } catch (Exception ignored) {
                player.sendMessage(Component.translatable("parsing.int.invalid").args(Component.text(args[2])).color(NamedTextColor.RED));
                return;
            }
            ServerPlayer sp = ServerPlayer.getServerPlayer(target);
            Economy.setMoney(player, sp.getMoney().add(b));
            player.sendMessage(Component.translatable("command.money.add_player").args(target.displayName(), Component.text(sp.getMoney().toString())));
            return;
        }
        if (args.length > 2 && args[0].equalsIgnoreCase("set")) {
            Player target = plugin.getServer().getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(Component.translatable("argument.player.unknown").color(NamedTextColor.RED));
                return;
            }
            BigDecimal b;
            try {
                b = new BigDecimal(args[2]);
            } catch (Exception ignored) {
                player.sendMessage(Component.translatable("parsing.int.invalid").args(Component.text(args[2])).color(NamedTextColor.RED));
                return;
            }
            ServerPlayer sp = ServerPlayer.getServerPlayer(target);
            Economy.setMoney(player, b);
            player.sendMessage(Component.translatable("command.money.set_player").args(target.displayName(), Component.text(sp.getMoney().toString())));
            return;
        }
        player.sendMessage(Component.translatable("command.usages"));
        player.sendMessage("/admin-money query [Player]");
        player.sendMessage("/admin-money add [Player] [value]");
        player.sendMessage("/admin-money set [Player] [value]");
    }

    @Override
    public String name() {
        return "admin-money";
    }

    @Override
    public String info() {
        return "Admin money command";
    }

    @Override
    public String[] aliases() {
        return new String[]{"a-money"};
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.add("query");
            list.add("add");
            list.add("set");
        }
        if (args.length == 2) {
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                list.add(p.getName());
            }
        }
        return list;
    }
}
