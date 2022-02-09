package me.core.gui.mail;

import me.core.gui.GUIBase;
import me.core.gui.MultiplePageGUI;
import me.core.items.InventoryItem;
import me.core.mail.Mail;
import me.core.mail.MailManager;
import me.core.utils.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MailSentGUI extends MultiplePageGUI implements MailGUIInterface {

    private static final HashMap<Player, MailSentGUI> VIEW_MAP = new HashMap<>();

    public MailSentGUI(@NotNull Player player) {
        super(player);
        VIEW_MAP.put(player, this);
        this.setDefault();
    }

    @Override
    public void setInventory() {
        List<ItemStack> stacks = new ArrayList<>();
        for (Mail mail : MailManager.getMailListBySender(this.getPlayer())) {
            stacks.add(mailStack(mail));
        }
        this.setContents(stacks);
        this.toArray(VIEW_MAP.containsKey(this.getPlayer()) ? VIEW_MAP.get(this.getPlayer()).getPage() : 1);
        this.inventory.setItem(0, info(this.getPlayer()));
    }

    @Override
    public Component getGUIName() {
        return Component.translatable("gui.mail.sent");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends GUIBase> HashMap<Player, T> getViewMap() {
        return (HashMap<Player, T>) VIEW_MAP;
    }

    public static HashMap<Player, MailSentGUI> getViews() {
        return VIEW_MAP;
    }

    @Override
    public void openToPlayer() {
        this.player.playSound(this.player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 0.7f, 1f);
        super.openToPlayer();
    }

    @Contract("_ -> new")
    private @NotNull InventoryItem info(Player p) {
        InventoryItem item = new InventoryItem(Material.PAPER).setTag("ItemTag", "gui.mail.sent.info");
        item.setDisplayName(Component.translatable("gui.mail.sent.info"));
        item.addLore(Component.translatable("gui.mail.sent.info_total").args(Component.text(ChatColor.YELLOW.toString() + MailManager.getMailListBySender(p).size())));
        int i = 0;
        for (Mail mail : MailManager.getMailListBySender(p)) {
            if (mail.isReceived()) i++;
        }
        item.addLore(Component.translatable("gui.mail.sent.info_read").args(Component.text(ChatColor.YELLOW.toString() + i)));
        return item;
    }

    @Contract("_ -> new")
    private @NotNull InventoryItem mailStack(@NotNull Mail mail) {
        InventoryItem item = new InventoryItem(Material.PAPER).setTag("ItemTag", "gui.mail.sent.mail").setTag("MailID", mail.getMailID());
        item.setDisplayName(ComponentUtil.translate(NamedTextColor.YELLOW, mail.getTitle()));
        item.addLore(ComponentUtil.translate(NamedTextColor.GRAY, "gui.mail.to").append(Component.text(ChatColor.GRAY + ": " + Objects.requireNonNull(plugin.getServer().getOfflinePlayer(mail.getAddressee()).getName()))));

        if (mail.getText().equals("gui.mail.no_text")) {
            item.addLore(ComponentUtil.translate(NamedTextColor.GRAY, mail.getText()).decoration(TextDecoration.ITALIC, true));
        } else {
            String[] sa = (mail.getText()).split("\\\\n");
            for (String s : sa) {
                item.addLore(Component.text(ChatColor.GRAY + "§o" + s));
            }
        }
        item.addLore(ComponentUtil.translate(NamedTextColor.GRAY, "gui.mail.attachment"));
        if (mail.getItemList().size() == 0) {
            item.addLore(ComponentUtil.translate(NamedTextColor.GRAY, "gui.none"));
        } else {
            for (ItemStack stack : mail.getItemList()) {
                item.addLore(ComponentUtil.component(NamedTextColor.GRAY, Component.text("- "), stack.displayName()));
            }
        }
        item.addLore(ComponentUtil.translate(NamedTextColor.GREEN, "gui.mail.date").append(Component.text(": " + mail.getDate())));
        if (mail.isReceived()) {
            item.addLore(Component.translatable("gui.mail.received"));
            item.setType(Material.MAP);
        }
        if (mail.isDeleted()) {
            item.addLore(Component.translatable("gui.mail.deleted"));
            item.setType(Material.CAULDRON);
        }
        item.addLore(Component.translatable("gui.show_details"));
        return item;
    }
}
