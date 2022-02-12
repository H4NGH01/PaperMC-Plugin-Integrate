package me.core.listeners;

import me.core.events.*;
import me.core.gui.GUIBase;
import me.core.gui.MultiplePageGUI;
import me.core.gui.mail.MailGUIInterface;
import me.core.gui.mail.MailViewerGUI;
import me.core.gui.market.MarketGUIInterface;
import me.core.gui.menu.MenuGUIInterface;
import me.core.items.MCServerItems;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ServerGUIListener implements Listener {

    private static final HashMap<Player, GUIBase> OPENED_GUI = new HashMap<>();
    private final MenuGUIListener menuGUIListener = new MenuGUIListener();
    private final MarketGUIListener marketGUIListener = new MarketGUIListener();
    private final MailGUIListener mailGUIListener = new MailGUIListener();

    public static HashMap<Player, GUIBase> getOpenedGUI() {
        return OPENED_GUI;
    }

    @EventHandler
    public void onClick(@NotNull InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (OPENED_GUI.containsKey(p)) Bukkit.getPluginManager().callEvent(new GUIClickEvent(p, OPENED_GUI.get(p), e));
    }

    @EventHandler
    public void onClose(@NotNull InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (OPENED_GUI.containsKey(p)) Bukkit.getPluginManager().callEvent(new GUICloseEvent(p, OPENED_GUI.get(p)));
    }

    @EventHandler
    public void onDeath(@NotNull PlayerDeathEvent e) {
        if (e.isCancelled()) return;
        Player p = e.getPlayer();
        if (OPENED_GUI.containsKey(p)) Bukkit.getPluginManager().callEvent(new GUICloseEvent(p, OPENED_GUI.get(p)));
    }

    @EventHandler
    public void onQuit(@NotNull PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (OPENED_GUI.containsKey(p)) Bukkit.getPluginManager().callEvent(new GUICloseEvent(p, OPENED_GUI.get(p)));
    }

    @EventHandler
    public void onOpen(@NotNull GUIOpenEvent e) {
        OPENED_GUI.put(e.getPlayer(), e.getGUI());
    }

    @EventHandler
    public void onClose(@NotNull GUICloseEvent e) {
        Player p = e.getPlayer();
        OPENED_GUI.remove(p);
        if (e.getGUI() instanceof MailViewerGUI) MailViewerGUI.getViews().remove(p);
        e.getGUI().getViewMap().remove(p);
    }

    @EventHandler
    public void onClick(@NotNull GUIClickEvent e) {
        GUIBase gui = e.getGUI();
        Player p = e.getPlayer();
        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        if (MCServerItems.isInventoryItem(item)) e.setCancelled(true);
        if (gui instanceof MultiplePageGUI mpGUI) {
            if (MCServerItems.equalWithKey(item, MCServerItems.prev, "ItemTag")) {
                mpGUI.prev();
                return;
            }
            if (MCServerItems.equalWithKey(item, MCServerItems.next, "ItemTag")) {
                mpGUI.next();
                return;
            }
        }
        if (MCServerItems.equalWithKey(item, MCServerItems.back, "ItemTag")) {
            gui.openLastInventory();
            return;
        }
        if (MCServerItems.equalWithKey(item, MCServerItems.close, "ItemTag")) {
            if (!e.isShiftClick() && (e.isLeftClick() || e.isRightClick())) {
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 0.7f, 1f);
            }
        }
        if (e.getGUI() instanceof MenuGUIInterface) {
            menuGUIListener.onClick(e);
            return;
        }
        if (e.getGUI() instanceof MarketGUIInterface) {
            marketGUIListener.onClick(e);
            return;
        }
        if (e.getGUI() instanceof MailGUIInterface) {
            mailGUIListener.onClick(e);
        }
    }

}
