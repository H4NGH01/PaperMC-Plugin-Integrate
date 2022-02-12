package me.core.gui.market;

import me.core.ServerPlayer;
import me.core.gui.GUIBase;
import me.core.gui.Updatable;
import me.core.items.InventoryItem;
import me.core.utils.ComponentUtil;
import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Objects;

public class MarketMultiBuyGUI extends GUIBase implements MarketGUIInterface, Updatable {

    private static final HashMap<Player, MarketMultiBuyGUI> VIEW_MAP = new HashMap<>();

    private final ItemStack buyItem;
    private final BigDecimal price;
    private int custom = 1;

    public MarketMultiBuyGUI(@NotNull Player player, ItemStack buyItem) {
        super(player, 45);
        this.buyItem = buyItem;
        this.price = new BigDecimal(NBTHelper.getTag(this.buyItem).l("price"));
        setDefault();
    }

    @Override
    public void setInventory() {
        this.inventory.setItem(0, money());
        this.inventory.setItem(20, buyOne());
        this.inventory.setItem(22, buyStack());
        this.inventory.setItem(24, buyCustom(custom));
    }

    @Override
    public Component getGUIName() {
        return Component.translatable("gui.market.multi_buy").append(Component.text(": ")).append(this.buyItem.hasItemMeta() && this.buyItem.getItemMeta().hasDisplayName() ? Objects.requireNonNull(this.buyItem.getItemMeta().displayName()) : this.buyItem.displayName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends GUIBase> HashMap<Player, T> getViewMap() {
        return (HashMap<Player, T>) VIEW_MAP;
    }

    public static HashMap<Player, MarketMultiBuyGUI> getViews() {
        return VIEW_MAP;
    }

    private @NotNull InventoryItem buyOne() {
        InventoryItem item = new InventoryItem(this.buyItem.getType()).setTag("BuyCount", 1);
        item.setDisplayName(Component.translatable("gui.market.buy_one"));
        item.addLore(Component.translatable("gui.market.buy_cost").args(ComponentUtil.text(NamedTextColor.YELLOW, this.getPrice().toString())));
        item.addLore(Component.translatable("gui.market.buy"));
        return item;
    }

    private @NotNull InventoryItem buyStack() {
        InventoryItem item = new InventoryItem(this.buyItem.getType(), 64).setTag("BuyCount", 64);
        item.setDisplayName(Component.translatable("gui.market.buy_stack"));
        item.addLore(Component.translatable("gui.market.buy_cost").args(ComponentUtil.text(NamedTextColor.YELLOW, this.getPrice().multiply(new BigDecimal(64)).toString())));
        item.addLore(Component.translatable("gui.market.buy"));
        return item;
    }

    private @NotNull InventoryItem buyCustom(int amount) {
        InventoryItem item = new InventoryItem(this.buyItem.getType(), amount).setTag("BuyCount", amount).setTag("BuyCustom", true);
        item.setDisplayName(Component.translatable("gui.market.buy_custom").args(Component.text(amount)));
        item.addLore(Component.translatable("gui.market.buy_cost").args(ComponentUtil.text(NamedTextColor.YELLOW, this.getPrice().multiply(new BigDecimal(custom)).toString())));
        item.addLore(Component.translatable("gui.market.buy"));
        item.addLore(Component.translatable("gui.market.buy_custom_lore"));
        return item;
    }

    public ItemStack getBuyItem() {
        return this.buyItem;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setCustom(int i) {
        this.custom = i;
    }

    private @NotNull InventoryItem money() {
        InventoryItem item = new InventoryItem(Material.EMERALD);
        item.setDisplayName(Component.translatable("gui.balance").args(ComponentUtil.text(NamedTextColor.YELLOW, "$" + ServerPlayer.getServerPlayer(this.player).getMoney())));
        return item;
    }

    @Override
    public void update() {
        this.inventory.setItem(0, money());
    }
}
