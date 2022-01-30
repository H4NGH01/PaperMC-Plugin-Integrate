package me.core.mail;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Mail {

    private final String id;
    private final String sender;
    private final UUID addressee;
    private final String title;
    private final String text;
    private final List<ItemStack> itemList;
    private final String date;
    private boolean received = false;
    private boolean deleted = false;

    public Mail(@NotNull Player sender, @NotNull OfflinePlayer addressee, String title, String text, List<ItemStack> items) {
        this.id = "mail@" + UUID.randomUUID();
        this.sender = "player@" + sender.getUniqueId();
        this.addressee = addressee.getUniqueId();
        this.title = title;
        this.text = text;
        this.itemList = items;
        this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(new Date(System.currentTimeMillis()));
    }

    public Mail(String sender, @NotNull Player addressee, String title, String text, List<ItemStack> items) {
        this.id = "mail@" + UUID.randomUUID();
        this.sender = sender;
        this.addressee = addressee.getUniqueId();
        this.title = title;
        this.text = text;
        this.itemList = items;
        this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(new Date(System.currentTimeMillis()));
    }

    public Mail(String id, String sender, String addressee, String title, String text, List<ItemStack> items, String date, boolean received, boolean deleted) {
        this.id = id;
        this.sender = sender;
        this.addressee = UUID.fromString(addressee);
        this.title = title;
        this.text = text;
        this.itemList = items;
        this.date = date;
        this.received = received;
        this.deleted = deleted;
    }

    public String getMailID() {
        return this.id;
    }

    public String getSender() {
        return this.sender;
    }

    public UUID getAddressee() {
        return this.addressee;
    }

    public String getTitle() {
        return this.title;
    }

    public String getText() {
        return this.text;
    }

    public List<ItemStack> getItemList() {
        return this.itemList;
    }

    public String getDate() {
        return this.date;
    }

    public boolean isReceived() {
        return this.received;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void addItem(ItemStack... stack) {
        Collections.addAll(this.itemList, stack);
    }

    public void setReceived() {
        this.received = true;
    }

    public void setDeleted() {
        this.deleted = true;
    }

    public void restore() {
        this.deleted = false;
    }
}
