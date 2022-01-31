package me.core.event;

import me.core.MCServerPlugin;
import me.core.gui.mail.*;
import me.core.item.MCServerItems;
import me.core.mail.Mail;
import me.core.mail.NewMail;
import me.core.util.ComponentUtil;
import me.core.util.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MailGUIListener {

    private final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);
    private static final String[] MAIL_GUIS_NAME = {"gui.mail.box", "gui.mail.writer.menu", "gui.mail.writer.player_selector", "gui.mail.sent", "gui.mail.bin", "gui.mail.viewer"};

    public void onClick(@NotNull InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().startsWith(MAIL_GUIS_NAME[0])) {
            if (!(MailBoxGUI.VIEW_MAP.containsKey(p) && MailBoxGUI.VIEW_MAP.get(p).getInventory().equals(e.getInventory()))) return;
            this.onMailGUIClick(e);
            return;
        }
        if (e.getView().getTitle().startsWith(MAIL_GUIS_NAME[1])) {
            if (!(MailWriterGUI.VIEW_MAP.containsKey(p) && MailWriterGUI.VIEW_MAP.get(p).getInventory().equals(e.getInventory()))) return;
            this.onMailWriterGUIClick(e);
            return;
        }
        if (e.getView().getTitle().startsWith(MAIL_GUIS_NAME[2])) {
            if (!(MailPlayerSelectorGUI.VIEW_MAP.containsKey(p) && MailPlayerSelectorGUI.VIEW_MAP.get(p).getInventory().equals(e.getInventory()))) return;
            this.onMailSelectorGUIClick(e);
            return;
        }
        if (e.getView().getTitle().startsWith(MAIL_GUIS_NAME[3])) {
            if (!(MailSentGUI.VIEW_MAP.containsKey(p) && MailSentGUI.VIEW_MAP.get(p).getInventory().equals(e.getInventory()))) return;
            this.onSentMailGUIClick(e);
            return;
        }
        if (e.getView().getTitle().startsWith(MAIL_GUIS_NAME[4])) {
            if (!(MailBinGUI.VIEW_MAP.containsKey(p) && MailBinGUI.VIEW_MAP.get(p).getInventory().equals(e.getInventory()))) return;
            this.onMailBinGUIClick(e);
        }
        if (e.getView().getTitle().startsWith(MAIL_GUIS_NAME[5])) {
            if (!MailViewerGUI.VIEW_MAP.containsKey(p)) return;
            this.onMailViewerClick(e);
        }
    }

    private void onMailGUIClick(@NotNull InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        if (item.getItemMeta() == null) return;
        Player p = (Player) e.getWhoClicked();
        MailBoxGUI gui = MailBoxGUI.VIEW_MAP.get(p);
        if (MCServerItems.equalItem(item, MCServerItems.prev)) {
            gui.prev();
            return;
        }
        if (MCServerItems.equalItem(item, MCServerItems.next)) {
            gui.next();
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.box.write")) {
            MailWriterGUI mwg = MailWriterGUI.VIEW_MAP.containsKey(p) ? MailWriterGUI.VIEW_MAP.get(p) : new MailWriterGUI(p, new NewMail(p));
            mwg.setLastInventory(gui);
            mwg.openToPlayer();
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.box.sent")) {
            MailSentGUI msg = MailSentGUI.VIEW_MAP.containsKey(p) ? MailSentGUI.VIEW_MAP.get(p) : new MailSentGUI(p);
            msg.setLastInventory(gui);
            msg.openToPlayer();
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.box.bin")) {
            MailBinGUI mbg = MailBinGUI.VIEW_MAP.containsKey(p) ? MailBinGUI.VIEW_MAP.get(p) : new MailBinGUI(p);
            mbg.setLastInventory(gui);
            mbg.openToPlayer();
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.box.delete")) {
            if (e.isLeftClick()) {
                List<Mail> mailList = new ArrayList<>();
                int i = gui.getStartSlot();
                for (ItemStack mailStack : gui.getContents()) {
                    if (mailStack == null || mailStack.getType().equals(Material.AIR) || !mailStack.hasItemMeta() || !NBTHelper.hasTag(mailStack, "MailID")) continue;
                    for (Mail mail : plugin.getMailManager().getMailList(p)) {
                        if (mail.getMailID().equals(NBTHelper.getTag(mailStack).l("MailID")) && gui.isSelectedSlot(i)) mailList.add(mail);
                    }
                    i++;
                }
                for (Mail mail : mailList) {
                    mail.setDeleted();
                }
                gui.unselectedAllSlot();
            }
            if (e.isRightClick()) {
                for (Mail mail : plugin.getMailManager().getReceivedMail(p)) {
                    mail.setDeleted();
                }
            }
            gui.setPage(1);
            gui.update();
            p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_ELYTRA, 0.7f, 1f);
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.box.mail")) {
            for (Mail mail : plugin.getMailManager().getMailList(p)) {
                if (mail.getMailID().equals(NBTHelper.getTag(item).l("MailID"))) {
                    if (e.isRightClick()) {
                        if (!mail.isReceived()) mail.setReceived();
                        MailViewerGUI mvg = new MailViewerGUI(p, mail, ViewType.ADDRESSEE);
                        mvg.setLastInventory(gui);
                        mvg.openToPlayer();
                        break;
                    } else {
                        gui.selectSlot(e.getSlot(), !gui.isSelectedSlot(e.getSlot()));
                        gui.update();
                    }
                    p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.7f, 1f);
                    return;
                }
            }
        }
    }

    private void onMailWriterGUIClick(@NotNull InventoryClickEvent e) {
        e.setCancelled(true);
        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        Player p = (Player) e.getWhoClicked();
        MailWriterGUI gui = MailWriterGUI.VIEW_MAP.get(p);
        NewMail mail = gui.getMail();
        if (Objects.equals(e.getClickedInventory(), p.getInventory())) {
            if (!mail.isItemStacksFull()) {
                mail.addItemStack(item);
                gui.update();
                return;
            }
        }
        if (e.getSlot() >= 37 && e.getSlot() < 45 && e.getInventory().getItem(e.getSlot()) != null) {
            mail.removeItemStack(item);
            gui.update();
            return;
        }
        if (item.getItemMeta() == null) return;
        if (MCServerItems.equalItem(item, MCServerItems.back)) {
            gui.openLastInventory();
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.writer.title")) {
            p.closeInventory();
            ServerChatBarListener.CHAT_MAP.put(p, "chat.mail.edit.title");
            p.sendMessage(Component.translatable("chat.mail_title_type"));
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.writer.to")) {
            MailPlayerSelectorGUI mps =  MailPlayerSelectorGUI.VIEW_MAP.containsKey(p) ? MailPlayerSelectorGUI.VIEW_MAP.get(p) : new MailPlayerSelectorGUI(p);
            mps.setPage(1);
            mps.setLastInventory(gui);
            mps.openToPlayer();
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.writer.text")) {
            p.closeInventory();
            ServerChatBarListener.CHAT_MAP.put(p, "chat.mail.edit.text");
            p.sendMessage(Component.translatable("chat.mail_text_type"));
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.writer.send")) {
            if (mail.getAddressee().length == 0) {
                p.closeInventory();
                p.sendMessage(Component.translatable("chat.mail_send_failure.no_player_selected"));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.7f, 0f);
                return;
            }
            StringBuilder sb = new StringBuilder();
            List<ItemStack> stacks = new ArrayList<>();
            for (ItemStack stack : mail.getItemStacks()) {
                if (stack != null) stacks.add(stack);
            }
            for (OfflinePlayer op : mail.getAddressee()) {
                Mail m = new Mail(p, op, mail.getTitle(), mail.getText(), stacks);
                plugin.getMailManager().sendMail(m);
                sb.append(op.getName()).append(", ");
            }
            sb.deleteCharAt(sb.length() - 2);
            p.closeInventory();
            p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.7f, 1f);
            p.sendMessage(Component.translatable("chat.mail_send_success").args(ComponentUtil.text(ChatColor.YELLOW, sb.toString())));
        }
    }

    private void onMailSelectorGUIClick(@NotNull InventoryClickEvent e) {
        e.setCancelled(true);
        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        if (item.getItemMeta() == null) return;
        Player p = (Player) e.getWhoClicked();
        MailPlayerSelectorGUI gui = MailPlayerSelectorGUI.VIEW_MAP.get(p);
        if (MCServerItems.equalItem(item, MCServerItems.prev)) {
            gui.prev();
            return;
        }
        if (MCServerItems.equalItem(item, MCServerItems.next)) {
            gui.next();
            return;
        }
        if (MCServerItems.equalItem(item, MCServerItems.back)) {
            gui.openLastInventory();
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.selector.player_icon")) {
            for (OfflinePlayer offlinePlayer : plugin.getServer().getOfflinePlayers()) {
                if (Objects.equals(offlinePlayer.getName(), item.getItemMeta().getDisplayName().substring(2))) {
                    NewMail mail = MailWriterGUI.VIEW_MAP.get(p).getMail();
                    if (mail.containAddressee(offlinePlayer)) {
                        mail.removeAddressee(offlinePlayer);
                        p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 0.7f, 1f);
                    } else {
                        mail.addAddressee(offlinePlayer);
                        p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 0.7f, 1f);
                    }
                    e.getInventory().setItem(e.getSlot(), gui.playerIcon(offlinePlayer));
                    return;
                }
            }
        }
    }

    private void onSentMailGUIClick(@NotNull InventoryClickEvent e) {
        e.setCancelled(true);
        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        if (item.getItemMeta() == null) return;
        Player p = (Player) e.getWhoClicked();
        MailSentGUI gui = MailSentGUI.VIEW_MAP.get(p);
        if (MCServerItems.equalItem(item, MCServerItems.prev)) {
            gui.prev();
            return;
        }
        if (MCServerItems.equalItem(item, MCServerItems.next)) {
            gui.next();
            return;
        }
        if (MCServerItems.equalItem(item, MCServerItems.back)) {
            gui.openLastInventory();
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.sent.mail")) {
            for (Mail mail : plugin.getMailManager().getMailListBySender(p)) {
                if (mail.getMailID().equals(NBTHelper.getTag(item).l("MailID"))) {
                    if (e.isRightClick()) {
                        MailViewerGUI mvg = new MailViewerGUI(p, mail, ViewType.SENDER);
                        mvg.setLastInventory(gui);
                        mvg.openToPlayer();
                        break;
                    }
                    break;
                }
            }
        }
    }

    private void onMailBinGUIClick(@NotNull InventoryClickEvent e) {
        e.setCancelled(true);
        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        if (item.getItemMeta() == null) return;
        Player p = (Player) e.getWhoClicked();
        MailBinGUI gui = MailBinGUI.VIEW_MAP.get(p);
        if (MCServerItems.equalItem(item, MCServerItems.prev)) {
            gui.prev();
            return;
        }
        if (MCServerItems.equalItem(item, MCServerItems.next)) {
            gui.next();
            return;
        }
        if (MCServerItems.equalItem(item, MCServerItems.back)) {
            gui.openLastInventory();
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.bin.mail")) {
            for (Mail mail : plugin.getMailManager().getMailList(p)) {
                if (mail.getMailID().equals(NBTHelper.getTag(item).l("MailID"))) {
                    mail.restore();
                    gui.update();
                    p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_ELYTRA, 0.7f, 1f);
                    break;
                }
            }
        }
    }

    private void onMailViewerClick(@NotNull InventoryClickEvent e) {
        e.setCancelled(true);
        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        if (item.getItemMeta() == null) return;
        Player p = (Player) e.getWhoClicked();
        MailViewerGUI gui = MailViewerGUI.VIEW_MAP.get(p);
        if (MCServerItems.equalItem(item, MCServerItems.back)) {
            gui.openLastInventory();
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.viewer.delete")) {
            gui.getMail().setDeleted();
            p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_ELYTRA, 0.7f, 1f);
            gui.openLastInventory();
        }
    }
}
