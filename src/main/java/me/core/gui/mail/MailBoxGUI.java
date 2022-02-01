package me.core.gui.mail;

import me.core.enchantments.PluginEnchantments;
import me.core.gui.MultiplePageGUI;
import me.core.item.InventoryItem;
import me.core.mail.Mail;
import me.core.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MailBoxGUI extends MultiplePageGUI {

    public static HashMap<Player, MailBoxGUI> VIEW_MAP = new HashMap<>();

    public static void setup() {
        VIEW_MAP = new HashMap<>();
    }

    private final HashSet<Mail> selectedMail;

    public MailBoxGUI(Player player) {
        super(player);
        VIEW_MAP.put(player, this);
        this.selectedMail = new HashSet<>();
    }

    @Override
    public void setInventory() {
        List<ItemStack> stacks = new ArrayList<>();
        for (Mail mail : plugin.getMailManager().getMailList(this.player)) {
            if (!mail.isDeleted()) {
                stacks.add(mailStack(mail, false));
            }
        }
        this.setContents(stacks);
        this.toArray(VIEW_MAP.containsKey(this.getPlayer()) ? VIEW_MAP.get(this.player).getPage() : 1);
        this.inventory.setItem(0, info(this.player));
        this.inventory.setItem(1, writeMail());
        this.inventory.setItem(2, sentMail());
        this.inventory.setItem(3, mailBin());
        this.inventory.setItem(8, deleteMail());
        this.updateGUIName();
    }

    @Override
    public Component getGUIName() {
        return Component.translatable("gui.mail.box");
    }

    @Contract("_ -> new")
    private @NotNull InventoryItem info(Player p) {
        InventoryItem item = new InventoryItem(Material.PAPER).setTag("ItemTag", "gui.mail.box.info");
        item.setDisplayName(Component.translatable("gui.mail.box.info"));
        item.addLore(ComponentUtil.component(Component.translatable("gui.mail.box.info_total").args(Component.text(ChatColor.YELLOW.toString() + plugin.getMailManager().getMailCount(p)))));
        item.addLore(ComponentUtil.component(Component.translatable("gui.mail.box.info_new").args(Component.text(ChatColor.YELLOW.toString() + plugin.getMailManager().getUnreadMail(p).size()))));
        return item;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem writeMail() {
        InventoryItem item = new InventoryItem(Material.WRITABLE_BOOK).setTag("ItemTag", "gui.mail.box.write");
        item.setDisplayName(Component.translatable("gui.mail.box.write"));
        item.addLore(Component.translatable("gui.mail.box.write_lore"));
        return item;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem sentMail() {
        InventoryItem item = new InventoryItem(Material.WRITTEN_BOOK).setTag("ItemTag", "gui.mail.box.sent");
        item.setDisplayName(Component.translatable("gui.mail.box.sent"));
        item.addLore(Component.translatable("gui.mail.box.sent_lore"));
        return item;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem mailBin() {
        InventoryItem item = new InventoryItem(Material.BUCKET).setTag("ItemTag", "gui.mail.box.bin");
        item.setDisplayName(Component.translatable("gui.mail.box.bin"));
        item.addLore(Component.translatable("gui.mail.box.bin_lore"));
        return item;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem deleteMail() {
        InventoryItem item = new InventoryItem(Material.CAULDRON).setTag("ItemTag", "gui.mail.box.delete");
        item.setDisplayName(Component.translatable("gui.mail.box.delete"));
        item.addLore(Component.translatable("gui.mail.box.delete_lore1"));
        item.addLore(Component.translatable("gui.mail.box.delete_lore2"));
        return item;
    }

    private @NotNull InventoryItem mailStack(@NotNull Mail mail, boolean selected) {
        InventoryItem item = new InventoryItem(Material.PAPER).setTag("ItemTag", "gui.mail.box.mail").setTag("MailID", mail.getMailID());
        item.setDisplayName(ComponentUtil.translate(NamedTextColor.YELLOW, mail.getTitle()));
        String sender = mail.getSender().startsWith("player@") ? plugin.getServer().getOfflinePlayer(UUID.fromString(mail.getSender().substring(7))).getName() : mail.getSender();
        item.addLore(ComponentUtil.component(NamedTextColor.GRAY, Component.translatable("gui.mail.from"), Component.text(": " + Objects.requireNonNull(sender))));
        if (mail.getText().equals("gui.mail.no_text")) {
            item.addLore(ComponentUtil.component(NamedTextColor.GRAY, Component.translatable(mail.getText()).decoration(TextDecoration.ITALIC, true)));
        } else {
            String[] sa = (mail.getText()).split("\\\\n");
            for (String s : sa) {
                item.addLore(Component.text(ChatColor.GRAY + "§o" + s));
            }
        }
        item.addLore(ComponentUtil.component(NamedTextColor.GRAY, Component.translatable("gui.mail.attachment")));
        if (mail.getItemList().size() == 0) {
            item.addLore(ComponentUtil.component(NamedTextColor.GRAY, Component.translatable("gui.none")));
        } else {
            for (ItemStack stack : mail.getItemList()) {
                item.addLore(ComponentUtil.component(Component.text(ChatColor.GRAY + "- "), stack.displayName()));
            }
        }
        item.addLore(ComponentUtil.component(NamedTextColor.GREEN, Component.translatable("gui.mail.date"), Component.text(": " + mail.getDate())));
        if (mail.isReceived()) {
            item.addLore(Component.translatable("gui.mail.received"));
            item.setType(Material.MAP);
        }
        item.addLore(Component.translatable(selected ? "gui.mail.unselect" : "gui.mail.select"));
        item.addLore(Component.translatable("gui.mail.show_details"));
        if (selected) item.addUnsafeEnchantment(PluginEnchantments.WRAPPER, 0);
        return item;
    }

    public void update() {
        List<ItemStack> stacks = new ArrayList<>();
        for (Mail mail : plugin.getMailManager().getMailList(this.player)) {
            if (!mail.isDeleted()) {
                stacks.add(mailStack(mail, this.selectedMail.contains(mail)));
            }
        }
        this.setContents(stacks);
        this.toArray(VIEW_MAP.containsKey(this.player) ? VIEW_MAP.get(this.player).getPage() : 1);
        this.inventory.setItem(0, info(this.player));
    }

    public HashSet<Mail> getSelectedMail() {
        return this.selectedMail;
    }
}
