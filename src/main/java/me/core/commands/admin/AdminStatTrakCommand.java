package me.core.commands.admin;

import me.core.commands.PluginCommand;
import me.core.items.StatTrak;
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

public class AdminStatTrakCommand extends PluginCommand {

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(Component.translatable("command.usages"));
            player.sendMessage("/admin-stattrak give");
            player.sendMessage("/admin-stattrak take");
            player.sendMessage("/admin-stattrak reset");
            return;
        }
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack.getType().equals(Material.AIR)) {
            player.sendMessage(Component.translatable("command.invalid_item"));
            return;
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (StatTrak.isStattrak(stack)) {
                player.sendMessage(Component.translatable("command.stattrak.item_has_stattrak"));
                return;
            }
            StatTrak.addStatTrak(stack);
            player.sendMessage(Component.translatable("command.stattrak.added"));
            return;
        }
        if (args[0].equalsIgnoreCase("take")) {
            if (!StatTrak.isStattrak(stack)) {
                player.sendMessage(Component.translatable("command.stattrak.item_no_stattrak"));
                return;
            }
            StatTrak.removeStatTrak(stack);
            player.sendMessage(Component.translatable("command.stattrak.removed"));
            return;
        }
        if (args[0].equalsIgnoreCase("reset")) {
            if (!StatTrak.isStattrak(stack)) {
                player.sendMessage(Component.translatable("command.stattrak.item_no_stattrak"));
                return;
            }
            StatTrak.resetKills(stack);
            player.sendMessage(Component.translatable("command.stattrak.reset"));
            return;
        }
        player.sendMessage(ComponentUtil.translate(NamedTextColor.RED, "command.unknown.argument"));
        player.sendMessage(Component.translatable("command.usages"));
        player.sendMessage("/admin-stattrak give");
        player.sendMessage("/admin-stattrak take");
        player.sendMessage("/admin-stattrak reset");
    }

    @Override
    public String name() {
        return "admin-stattrak";
    }

    @Override
    public String info() {
        return "Admin StatTrak commands";
    }

    @Override
    public String[] aliases() {
        return new String[]{"a-stattrak", "a-st"};
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.add("give");
            list.add("take");
            list.add("reset");
        }
        return list;
    }
}
