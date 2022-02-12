package me.core.gui.market;

import me.core.ServerPlayer;
import me.core.containers.Container;
import me.core.containers.ContainerKey;
import me.core.containers.ContainerManager;
import me.core.containers.ContainerType;
import me.core.gui.GUIBase;
import me.core.gui.MultiplePageGUI;
import me.core.gui.Updatable;
import me.core.items.CaseKeyStack;
import me.core.items.ContainerItemStack;
import me.core.items.InventoryItem;
import me.core.utils.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ContainerMarketGUI extends MultiplePageGUI implements MarketGUIInterface, Updatable {

    private static final HashMap<Player, ContainerMarketGUI> VIEW_MAP = new HashMap<>();

    public ContainerMarketGUI(@NotNull Player player) {
        super(player);
        this.setDefault();
    }

    @Override
    public void setInventory() {
        List<ItemStack> stacks = new ArrayList<>();
        for (ContainerType type : ContainerType.values()) {
            Container c = ContainerManager.getContainerByType(type);
            InventoryItem stack = new InventoryItem(c.getContainerTexture()).setTag("price", "10").setTag("ContainerType", type.getID());
            stack.setDisplayName(Component.translatable(c.getContainerType().getTranslationKey()));
            stack.addLore(ComponentUtil.translate(NamedTextColor.GRAY, "gui.container.require_key").args(ComponentUtil.translate(NamedTextColor.YELLOW, c.getKeyType().getTranslationKey())));
            stack.addLore(ComponentUtil.translate(NamedTextColor.GRAY, "gui.container.contains").color(NamedTextColor.GRAY));
            for (ContainerItemStack drops : c.getDisplayDrops()) {
                stack.addLore(Objects.requireNonNull(drops.getItemMeta().displayName()).decoration(TextDecoration.ITALIC, false));
            }
            stack.addLore(ComponentUtil.translate("gui.market.price").args(Component.text("$10").color(NamedTextColor.YELLOW)));
            stack.addLore(ComponentUtil.translate("gui.market.buy"));
            stack.addLore(ComponentUtil.translate("gui.market.buy_multi"));
            stacks.add(stack);
        }
        for (ContainerKey key : ContainerKey.values()) {
            CaseKeyStack stack = (CaseKeyStack) new CaseKeyStack(key).setTag("price", "20");
            stack.addLore(ComponentUtil.translate("gui.market.price").args(Component.text("$20").color(NamedTextColor.YELLOW)));
            stack.addLore(ComponentUtil.translate("gui.market.buy"));
            stack.addLore(ComponentUtil.translate("gui.market.buy_multi"));
            stacks.add(stack);
        }
        this.setContents(stacks);
        this.toArray(VIEW_MAP.containsKey(this.getPlayer()) ? VIEW_MAP.get(this.player).getPage() : 1);
        this.inventory.setItem(0, money());
    }

    @Override
    public Component getGUIName() {
        return Component.translatable("gui.market.container");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends GUIBase> HashMap<Player, T> getViewMap() {
        return (HashMap<Player, T>) VIEW_MAP;
    }

    public static HashMap<Player, ContainerMarketGUI> getViews() {
        return VIEW_MAP;
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
