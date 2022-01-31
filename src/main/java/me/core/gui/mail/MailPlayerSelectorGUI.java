package me.core.gui.mail;

import me.core.gui.MultiplePageGUI;
import me.core.item.InventoryItem;
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

    public static HashMap<Player, MailPlayerSelectorGUI> VIEW_MAP = new HashMap<>();

    public MailPlayerSelectorGUI(Player player) {
        super(player);
        VIEW_MAP.put(player, this);
    }

    public static void setup() {
        VIEW_MAP = new HashMap<>();
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
        this.updateGUIName();
    }

    @Override
    public Component getGUIName() {
        return Component.translatable("gui.mail.writer.player_selector");
    }

    public @NotNull InventoryItem playerIcon(@NotNull OfflinePlayer p) {
        InventoryItem icon = new InventoryItem(Material.PLAYER_HEAD).setTag("ItemTag", "gui.mail.selector.player_icon");
        boolean b = MailWriterGUI.VIEW_MAP.get(this.getPlayer()).getMail().containAddressee(p);
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
