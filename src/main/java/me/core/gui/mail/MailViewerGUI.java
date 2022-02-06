package me.core.gui.mail;

import me.core.gui.GUIBase;
import me.core.items.InventoryItem;
import me.core.mail.Mail;
import me.core.utils.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class MailViewerGUI extends GUIBase {

    private static final HashMap<Player, MailViewerGUI> VIEW_MAP = new HashMap<>();
    private final Mail mail;
    private final ViewType type;

    public MailViewerGUI(@NotNull Player player, @NotNull Mail mail, @NotNull ViewType type) {
        super(player);
        this.mail = mail;
        this.type = type;
        this.setDefault();
    }

    @Override
    public void setInventory() {
        switch (this.type) {
            case ADMIN:
                this.inventory.setItem(9, from());
                this.inventory.setItem(10, to());
                break;
            case SENDER:
                this.inventory.setItem(9, to());
                break;
            case ADDRESSEE:
                this.inventory.setItem(9, from());
                break;
            default:
                break;
        }
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
    }

    @Override
    public Component getGUIName() {
        return Component.translatable("gui.mail.viewer").append(Component.text(this.type.equals(ViewType.ADMIN) ? mail.getMailID() : mail.getTitle()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends GUIBase> HashMap<Player, T> getViewMap() {
        return (HashMap<Player, T>) VIEW_MAP;
    }

    public static HashMap<Player, MailViewerGUI> getViews() {
        return VIEW_MAP;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem from() {
        String sender = mail.getSender().startsWith("player@") ? plugin.getServer().getOfflinePlayer(UUID.fromString(mail.getSender().substring(7))).getName() : mail.getSender();
        InventoryItem item = new InventoryItem(Material.PLAYER_HEAD).setTag("ItemTag", "gui.mail.viewer.from");
        item.setDisplayName(ComponentUtil.translate(NamedTextColor.GREEN, "gui.mail.from"));
        item.addLore(Component.text(ChatColor.YELLOW + Objects.requireNonNull(sender)));
        return item;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem to() {
        InventoryItem item = new InventoryItem(Material.PLAYER_HEAD).setTag("ItemTag", "gui.mail.viewer.to");
        item.setDisplayName(ComponentUtil.translate(NamedTextColor.GREEN, "gui.mail.to"));
        item.addLore(Component.text(ChatColor.YELLOW + Objects.requireNonNull(plugin.getServer().getOfflinePlayer(mail.getAddressee()).getName())));
        return item;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem title() {
        InventoryItem item = new InventoryItem(Material.PAPER).setTag("ItemTag", "gui.mail.viewer.title");
        item.setDisplayName(ComponentUtil.translate(NamedTextColor.GREEN, "gui.mail.title"));
        item.setLore(ComponentUtil.translate(NamedTextColor.YELLOW, mail.getTitle()));
        return item;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem text() {
        InventoryItem item = new InventoryItem(Material.PAPER).setTag("ItemTag", "gui.mail.viewer.content");
        item.setDisplayName(ComponentUtil.translate(NamedTextColor.GREEN, "gui.mail.text"));
        if (this.mail.getText().equals("gui.mail.no_text")) {
            item.addLore(ComponentUtil.translate(NamedTextColor.GRAY, mail.getText()));
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
        InventoryItem item = new InventoryItem(Material.PAPER).setTag("ItemTag", "gui.mail.viewer.attachment");
        item.setDisplayName(ComponentUtil.translate(NamedTextColor.GREEN, "gui.mail.attachment"));
        if (mail.getItemList().size() == 0) item.addLore(ComponentUtil.translate(NamedTextColor.GRAY, "gui.none"));
        return item;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem date() {
        InventoryItem item = new InventoryItem(Material.PAPER).setTag("ItemTag", "gui.mail.viewer.date");
        item.setDisplayName(ComponentUtil.component(NamedTextColor.GREEN, ComponentUtil.translate("gui.mail.date"), Component.text(ChatColor.GREEN + ": " + mail.getDate())));
        return item;
    }

    @Contract(" -> new")
    private @NotNull InventoryItem delete() {
        InventoryItem item = new InventoryItem(Material.RED_CONCRETE).setTag("ItemTag", "gui.mail.viewer.delete");
        item.setDisplayName(Component.translatable("gui.mail.delete"));
        return item;
    }

    public Mail getMail() {
        return this.mail;
    }
}
