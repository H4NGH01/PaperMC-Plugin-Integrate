package me.core.event;

import me.core.gui.MailWriterGUI;
import me.core.mail.NewMail;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.HashMap;

public class ServerChatBarListener implements Listener {

    public static final HashMap<Player, String> CHAT_MAP = new HashMap<>();

    @EventHandler
    public void onChat(PlayerChatEvent e) {
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

    private void editTitle(PlayerChatEvent e) {
        Player p = e.getPlayer();
        String s = e.getMessage();
        MailWriterGUI mwg = MailWriterGUI.VIEW_MAP.get(p);
        NewMail mail = mwg.getMail();
        mail.setTitle(s);
        p.sendMessage(Component.translatable("chat.mail_title_set").append(Component.text(s)));
        CHAT_MAP.remove(p);
        mwg.openToPlayer();
    }

    private void editText(PlayerChatEvent e) {
        Player p = e.getPlayer();
        String s = e.getMessage();
        MailWriterGUI mwg = MailWriterGUI.VIEW_MAP.get(p);
        NewMail mail = mwg.getMail();
        mail.setText(s);
        p.sendMessage(Component.translatable("chat.mail_title_set").append(Component.text("\n" + s.replaceAll("\\\\n", "\n"))));
        CHAT_MAP.remove(p);
        mwg.openToPlayer();
    }

}
