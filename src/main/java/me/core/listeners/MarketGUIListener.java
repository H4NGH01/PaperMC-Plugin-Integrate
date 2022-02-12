package me.core.listeners;

import me.core.ServerPlayer;
import me.core.containers.*;
import me.core.economies.Economy;
import me.core.events.GUIClickEvent;
import me.core.gui.market.ContainerMarketGUI;
import me.core.gui.market.MarketMultiBuyGUI;
import me.core.items.CaseKeyStack;
import me.core.items.CaseStack;
import me.core.utils.ComponentUtil;
import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class MarketGUIListener {

    public void onClick(@NotNull GUIClickEvent e) {
        if (e.getGUI() instanceof ContainerMarketGUI) {
            this.onMarketClick(e);
            return;
        }
        if (e.getGUI() instanceof MarketMultiBuyGUI) {
            this.onMultiBuyClick(e);
        }
    }

    public void onMarketClick(@NotNull GUIClickEvent e) {
        e.setCancelled(true);
        ItemStack item = e.getCurrentItem();
        if (item == null || !item.hasItemMeta() || !NBTHelper.hasTag(item, "price")) return;
        ContainerMarketGUI gui = (ContainerMarketGUI) e.getGUI();
        Player p = e.getPlayer();
        if (e.isLeftClick()) {
            if (buy(p, item, 1)) gui.update();
        }
        if (e.isRightClick()) {
            MarketMultiBuyGUI mmg = new MarketMultiBuyGUI(p, item);
            mmg.setLastInventory(gui);
            mmg.openToPlayer();
        }
    }

    public void onMultiBuyClick(@NotNull GUIClickEvent e) {
        e.setCancelled(true);
        ItemStack item = e.getCurrentItem();
        if (item == null || !item.hasItemMeta() || !NBTHelper.hasTag(item, "BuyCount")) return;
        MarketMultiBuyGUI gui = (MarketMultiBuyGUI) e.getGUI();
        Player p = e.getPlayer();
        if (e.isRightClick() && NBTHelper.hasTag(item, "BuyCustom")) {
            ServerPlayer.getServerPlayer(p).setHoldingGUI(gui);
            p.closeInventory();
            ServerChatBarListener.CHAT_MAP.put(p, "chat.market.buy_custom_count");
            p.sendMessage(Component.translatable("chat.market.buy_count_type"));
            return;
        }
        int count = NBTHelper.getTag(item).h("BuyCount");
        if (buy(p, gui.getBuyItem(), count)) gui.update();
    }

    private boolean buy(Player player, ItemStack buy, int count) {
        ServerPlayer sp = ServerPlayer.getServerPlayer(player);
        BigDecimal price = new BigDecimal(NBTHelper.getTag(buy).l("price")).multiply(new BigDecimal(count));
        BigDecimal money = sp.getMoney();
        int j = 0;
        for (ItemStack stack : player.getInventory().getStorageContents()) {
            if (stack == null || stack.getType().equals(Material.AIR)) j++;
        }
        if (money.compareTo(price) >= 0) {
            if (player.getInventory().firstEmpty() == -1 || (NBTHelper.hasTag(buy, "ContainerType") && count > j) || count > j * 64) {
                player.sendMessage(Component.translatable("chat.market.buy_failure.inventory_full"));
                return false;
            }
            Economy.setMoney(player, money.subtract(price));
            ItemStack stack = null;
            for (int i = 0; i < count; i++) {
                if (NBTHelper.hasTag(buy, "ContainerType")) {
                    Container container = ContainerManager.getContainerByType(ContainerType.byID(NBTHelper.getTag(buy).l("ContainerType")));
                    stack = new CaseStack(container, new ContainerData(container.getContainerType(), container.generateDrop()));
                } else if (NBTHelper.hasTag(buy, "ContainerKey")) {
                    stack = new CaseKeyStack(ContainerKey.getByID(NBTHelper.getTag(buy).l("ContainerKey")));
                } else {
                    throw new IllegalArgumentException("Unknown item");
                }
                player.getInventory().addItem(stack);
            }
            assert stack != null;
            player.sendMessage(Component.translatable("chat.market.buy_success").args(stack.displayName().append(Component.text(" * " + count)), ComponentUtil.text(NamedTextColor.YELLOW, price.toString())));
            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.7f, 1f);
            return true;
        } else {
            player.sendMessage(Component.translatable("chat.market.buy_failure.not_enough"));
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.7f, 0f);
        }
        return false;
    }

}
