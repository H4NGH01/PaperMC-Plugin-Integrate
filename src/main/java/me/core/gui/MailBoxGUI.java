package me.core.gui;

import me.core.item.InventoryItem;
import me.core.item.MCServerItems;
import me.core.mail.Mail;
import me.core.util.ComponentHelper;
import me.core.util.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MailBoxGUI extends MultiplePageGUI {

    public static HashMap<Player, MailBoxGUI> VIEW_MAP = new HashMap<>();

    public static void setup() {
        VIEW_MAP = new HashMap<>();
    }

    public MailBoxGUI(Player player) {
        super(player);
        VIEW_MAP.put(player, this);
    }

    @Override
    public void setInventory() {
        List<ItemStack> stacks = new ArrayList<>();
        for (Mail mail : plugin.getMailManager().getMailList(this.player)) {
            if (!mail.isDeleted()) stacks.add(mail(mail));
        }
        this.setContents(stacks);
        this.toArray(VIEW_MAP.containsKey(this.getPlayer()) ? VIEW_MAP.get(this.player).getPage() : 1);

        this.inventory.setItem(0, info(this.player));
        this.inventory.setItem(1, writeMail());
        this.inventory.setItem(2, sentMail());
        this.inventory.setItem(3, mailBin());
        this.inventory.setItem(8, deleteMail());
        this.inventory.setItem(45, MCServerItems.prev);
        this.inventory.setItem(53, MCServerItems.next);

        this.updateGUIName();
    }

    @Override
    public String getGUIName() {
        return "gui.mail.box";
    }

    @Contract("_ -> new")
    private @NotNull InventoryItem info(Player p) {
        InventoryItem item = new InventoryItem(Material.PAPER, Component.translatable("gui.mail.box.info")).addTag("ItemTag", "gui.mail.box.info");
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = new ArrayList<>();
        lore.add(Component.translatable("gui.mail.box.info_total").append(Component.text(plugin.getMailManager().getMailCount(p))).decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.YELLOW)));
        lore.add(Component.translatable("gui.mail.box.info_new").append(Component.text(plugin.getMailManager().getUnreadMail(p).size())).decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.YELLOW)));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem writeMail() {
        return new InventoryItem(Material.WRITABLE_BOOK, Component.translatable("gui.mail.box.write"), Component.translatable("gui.mail.box.write_lore")).addTag("ItemTag", "gui.mail.box.write");
    }

    @Contract(" -> new")
    private @NotNull InventoryItem sentMail() {
        return new InventoryItem(Material.WRITTEN_BOOK, Component.translatable("gui.mail.box.sent"), Component.translatable("gui.mail.box.sent_lore")).addTag("ItemTag", "gui.mail.box.sent");
    }

    @Contract(" -> new")
    private @NotNull InventoryItem mailBin() {
        return new InventoryItem(Material.BUCKET, Component.translatable("gui.mail.box.bin"), Component.translatable("gui.mail.box.bin_lore")).addTag("ItemTag", "gui.mail.box.bin");
    }

    @Contract(" -> new")
    private @NotNull InventoryItem deleteMail() {
        return new InventoryItem(Material.CAULDRON, Component.translatable("gui.mail.box.delete"), Component.translatable("gui.mail.box.delete_lore")).addTag("ItemTag", "gui.mail.box.delete");
    }

    private @NotNull ItemStack mail(@NotNull Mail mail) {
        InventoryItem item = new InventoryItem(Material.PAPER, Component.translatable(mail.getTitle()).decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.YELLOW))).addTag("ItemTag", "gui.mail.box.mail");
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = new ArrayList<>();
        String sender = mail.getSender().startsWith("player@") ? plugin.getServer().getOfflinePlayer(UUID.fromString(mail.getSender().substring(7))).getName() : mail.getSender();
        lore.add(Component.translatable("gui.mail.from").append(Component.text(": " + Objects.requireNonNull(sender))).decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.GRAY)));
        if (mail.getText().equals("gui.mail.no_text")) {
            lore.add(Component.translatable(mail.getText()).color(ComponentHelper.convertTextColor(Color.GRAY)));
        } else {
            String[] sa = (mail.getText()).split("\\\\n");
            for (String s : sa) {
                lore.add(Component.text(s).color(ComponentHelper.convertTextColor(Color.GRAY)));
            }
        }
        lore.add(Component.translatable("gui.mail.attachment").decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, false).color(ComponentHelper.convertTextColor(Color.GRAY)));
        if (mail.getItemList().size() == 0) {
            lore.add(Component.translatable("gui.none").decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.GRAY)));
        } else {
            for (ItemStack stack : mail.getItemList()) {
                lore.add(Component.text("- ").append(stack.displayName()).decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.GRAY)));
            }
        }
        lore.add(Component.translatable("gui.mail.date").append(Component.text(": " + mail.getDate())).decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.LIME)));
        if (mail.isReceived()) {
            lore.add(Component.translatable("gui.mail.received"));
            item.setType(Material.MAP);
        }
        lore.add(Component.translatable("gui.mail.show_details"));
        meta.lore(lore);
        item.setItemMeta(meta);
        NBTHelper.addTag(item, "MailID", mail.getMailID());
        return item;
    }

    public void update() {
        List<ItemStack> stacks = new ArrayList<>();
        for (Mail mail : plugin.getMailManager().getMailList(this.player)) {
            if (!mail.isDeleted()) stacks.add(mail(mail));
        }
        this.setContents(stacks);
        this.toArray(VIEW_MAP.containsKey(this.player) ? VIEW_MAP.get(this.player).getPage() : 1);
        this.inventory.setItem(0, info(this.player));
    }
}
