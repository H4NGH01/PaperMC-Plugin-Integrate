package me.core.gui;

import me.core.item.InventoryItem;
import me.core.item.MCServerItems;
import me.core.mail.NewMail;
import me.core.util.ComponentHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MailWriterGUI extends GUIBase {

    public static HashMap<Player, MailWriterGUI> VIEW_MAP = new HashMap<>();
    private final NewMail mail;

    public static void setup() {
        VIEW_MAP = new HashMap<>();
    }

    public MailWriterGUI(Player player, NewMail mail) {
        super(player);
        this.mail = mail;
        VIEW_MAP.put(player, this);
    }

    @Override
    public void setInventory() {
        for (int i = 0; i < 8; i++) {
            this.inventory.setItem(i + 37, this.mail.getItemStacks()[i]);
        }
        this.inventory.setItem(8, send());
        this.inventory.setItem(9, title());
        this.inventory.setItem(18, to());
        this.inventory.setItem(27, text());
        this.inventory.setItem(36, attachment());
        this.inventory.setItem(48, MCServerItems.back);
    }

    @Override
    public String getGUIName() {
        return "gui.mail.writer.menu";
    }

    @Contract(" -> new")
    private @NotNull InventoryItem title() {
        return new InventoryItem(Material.PAPER, Component.translatable("gui.mail.title").decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.LIME)), Component.translatable(mail.getTitle()).decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.YELLOW)), Component.translatable("gui.mail.edit")).addTag("ItemTag", "gui.mail.writer.title");
    }

    @Contract(" -> new")
    private @NotNull InventoryItem to() {
        TextComponent.Builder component = Component.text();
        if (this.mail.getAddressee().length > 0) {
            for (int i = 0; i < this.mail.getAddressee().length; i++) {
                OfflinePlayer op = this.mail.getAddressee()[i];
                component.append(Component.text(Objects.requireNonNull(op.getName())).color(ComponentHelper.convertTextColor(Color.YELLOW)));
                if (i < this.mail.getAddressee().length - 1) component.append(Component.text(", "));
            }
        } else {
            component.append(Component.translatable("gui.none")).decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.GRAY));
        }
        return new InventoryItem(Material.PLAYER_HEAD, Component.translatable("gui.mail.to").decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.LIME)), component.color(ComponentHelper.convertTextColor(Color.GRAY)).decoration(TextDecoration.ITALIC, false).build(), Component.translatable("gui.mail.edit")).addTag("ItemTag", "gui.mail.writer.to");
    }

    @Contract(" -> new")
    private @NotNull InventoryItem text() {
        InventoryItem item = new InventoryItem(Material.PAPER, Component.translatable("gui.mail.text").decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.LIME))).addTag("ItemTag", "gui.mail.writer.text");
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
        lore.add(Component.translatable("gui.mail.edit"));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem attachment() {
        InventoryItem item = new InventoryItem(Material.PAPER, Component.translatable("gui.mail.attachment").decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.LIME))).addTag("ItemTag", "gui.mail.writer.attachment");
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = new ArrayList<>();
        lore.add(Component.translatable("gui.mail.attachment_edit_lore1").decoration(TextDecoration.ITALIC, false));
        lore.add(Component.translatable("gui.mail.attachment_edit_lore2").decoration(TextDecoration.ITALIC, false));
        lore.add(Component.translatable("gui.mail.edit"));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem send() {
        return new InventoryItem(Material.GREEN_CONCRETE, Component.translatable("gui.mail.writer.menu.send")).addTag("ItemTag", "gui.mail.writer.send");
    }

    public NewMail getMail() {
        return this.mail;
    }

    public void update() {
        for (int i = 0; i < 8; i++) {
            this.inventory.setItem(i + 37, this.mail.getItemStacks()[i]);
        }
        this.inventory.setItem(8, send());
        this.inventory.setItem(9, title());
        this.inventory.setItem(18, to());
        this.inventory.setItem(27, text());
        this.inventory.setItem(36, attachment());
        this.inventory.setItem(48, MCServerItems.back);
    }
}
