package me.core.commands.admin;

import me.core.commands.PluginCommand;
import me.core.containers.Container;
import me.core.containers.ContainerData;
import me.core.containers.ContainerKey;
import me.core.containers.ContainerType;
import me.core.items.CaseItemRarity;
import me.core.items.CaseKeyStack;
import me.core.items.CaseStack;
import me.core.items.ContainerItemStack;
import me.core.utils.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdminContainerCommand extends PluginCommand {

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(Component.translatable("command.usages"));
            player.sendMessage("/admin-container list");
            player.sendMessage("/admin-container give");
            player.sendMessage("/admin-container seek");
            player.sendMessage("/admin-container setdrop");
            return;
        }
        if (args[0].equalsIgnoreCase("list")) {
            player.sendMessage(Component.translatable("command.container.list"));
            for (ContainerType type : ContainerType.values()) {
                player.sendMessage(Component.translatable(type.getTranslationKey()).append(Component.text(" id:\"" + type.getID() + "\"")));
            }
            player.sendMessage(Component.translatable("command.container.key_list"));
            for (ContainerKey key : ContainerKey.values()) {
                player.sendMessage(Component.translatable(key.getTranslationKey()).append(Component.text(" id:\"" + key.getID() + "\"")));
            }
            return;
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (args.length == 1) {
                player.sendMessage(ComponentUtil.translate(NamedTextColor.RED, "command.unknown.argument"));
                return;
            }
            String id = args[1];
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(Component.translatable("command.inventory_full"));
                return;
            }
            if (id.equalsIgnoreCase("random_case")) {
                CaseStack stack = plugin.getContainerManager().generateRandomCaseStack();
                player.getInventory().addItem(stack);
                player.sendMessage(Component.translatable("command.container.gave").args(Component.translatable(stack.getCaseType().getTranslationKey())));
                ItemStack drop = plugin.getContainerManager().getContainerByStack(stack).getDrop();
                Component item = drop.displayName();
                item.hoverEvent(drop.asHoverEvent());
                player.sendMessage(Component.translatable("command.container.seek_drop").args(item));
                return;
            }
            for (ContainerType type : ContainerType.values()) {
                if (type.getID().equalsIgnoreCase(id)) {
                    CaseStack stack = new CaseStack(plugin.getContainerManager().getContainerByType(type));
                    player.getInventory().addItem(stack);
                    player.sendMessage(Component.translatable("command.container.gave").args(Component.translatable(stack.getCaseType().getTranslationKey())));
                    ItemStack drop = plugin.getContainerManager().getContainerByStack(stack).getDrop();
                    Component item = drop.displayName();
                    item.hoverEvent(drop.asHoverEvent());
                    player.sendMessage(Component.translatable("command.container.seek_drop").args(item));
                    return;
                }
            }
            for (ContainerKey key : ContainerKey.values()) {
                if (key.getID().equalsIgnoreCase(id)) {
                    CaseKeyStack stack = new CaseKeyStack(key);
                    player.getInventory().addItem(stack);
                    player.sendMessage(Component.translatable("command.container.gave").args(Component.translatable(stack.getKeyType().getTranslationKey())));
                    return;
                }
            }
            player.sendMessage(Component.translatable("argument.item.id.invalid").color(NamedTextColor.RED).args(Component.text(id).color(NamedTextColor.RED)));
            return;
        }
        if (args[0].equalsIgnoreCase("seek")) {
            ItemStack stack = player.getInventory().getItemInMainHand();
            if (stack.getType().equals(Material.AIR)) {
                player.sendMessage(Component.translatable("command.invalid_item"));
                return;
            }
            if (!Container.isContainerStack(stack)) {
                player.sendMessage(Component.translatable("command.container.invalid_container"));
                return;
            }
            ItemStack drop = plugin.getContainerManager().getContainerByStack(stack).getDrop();
            Component item = drop.displayName();
            item.hoverEvent(drop.asHoverEvent());
            player.sendMessage(Component.translatable("command.container.seek_drop").args(item));
            return;
        }
        if (args[0].equalsIgnoreCase("setdrop")) {
            ItemStack stack = player.getInventory().getItemInMainHand();
            if (stack.getType().equals(Material.AIR)) {
                player.sendMessage(Component.translatable("command.invalid_item"));
                return;
            }
            if (!Container.isContainerStack(stack)) {
                player.sendMessage(Component.translatable("command.container.invalid_container"));
                return;
            }
            Container container = plugin.getContainerManager().getContainerByStack(stack);
            if (args.length == 1) {
                player.sendMessage(ComponentUtil.translate(NamedTextColor.RED, "command.unknown.argument"));
                return;
            }
            if (getItemFromString(container, args[1]) == null) {
                player.sendMessage(Component.translatable("argument.item.id.invalid").color(NamedTextColor.RED).args(Component.text(args[1]).color(NamedTextColor.RED)));
                return;
            }
            ContainerData data = plugin.getContainerManager().getContainerByStack(stack).getData();
            ContainerItemStack item = getItemFromString(container, args[1]);
            assert item != null;
            data.setDrop(item);
            player.sendMessage(Component.text("Drop set ").append(item.getDisplayName()));
            return;
        }
        player.sendMessage(ComponentUtil.translate(NamedTextColor.RED, "command.unknown.argument"));
        player.sendMessage(Component.translatable("command.usages"));
        player.sendMessage("/admin-container list");
        player.sendMessage("/admin-container give");
        player.sendMessage("/admin-container seek");
        player.sendMessage("/admin-container setdrop");
    }

    private ContainerItemStack getItemFromString(Container container, String str) {
        ContainerItemStack[] drops = container.getContainerDrops();
        boolean rare = false;
        if (str.toLowerCase().startsWith("RARE_".toLowerCase())) {
            str = str.substring(5);
            rare = true;
        }
        for (ContainerItemStack item : drops) {
            if (rare && !item.getItemRarity().equals(CaseItemRarity.RARE_SPECIAL)) continue;
            if (item.getType().name().equalsIgnoreCase(str)) return item;
        }
        return null;
    }

    @Override
    public String name() {
        return "admin-container";
    }

    @Override
    public String info() {
        return "Admin container commands";
    }

    @Override
    public String[] aliases() {
        return new String[]{"a-container", "a-case"};
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.add("list");
            list.add("give");
            list.add("seek");
            list.add("setdrop");
        }
        if (args.length > 1 & args[0].equalsIgnoreCase("give")) {
            for (ContainerType type : ContainerType.values()) {
                list.add(type.getID());
            }
            for (ContainerKey key : ContainerKey.values()) {
                list.add(key.getID());
            }
            list.add("random_case");
        }
        if (args.length > 1 & args[0].equalsIgnoreCase("setdrop")) {
            if (!(sender instanceof Player player)) return list;
            ItemStack stack = player.getInventory().getItemInMainHand();
            if (stack.getType().equals(Material.AIR) || !Container.isContainerStack(stack)) return list;
            Container container = plugin.getContainerManager().getContainerByStack(stack);
            ContainerItemStack[] drops = container.getContainerDrops();
            for (ContainerItemStack item : drops) {
                list.add(item.getItemRarity().equals(CaseItemRarity.RARE_SPECIAL) ? "RARE_" + item.getType().name() : item.getType().name());
            }
        }
        return list;
    }
}
