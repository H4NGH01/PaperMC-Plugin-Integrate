package me.core.command;

import me.core.gui.mail.MailBoxGUI;
import org.bukkit.entity.Player;

public class MailCommand extends PluginCommand {

    private final String[] s = new String[]{"mailbox"};

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
        return s;
    }
}
