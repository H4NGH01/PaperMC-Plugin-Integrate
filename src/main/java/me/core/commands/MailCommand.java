package me.core.commands;

import me.core.guis.mails.MailBoxGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MailCommand extends PluginCommand {

    @Override
    public void onCommand(Player player, String[] args) {
        final MailBoxGUI gui = new MailBoxGUI(player);
        gui.openToPlayer();
    }

    @Override
    public String name() {
        return "mail";
    }

    @Override
    public String info() {
        return "Open your mailbox";
    }

    @Override
    public String[] aliases() {
        return new String[]{"mailbox"};
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
