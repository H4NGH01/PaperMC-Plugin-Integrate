package me.core.commands.admin;

import me.core.commands.PluginCommand;
import me.core.items.StatTrak;
import me.core.utils.ComponentUtil;
import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
            player.sendMessage(Component.translatable("command.stattrak.invalid_item"));
            return;
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (StatTrak.isStattrak(stack)) {
                player.sendMessage(Component.translatable("command.stattrak.item_has_stattrak"));
                return;
            }
            StatTrak statTrak = new StatTrak(stack);
            player.getInventory().setItemInMainHand(statTrak);
            player.sendMessage(Component.translatable("command.stattrak.added"));
            return;
        }
        if (args[0].equalsIgnoreCase("take")) {
            if (!StatTrak.isStattrak(stack)) {
                player.sendMessage(Component.translatable("command.stattrak.item_no_stattrak"));
                return;
            }
            ItemMeta meta = stack.getItemMeta();
            boolean b = NBTHelper.getTag(stack).l("CustomName").equals(stack.translationKey());
            meta.displayName(b ? null : Component.text(NBTHelper.getTag(stack).l("CustomName")));
            List<Component> components = meta.lore();
            assert components != null;
            components.remove(0);
            meta.lore(components);
            stack.setItemMeta(meta);
            NBTHelper.removeTag(stack, "CustomName");
            NBTHelper.removeTag(stack, "stattrak");
            if (components.size() == 0 && !meta.hasDisplayName()) NBTHelper.removeTag(stack, "display");
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
