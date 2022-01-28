package me.core.event;

import me.core.gui.MailViewerGUI;
import me.core.item.MCServerItems;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ServerGUIListener implements Listener {

    private final MailGUIListener mge = new MailGUIListener();
    private static final String[] MAIL_GUIS_NAME = {"gui.mail.box", "gui.mail.sent", "gui.mail.writer", "gui.mail.bin", "gui.mail.viewer"};

    @EventHandler
    public void onClick(@NotNull InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        if (MCServerItems.isServerItem(item)) e.setCancelled(true);
        if (MCServerItems.equalItem(item, MCServerItems.close)) {
            Player p = (Player) e.getWhoClicked();
            p.closeInventory();
            p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 0.7f, 1f);
        }
        for (String s : MAIL_GUIS_NAME) {
            if (e.getView().getTitle().startsWith(s)) {
                mge.onClick(e);
                return;
            }
        }
    }

    @EventHandler
    public void onClose(@NotNull InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (!e.getView().getTitle().startsWith(MAIL_GUIS_NAME[4])) return;
        MailViewerGUI.VIEW_SET.remove(p);
    }

}
