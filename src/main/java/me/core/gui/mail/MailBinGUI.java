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

import java.util.*;

public class MailBinGUI extends MultiplePageGUI implements MailGUIInterface {

    private static final HashMap<Player, MailBinGUI> VIEW_MAP = new HashMap<>();

    public MailBinGUI(@NotNull Player player) {
        super(player);
        this.setDefault();
    }

    @Override
    public void setInventory() {
        List<ItemStack> stacks = new ArrayList<>();
        for (Mail mail : MailManager.getDeletedMail(this.getPlayer())) {
            stacks.add(mailStack(mail));
        }
        this.setContents(stacks);
        this.toArray(VIEW_MAP.containsKey(this.getPlayer()) ? VIEW_MAP.get(this.getPlayer()).getPage() : 1);
        this.inventory.setItem(0, info(this.getPlayer()));
    }

    @Override
    public Component getGUIName() {
        return Component.translatable("gui.mail.bin");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends GUIBase> HashMap<Player, T> getViewMap() {
        return (HashMap<Player, T>) VIEW_MAP;
    }

    public static HashMap<Player, MailBinGUI> getViews() {
        return VIEW_MAP;
    }

    @Override
    public void openToPlayer() {
        super.openToPlayer();
        this.player.playSound(this.player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 0.7f, 1f);
    }

    @Contract("_ -> new")
    private @NotNull InventoryItem info(Player p) {
        InventoryItem item = new InventoryItem(Material.PAPER).setTag("ItemTag", "gui.mail.bin.info");
        item.setDisplayName(ComponentUtil.translate("gui.mail.bin.info"));
        item.setLore(ComponentUtil.component(Component.translatable("gui.mail.bin.info_count").args(Component.text(ChatColor.YELLOW.toString() + MailManager.getDeletedMail(p).size()))));
        return item;
    }

    @Contract("_ -> new")
    private @NotNull InventoryItem mailStack(@NotNull Mail mail) {
        InventoryItem item = new InventoryItem(Material.FILLED_MAP).setTag("ItemTag", "gui.mail.bin.mail").setTag("MailID", mail.getMailID());
        item.setDisplayName(ComponentUtil.component(NamedTextColor.YELLOW, Component.translatable(mail.getTitle())));
        String sender = mail.getSender().startsWith("player@") ? plugin.getServer().getOfflinePlayer(UUID.fromString(mail.getSender().substring(7))).getName() : mail.getSender();
        item.addLore(ComponentUtil.component(NamedTextColor.GRAY, ComponentUtil.translate("gui.mail.from"), ComponentUtil.text(": " + Objects.requireNonNull(sender))));
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
        item.addLore(ComponentUtil.component(NamedTextColor.GREEN, ComponentUtil.translate("gui.mail.date"), Component.text(": " + mail.getDate())));
        item.addLore(ComponentUtil.translate("gui.mail.restore"));
        return item;
    }

    public void update() {
        List<ItemStack> stacks = new ArrayList<>();
        for (Mail mail : MailManager.getDeletedMail(this.getPlayer())) stacks.add(mailStack(mail));
        this.setContents(stacks);
        this.toArray(VIEW_MAP.containsKey(this.getPlayer()) ? VIEW_MAP.get(this.getPlayer()).getPage() : 1);
        this.inventory.setItem(0, info(this.getPlayer()));
    }
}
