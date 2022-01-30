package me.core.gui.mail;

import me.core.gui.MultiplePageGUI;
import me.core.item.InventoryItem;
import me.core.item.MCServerItems;
import me.core.mail.Mail;
import me.core.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
        InventoryItem item = new InventoryItem(Material.PAPER).setTag("ItemTag", "gui.mail.bin.info");
        item.setDisplayName(ComponentUtil.translate("gui.mail.bin.info"));
        item.setLore(ComponentUtil.component(Component.translatable("gui.mail.bin.info_count").args(Component.text(ChatColor.YELLOW.toString() + plugin.getMailManager().getDeletedMail(p).size()))));
        return item;
    }

    private @NotNull ItemStack mail(@NotNull Mail mail) {
        InventoryItem item = new InventoryItem(Material.MAP).setTag("ItemTag", "gui.mail.bin.mail").setTag("MailID", mail.getMailID());
        item.setDisplayName(ComponentUtil.component(ChatColor.YELLOW, Component.translatable(mail.getTitle())));
        String sender = mail.getSender().startsWith("player@") ? plugin.getServer().getOfflinePlayer(UUID.fromString(mail.getSender().substring(7))).getName() : mail.getSender();
        item.addLore(ComponentUtil.component(ChatColor.GRAY, ComponentUtil.translate("gui.mail.from"), ComponentUtil.text(": " + Objects.requireNonNull(sender))));
        if (mail.getText().equals("gui.mail.no_text")) {
            item.addLore(ComponentUtil.translate(ChatColor.GRAY, mail.getText()).decoration(TextDecoration.ITALIC, true));
        } else {
            String[] sa = (mail.getText()).split("\\\\n");
            for (String s : sa) {
                item.addLore(Component.text(ChatColor.GRAY + "§o" + s));
            }
        }
        item.addLore(ComponentUtil.translate(ChatColor.GRAY, "gui.mail.attachment"));
        if (mail.getItemList().size() == 0) {
            item.addLore(ComponentUtil.translate(ChatColor.GRAY, "gui.none"));
        } else {
            for (ItemStack stack : mail.getItemList()) {
                item.addLore(ComponentUtil.component(ChatColor.GRAY, Component.text("- ").append(stack.displayName())));
            }
        }
        item.addLore(ComponentUtil.component(ChatColor.GREEN, ComponentUtil.translate("gui.mail.date"), Component.text(": " + mail.getDate())));
        item.addLore(ComponentUtil.translate("gui.mail.restore"));
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
