package me.core.commands.admin;

import me.core.commands.PluginCommand;
import me.core.containers.Container;
import me.core.containers.ContainerKey;
import me.core.containers.ContainerType;
import me.core.items.CaseKeyStack;
import me.core.items.CaseStack;
import me.core.utils.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
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
                player.sendMessage(Component.translatable("argument.item.id.invalid"));
                return;
            }
            String id = args[1];
            if (id.equalsIgnoreCase("random_case")) {
                CaseStack stack = plugin.getContainerManager().generateRandomCaseStack();
                player.getInventory().addItem(stack);
                player.sendMessage(Component.translatable("command.container.gave").args(Component.translatable(stack.getCaseType().getTranslationKey())));
                return;
            }
            for (ContainerType type : ContainerType.values()) {
                if (type.getID().equalsIgnoreCase(id)) {
                    CaseStack stack = new CaseStack(plugin.getContainerManager().getContainerByType(type));
                    player.getInventory().addItem(stack);
                    player.sendMessage(Component.translatable("command.container.gave").args(Component.translatable(stack.getCaseType().getTranslationKey())));
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
            TranslatableComponent component = Component.translatable("command.container.seek_drop").args(item);
            player.sendMessage(component);
            return;
        }
        player.sendMessage(ComponentUtil.translate(NamedTextColor.RED, "command.unknown.argument"));
        player.sendMessage(Component.translatable("command.usages"));
        player.sendMessage("/admin-container list");
        player.sendMessage("/admin-container give");
        player.sendMessage("/admin-container seek");
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
        return list;
    }
}
