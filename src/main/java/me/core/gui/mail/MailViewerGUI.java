package me.core.gui.mail;

import me.core.gui.GUIBase;
import me.core.item.InventoryItem;
import me.core.item.MCServerItems;
import me.core.mail.Mail;
import me.core.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class MailViewerGUI extends GUIBase {

    public static HashMap<Player, MailViewerGUI> VIEW_MAP = new HashMap<>();
    private final Mail mail;
    private final ViewType type;

    public static void setup() {
        VIEW_MAP = new HashMap<>();
    }

    public MailViewerGUI(Player player, Mail mail, ViewType type) {
        super(player);
        this.mail = mail;
        this.type = type;
        VIEW_MAP.put(player, this);
    }

    @Override
    public void setInventory() {
        this.inventory.setItem(9, from());
        this.inventory.setItem(10, to());
        this.inventory.setItem(18, title());
        this.inventory.setItem(19, text());
        this.inventory.setItem(27, attachment());
        for (int i = 0; i < mail.getItemList().size(); i++) {
            this.inventory.setItem(i + 28, mail.getItemList().get(i));
        }
        this.inventory.setItem(36, date());
        if (this.type.equals(ViewType.ADDRESSEE)) {
            this.inventory.setItem(44, delete());
        }
        this.inventory.setItem(48, MCServerItems.back);

        this.setTitle(getGUIName(), mail.getMailID());
    }

    @Override
    public String getGUIName() {
        return "gui.mail.viewer";
    }

    @Contract(" -> new")
    private @NotNull InventoryItem from() {
        String sender = mail.getSender().startsWith("player@") ? plugin.getServer().getOfflinePlayer(UUID.fromString(mail.getSender().substring(7))).getName() : mail.getSender();
        InventoryItem item = new InventoryItem(Material.PLAYER_HEAD).addTag("ItemTag", "gui.mail.viewer.from");
        item.setDisplayName(ComponentUtil.translate(ChatColor.GREEN, "gui.mail.from"));
        item.addLore(Component.text(ChatColor.YELLOW + Objects.requireNonNull(sender)));
        return item;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem to() {
        InventoryItem item = new InventoryItem(Material.PLAYER_HEAD).addTag("ItemTag", "gui.mail.viewer.to");
        item.setDisplayName(ComponentUtil.translate(ChatColor.GREEN, "gui.mail.to"));
        item.addLore(Component.text(ChatColor.YELLOW + Objects.requireNonNull(plugin.getServer().getOfflinePlayer(mail.getAddressee()).getName())));
        return item;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem title() {
        InventoryItem item = new InventoryItem(Material.PAPER).addTag("ItemTag", "gui.mail.viewer.title");
        item.setDisplayName(ComponentUtil.translate(ChatColor.GREEN, "gui.mail.title"));
        item.setLore(ComponentUtil.translate(ChatColor.YELLOW, mail.getTitle()));
        return item;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem text() {
        InventoryItem item = new InventoryItem(Material.PAPER).addTag("ItemTag", "gui.mail.viewer.content");
        item.setDisplayName(ComponentUtil.translate(ChatColor.GREEN, "gui.mail.text"));
        if (this.mail.getText().equals("gui.mail.no_text")) {
            item.addLore(ComponentUtil.translate(ChatColor.GRAY, mail.getText()));
        } else {
            String[] sa = (this.mail.getText()).split("\\\\n");
            for (String s : sa) {
                item.addLore(Component.text(ChatColor.GRAY + "§o" + s));
            }
        }
        return item;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem attachment() {
        InventoryItem item = new InventoryItem(Material.PAPER).addTag("ItemTag", "gui.mail.viewer.attachment");
        item.setDisplayName(ComponentUtil.translate(ChatColor.GREEN, "gui.mail.attachment"));
        if (mail.getItemList().size() == 0) item.addLore(ComponentUtil.translate(ChatColor.GRAY, "gui.none"));
        return item;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem date() {
        InventoryItem item = new InventoryItem(Material.PAPER).addTag("ItemTag", "gui.mail.viewer.date");
        item.setDisplayName(ComponentUtil.component(ChatColor.GREEN, ComponentUtil.translate("gui.mail.date"), Component.text(ChatColor.GREEN + ": " + mail.getDate())));
        return item;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem delete() {
        InventoryItem item = new InventoryItem(Material.RED_CONCRETE).addTag("ItemTag", "gui.mail.viewer.delete");
        item.setDisplayName(Component.translatable("gui.mail.delete"));
        return item;
    }

    public Mail getMail() {
        return this.mail;
    }
}
