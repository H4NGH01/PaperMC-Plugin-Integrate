package me.core.guis.mails;

import me.core.guis.GUIBase;
import me.core.guis.MultiplePageGUI;
import me.core.items.InventoryItem;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MailPlayerSelectorGUI extends MultiplePageGUI {

    private static final HashMap<Player, MailPlayerSelectorGUI> VIEW_MAP = new HashMap<>();

    public MailPlayerSelectorGUI(Player player) {
        super(player);
        this.setDefault();
    }

    @Override
    public void setInventory() {
        List<ItemStack> stacks = new ArrayList<>();
        for (OfflinePlayer p2 : plugin.getServer().getOfflinePlayers()) {
            stacks.add(playerIcon(p2));
        }
        stacks.sort(Comparator.comparing(o -> Objects.requireNonNull(((SkullMeta) o.getItemMeta()).getOwningPlayer()).getName()));
        this.setContents(stacks);
        this.toArray(VIEW_MAP.containsKey(this.getPlayer()) ? VIEW_MAP.get(this.getPlayer()).getPage() : 1);
        VIEW_MAP.put(this.getPlayer(), this);
        this.inventory.setItem(0, info());
    }

    @Override
    public Component getGUIName() {
        return Component.translatable("gui.mail.writer.player_selector");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends GUIBase> HashMap<Player, T> getViewMap() {
        return (HashMap<Player, T>) VIEW_MAP;
    }

    public static HashMap<Player, MailPlayerSelectorGUI> getViews() {
        return VIEW_MAP;
    }

    public @NotNull InventoryItem playerIcon(@NotNull OfflinePlayer p) {
        InventoryItem icon = new InventoryItem(Material.PLAYER_HEAD).setTag("ItemTag", "gui.mail.selector.player_icon");
        boolean b = MailWriterGUI.NEW_MAP_MAP.get(this.getPlayer()).containAddressee(p);
        icon.setDisplayName(Component.text((b ? ChatColor.YELLOW : ChatColor.GRAY) + p.getName()));
        icon.addLore(Component.text(ChatColor.GRAY + "UUID: " + p.getUniqueId()));
        SkullMeta ms = (SkullMeta) icon.getItemMeta();
        ms.setOwningPlayer(p);
        icon.setItemMeta(ms);
        icon.addLore(Component.translatable(b ? "gui.mail.unselect" : "gui.mail.select"));
        return icon;
    }

    private @NotNull InventoryItem info() {
        InventoryItem item = new InventoryItem(Material.PAPER).setTag("ItemTag", "gui.mail.selector.info");
        item.setDisplayName(Component.translatable("gui.mail.writer.player_selector.info"));
        return item;
    }
}
