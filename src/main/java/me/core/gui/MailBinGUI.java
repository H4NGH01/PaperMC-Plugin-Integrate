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

public class MailBinGUI extends MultiplePageGUI {

    public static HashMap<Player, MailBinGUI> VIEW_MAP = new HashMap<>();

    public static void setup() {
        VIEW_MAP = new HashMap<>();
    }

    public MailBinGUI(Player player) {
        super(player);
        VIEW_MAP.put(player, this);
    }

    @Override
    public void setInventory() {
        List<ItemStack> stacks = new ArrayList<>();
        for (Mail mail : plugin.getMailManager().getDeletedMail(this.getPlayer())) {
            stacks.add(mail(mail));
        }
        this.setContents(stacks);
        this.toArray(VIEW_MAP.containsKey(this.getPlayer()) ? VIEW_MAP.get(this.getPlayer()).getPage() : 1);

        this.inventory.setItem(0, info(this.getPlayer()));
        this.inventory.setItem(48, MCServerItems.back);
        this.inventory.setItem(45, MCServerItems.prev);
        this.inventory.setItem(53, MCServerItems.next);

        this.updateGUIName();
    }

    @Override
    public String getGUIName() {
        return "gui.mail.bin";
    }

    @Contract("_ -> new")
    private @NotNull InventoryItem info(Player p) {
        InventoryItem item = new InventoryItem(Material.PAPER, Component.translatable("gui.mail.bin.info")).addTag("ItemTag", "gui.mail.bin.info");
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = new ArrayList<>();
        lore.add(Component.translatable("gui.mail.bin.info_count").append(Component.text(plugin.getMailManager().getDeletedMail(p).size())).decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.YELLOW)));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private @NotNull ItemStack mail(@NotNull Mail mail) {
        ItemStack item = new InventoryItem(Material.MAP, Component.translatable(mail.getTitle()).decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.YELLOW))).addTag("ItemTag", "gui.mail.bin.mail");
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = new ArrayList<>();
        String sender = mail.getSender().startsWith("player@") ? plugin.getServer().getOfflinePlayer(UUID.fromString(mail.getSender().substring(7))).getName() : mail.getSender();
        lore.add(Component.translatable("gui.mail.from").append(Component.text(": " + Objects.requireNonNull(sender))).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, false).color(ComponentHelper.convertTextColor(Color.GRAY)));
        if (mail.getText().equals("gui.mail.no_text")) {
            lore.add(Component.translatable(mail.getText()).color(ComponentHelper.convertTextColor(Color.GRAY)));
        } else {
            String[] sa = (mail.getText()).split("\\\\n");
            for (String s : sa) {
                lore.add(Component.text(s).color(ComponentHelper.convertTextColor(Color.GRAY)));
            }
        }
        lore.add(Component.translatable("gui.mail.attachment").decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.GRAY)));
        if (mail.getItemList().size() == 0) {
            lore.add(Component.translatable("gui.none").decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.GRAY)));
        } else {
            for (ItemStack stack : mail.getItemList()) {
                lore.add(Component.text("- ").append(stack.displayName()).decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.GRAY)));
            }
        }
        lore.add(Component.translatable("gui.mail.date").append(Component.text(": " + mail.getDate())).decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.LIME)));
        lore.add(Component.translatable("gui.mail.restore").decoration(TextDecoration.ITALIC, false).color(ComponentHelper.convertTextColor(Color.YELLOW)));
        meta.lore(lore);
        item.setItemMeta(meta);
        NBTHelper.addTag(item, "MailID", mail.getMailID());
        return item;
    }

    public void update() {
        List<ItemStack> stacks = new ArrayList<>();
        for (Mail mail : plugin.getMailManager().getDeletedMail(this.getPlayer())) stacks.add(mail(mail));
        this.setContents(stacks);
        this.toArray(VIEW_MAP.containsKey(this.getPlayer()) ? VIEW_MAP.get(this.getPlayer()).getPage() : 1);
        this.inventory.setItem(0, info(this.getPlayer()));
    }
}
