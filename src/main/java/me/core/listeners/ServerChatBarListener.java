package me.core.listeners;

import me.core.gui.mail.MailBoxGUI;
import me.core.gui.mail.MailWriterGUI;
import me.core.mail.MailManager;
import me.core.mail.NewMail;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@SuppressWarnings("deprecation")
public class ServerChatBarListener implements Listener {

    public static final HashMap<Player, String> CHAT_MAP = new HashMap<>();

    @EventHandler
    public void onChat(@NotNull PlayerChatEvent e) {
        Player p = e.getPlayer();
        e.setMessage(e.getMessage().replace('&', '§'));
        if (!CHAT_MAP.containsKey(p)) return;
        e.setCancelled(true);
        if (CHAT_MAP.get(p).equals("chat.mail.edit.title")) {
            this.editTitle(e);
            return;
        }
        if (CHAT_MAP.get(p).equals("chat.mail.edit.text")) {
            this.editText(e);
        }
    }

    private void editTitle(@NotNull PlayerChatEvent e) {
        Player p = e.getPlayer();
        String s = e.getMessage();
        NewMail mail = MailManager.getNewMailMap().get(p.getUniqueId());
        MailWriterGUI mwg = new MailWriterGUI(p, mail);
        mwg.setLastInventory(new MailBoxGUI(p));
        mail.setTitle(s);
        p.sendMessage(Component.translatable("chat.mail_title_set").args(Component.text(s)));
        CHAT_MAP.remove(p);
        mwg.openToPlayer();
    }

    private void editText(@NotNull PlayerChatEvent e) {
        Player p = e.getPlayer();
        String s = e.getMessage();
        NewMail mail = MailManager.getNewMailMap().get(p.getUniqueId());
        MailWriterGUI mwg = new MailWriterGUI(p, mail);
        mwg.setLastInventory(new MailBoxGUI(p));
        mail.setText(s);
        p.sendMessage(Component.translatable("chat.mail_text_set").args(Component.text("\n" + s.replaceAll("\\\\n", "\n"))));
        CHAT_MAP.remove(p);
        mwg.openToPlayer();
    }

}
