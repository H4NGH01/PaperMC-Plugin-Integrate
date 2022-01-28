package me.core.gui;

import me.core.item.InventoryItem;
import me.core.item.MCServerItems;
import me.core.mail.Mail;
import me.core.util.ComponentHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MailViewerGUI extends GUIBase {

    public static HashSet<Player> VIEW_SET = new HashSet<>();
    private final Mail mail;
    private final ViewType type;

    public static void setup() {
        VIEW_SET = new HashSet<>();
    }

    public MailViewerGUI(Player player, Mail mail, ViewType type) {
        super(player);
        this.mail = mail;
        this.type = type;
        VIEW_SET.add(player);
    }

    @Override
    public void setInventory() {
        this.inventory.setItem(9, title());
        this.inventory.setItem(10, text());
        this.inventory.setItem(18, from());
        this.inventory.setItem(19, to());
        this.inventory.setItem(27, attachment());
        for (int i = 0; i < mail.getItemList().size(); i++) {
            this.inventory.setItem(i + 28, mail.getItemList().get(i));
        }
        this.inventory.setItem(36, date());
        this.inventory.setItem(48, MCServerItems.back);

        this.setTitle(getGUIName(), mail.getMailID());
    }

    @Override
    public String getGUIName() {
        return "gui.mail.viewer";
    }

    @Contract(" -> new")
    private @NotNull InventoryItem title() {
        return new InventoryItem(Material.PAPER, Component.translatable("gui.mail.title").decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.LIME)), Component.translatable(mail.getTitle()).decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.YELLOW))).addTag("ItemTag", "gui.mail.viewer.title");
    }

    @Contract(" -> new")
    private @NotNull InventoryItem text() {
        InventoryItem item = new InventoryItem(Material.PAPER, Component.translatable("gui.mail.text").decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.LIME))).addTag("ItemTag", "gui.mail.viewer.content");
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = new ArrayList<>();
        if (this.mail.getText().equals("gui.mail.no_text")) {
            lore.add(Component.translatable(mail.getText()).color(ComponentHelper.convertTextColor(Color.GRAY)));
        } else {
            String[] sa = (this.mail.getText()).split("\\\\n");
            for (String s : sa) {
                lore.add(Component.text(s).color(ComponentHelper.convertTextColor(Color.GRAY)));
            }
        }
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem from() {
        String sender = mail.getSender().startsWith("player@") ? plugin.getServer().getOfflinePlayer(UUID.fromString(mail.getSender().substring(7))).getName() : mail.getSender();
        return new InventoryItem(Material.PLAYER_HEAD, Component.translatable("gui.mail.from").decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.LIME)), Component.text(Objects.requireNonNull(sender)).decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.YELLOW))).addTag("ItemTag", "gui.mail.viewer.from");
    }

    @Contract(" -> new")
    private @NotNull InventoryItem to() {
        return new InventoryItem(Material.PLAYER_HEAD, Component.translatable("gui.mail.to").decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.LIME)), Component.text(Objects.requireNonNull(plugin.getServer().getOfflinePlayer(mail.getAddressee()).getName())).decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.YELLOW))).addTag("ItemTag", "gui.mail.viewer.to");
    }

    @Contract(" -> new")
    private @NotNull InventoryItem attachment() {
        InventoryItem item = new InventoryItem(Material.PAPER, Component.translatable("gui.mail.attachment").decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, false).color(ComponentHelper.convertTextColor(Color.LIME))).addTag("ItemTag", "gui.mail.viewer.attachment");
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = new ArrayList<>();
        if (mail.getItemList().size() == 0) lore.add(Component.translatable("gui.none").decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.GRAY)));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem date() {
        return new InventoryItem(Material.PAPER, Component.translatable("gui.mail.date").append(Component.text(": " + mail.getDate())).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, false).color(ComponentHelper.convertTextColor(Color.LIME))).addTag("ItemTag", "gui.mail.viewer.date");
    }

    public ViewType getViewType() {
        return this.type;
    }
}
