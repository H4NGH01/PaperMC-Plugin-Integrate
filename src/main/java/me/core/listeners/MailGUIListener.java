package me.core.listeners;

import me.core.MCServerPlugin;
import me.core.PlayerSettings;
import me.core.ServerPlayer;
import me.core.events.GUIClickEvent;
import me.core.gui.mail.*;
import me.core.items.MCServerItems;
import me.core.mail.Mail;
import me.core.mail.MailManager;
import me.core.mail.NewMail;
import me.core.utils.ComponentUtil;
import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MailGUIListener {

    private final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);

    public void onClick(@NotNull GUIClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item == null || !item.hasItemMeta()) return;
        if (e.getGUI() instanceof MailBoxGUI) {
            this.onMailGUIClick(e);
            return;
        }
        if (e.getGUI() instanceof MailWriterGUI) {
            this.onMailWriterGUIClick(e);
            return;
        }
        if (e.getGUI() instanceof MailPlayerSelectorGUI) {
            this.onMailSelectorGUIClick(e);
            return;
        }
        if (e.getGUI() instanceof MailSentGUI) {
            this.onSentMailGUIClick(e);
            return;
        }
        if (e.getGUI() instanceof MailBinGUI) {
            this.onMailBinGUIClick(e);
        }
        if (e.getGUI() instanceof MailViewerGUI) {
            this.onMailViewerClick(e);
        }
    }

    private void onMailGUIClick(@NotNull GUIClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player p = e.getPlayer();
        MailBoxGUI gui = (MailBoxGUI) e.getGUI();
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.box.write")) {
            MailWriterGUI mwg = new MailWriterGUI(p, MailManager.getNewMailMap().containsKey(p.getUniqueId()) ? MailManager.getNewMailMap().get(p.getUniqueId()) : new NewMail(p));
            mwg.setLastInventory(gui);
            mwg.openToPlayer();
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.box.sent")) {
            MailSentGUI msg = new MailSentGUI(p);
            msg.setLastInventory(gui);
            msg.openToPlayer();
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.box.bin")) {
            MailBinGUI mbg = new MailBinGUI(p);
            mbg.setLastInventory(gui);
            mbg.openToPlayer();
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.box.delete")) {
            if (e.isLeftClick()) {
                List<Mail> mailList = new ArrayList<>(gui.getSelectedMail());
                for (Mail mail : mailList) {
                    mail.setDeleted();
                }
                gui.getSelectedMail().clear();
            }
            if (e.isRightClick()) {
                for (Mail mail : MailManager.getReceivedMail(p)) {
                    mail.setDeleted();
                }
            }
            gui.setPage(1);
            gui.update();
            p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_ELYTRA, 0.7f, 1f);
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.box.mail")) {
            Mail mail = MailManager.getMailByID(NBTHelper.getTag(item).l("MailID"));
            if (e.isRightClick()) {
                assert mail != null;
                if (!mail.isReceived()) {
                    mail.setReceived();
                    p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.7f, 1f);
                }
                MailViewerGUI mvg = new MailViewerGUI(p, mail, ViewType.ADDRESSEE);
                mvg.setLastInventory(gui);
                mvg.openToPlayer();
            } else {
                if (gui.getSelectedMail().contains(mail)) {
                    gui.getSelectedMail().remove(mail);
                } else {
                    gui.getSelectedMail().add(mail);
                }
                gui.update();
                p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.7f, 1f);
            }
        }
    }

    private void onMailWriterGUIClick(@NotNull GUIClickEvent e) {
        e.setCancelled(true);
        ItemStack item = e.getCurrentItem();
        Player p = e.getPlayer();
        MailWriterGUI gui = (MailWriterGUI) e.getGUI();
        NewMail mail = MailManager.getNewMailMap().get(p.getUniqueId());
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
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.writer.title")) {
            p.closeInventory();
            ServerChatBarListener.CHAT_MAP.put(p, "chat.mail.edit.title");
            p.sendMessage(Component.translatable("chat.mail.title_type"));
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.writer.to")) {
            MailPlayerSelectorGUI mps = MailPlayerSelectorGUI.getViews().containsKey(p) ? MailPlayerSelectorGUI.getViews().get(p) : new MailPlayerSelectorGUI(p);
            mps.setPage(1);
            mps.setLastInventory(gui);
            mps.openToPlayer();
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.writer.text")) {
            p.closeInventory();
            ServerChatBarListener.CHAT_MAP.put(p, "chat.mail.edit.text");
            p.sendMessage(Component.translatable("chat.mail.text_type"));
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.writer.send")) {
            if (mail.getAddressee().length == 0) {
                p.closeInventory();
                p.sendMessage(Component.translatable("chat.mail.send_failure.no_player_selected"));
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
                MailManager.sendMail(m);
                sb.append(op.getName()).append(", ");
                ServerPlayer sp = ServerPlayer.getServerPlayer(op);
                if (op.isOnline() && sp.getSettings().get(PlayerSettings.NEW_MAIL_MESSAGE)) {
                    Objects.requireNonNull(op.getPlayer()).sendMessage(Component.translatable("chat.mail.received"));
                } else {
                    sp.setNewMail(sp.getNewMail() + 1);
                    sp.save();
                }
            }
            sb.deleteCharAt(sb.length() - 2);
            p.closeInventory();
            p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.7f, 1f);
            p.sendMessage(Component.translatable("chat.mail.send_success").args(ComponentUtil.text(NamedTextColor.YELLOW, sb.toString())));
        }
    }

    private void onMailSelectorGUIClick(@NotNull GUIClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player p = e.getPlayer();
        MailPlayerSelectorGUI gui = (MailPlayerSelectorGUI) e.getGUI();
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.selector.player_icon")) {
            for (OfflinePlayer offlinePlayer : plugin.getServer().getOfflinePlayers()) {
                if (Objects.equals(offlinePlayer.getName(), ComponentUtil.plainText(Objects.requireNonNull(Objects.requireNonNull(item).getItemMeta().displayName())).substring(2))) {
                    NewMail mail = MailManager.getNewMailMap().get(p.getUniqueId());
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

    private void onSentMailGUIClick(@NotNull GUIClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player p = e.getPlayer();
        MailSentGUI gui = (MailSentGUI) e.getGUI();
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.sent.mail")) {
            Mail mail = MailManager.getMailByID(NBTHelper.getTag(item).l("MailID"));
            if (e.isRightClick()) {
                assert mail != null;
                MailViewerGUI mvg = new MailViewerGUI(p, mail, ViewType.SENDER);
                mvg.setLastInventory(gui);
                mvg.openToPlayer();
            }
        }
    }

    private void onMailBinGUIClick(@NotNull GUIClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player p = e.getPlayer();
        MailBinGUI gui = (MailBinGUI) e.getGUI();
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.bin.mail")) {
            Mail mail = MailManager.getMailByID(NBTHelper.getTag(item).l("MailID"));
            assert mail != null;
            mail.restore();
            gui.update();
            p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_ELYTRA, 0.7f, 1f);
        }
    }

    private void onMailViewerClick(@NotNull GUIClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player p = e.getPlayer();
        MailViewerGUI gui = (MailViewerGUI) e.getGUI();
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.mail.viewer.delete")) {
            gui.getMail().setDeleted();
            p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_ELYTRA, 0.7f, 1f);
            gui.openLastInventory();
            return;
        }
        if (e.getSlot() >= 28 && e.getSlot() < 36 && e.getInventory().getItem(e.getSlot()) != null) {
            e.setCancelled(true);
        }
    }
}
