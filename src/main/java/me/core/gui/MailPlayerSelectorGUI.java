package me.core.gui;

import me.core.item.InventoryItem;
import me.core.item.MCServerItems;
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
            stacks.add(playerIcon(p2, MailWriterGUI.VIEW_MAP.get(this.getPlayer()).getMail().containAddressee(p2)));
        }
        stacks.sort(Comparator.comparing(o -> Objects.requireNonNull(((SkullMeta) o.getItemMeta()).getOwningPlayer()).getName()));
        this.setContents(stacks);
        this.toArray(VIEW_MAP.containsKey(this.getPlayer()) ? VIEW_MAP.get(this.getPlayer()).getPage() : 1);
        VIEW_MAP.put(this.getPlayer(), this);

        this.inventory.setItem(0, info());
        this.inventory.setItem(48, MCServerItems.back);
        this.inventory.setItem(45, MCServerItems.prev);
        this.inventory.setItem(53, MCServerItems.next);

        this.updateGUIName();
    }

    @Override
    public String getGUIName() {
        return "gui.mail.writer.player_selector";
    }

    public static @NotNull InventoryItem playerIcon(@NotNull OfflinePlayer p, boolean glow) {
        InventoryItem icon = new InventoryItem(Material.PLAYER_HEAD, Component.text((glow ? ChatColor.YELLOW : ChatColor.GRAY) + p.getName()), Component.text(ChatColor.GRAY + "UUID: " + p.getUniqueId())).addTag("ItemTag", "gui.mail.selector.player_icon");
        SkullMeta ms = (SkullMeta) icon.getItemMeta();
        ms.setOwningPlayer(p);
        icon.setItemMeta(ms);
        return icon;
    }

    private @NotNull InventoryItem info() {
        return new InventoryItem(Material.PAPER, Component.translatable("gui.mail.writer.player_selector.info")).addTag("ItemTag", "gui.mail.selector.info");
    }
}
