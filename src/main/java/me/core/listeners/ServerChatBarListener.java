package me.core.listeners;

import me.core.ServerPlayer;
import me.core.gui.mail.MailBoxGUI;
import me.core.gui.mail.MailWriterGUI;
import me.core.gui.market.MarketMultiBuyGUI;
import me.core.mail.MailManager;
import me.core.mail.NewMail;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
            return;
        }
        if (CHAT_MAP.get(p).equals("chat.market.buy_custom_count")) {
            this.editCount(e);
        }
    }

    private void editTitle(@NotNull PlayerChatEvent e) {
        Player p = e.getPlayer();
        String s = e.getMessage();
        NewMail mail = MailManager.getNewMailMap().get(p.getUniqueId());
        MailWriterGUI mwg = new MailWriterGUI(p, mail);
        mwg.setLastInventory(new MailBoxGUI(p));
        mail.setTitle(s);
        p.sendMessage(Component.translatable("chat.mail.title_set").args(Component.text(s)));
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
        p.sendMessage(Component.translatable("chat.mail.text_set").args(Component.text("\n" + s.replaceAll("\\\\n", "\n"))));
        CHAT_MAP.remove(p);
        mwg.openToPlayer();
    }

    private void editCount(@NotNull PlayerChatEvent e) {
        Player p = e.getPlayer();
        String s = e.getMessage();
        MarketMultiBuyGUI gui = (MarketMultiBuyGUI) ServerPlayer.getServerPlayer(p).getHoldingGUI();
        int i;
        try {
            i = Integer.parseInt(s);
        } catch (Exception ignored) {
            p.sendMessage(Component.translatable("parsing.int.invalid").args(Component.text(s)).color(NamedTextColor.RED));
            return;
        }
        gui.setCustom(i);
        p.sendMessage(Component.translatable("chat.market.count_set").args(Component.text(s)));
        CHAT_MAP.remove(p);
        gui.openToPlayer();
        ServerPlayer.getServerPlayer(p).setHoldingGUI(null);
    }

}
