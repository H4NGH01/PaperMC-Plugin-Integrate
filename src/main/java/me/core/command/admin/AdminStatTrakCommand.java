package me.core.command.admin;

import me.core.command.PluginCommand;
import me.core.item.StatTrak;
import me.core.util.ComponentUtil;
import me.core.util.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class AdminStatTrakCommand extends PluginCommand {

    private final String[] s = new String[]{"a-stattrak", "a-st"};

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("Usages:");
            player.sendMessage("/admin-stattrak give");
            player.sendMessage("/admin-stattrak take");
            return;
        }
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack.getType().equals(Material.AIR)) {
            player.sendMessage(ChatColor.RED + "You need a item in hand to apply StatTrak™");
            return;
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (StatTrak.isStattrak(stack)) {
                player.sendMessage(ChatColor.RED + "This item has already applied StatTrak™");
                return;
            }
            StatTrak statTrak = new StatTrak(stack);
            player.getInventory().setItemInMainHand(statTrak);
            player.sendMessage(ChatColor.YELLOW + "Success applied StatTrak™ on item");
            return;
        }
        if (args[0].equalsIgnoreCase("take")) {
            if (!StatTrak.isStattrak(stack)) {
                player.sendMessage(ChatColor.RED + "This item has not applied StatTrak™");
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
            player.sendMessage(ChatColor.YELLOW + "Success remove StatTrak™ on item");
            return;
        }
        player.sendMessage(ComponentUtil.translate(org.bukkit.ChatColor.RED, "command.unknown.argument"));
        player.sendMessage("Usages:");
        player.sendMessage("/admin-stattrak give");
        player.sendMessage("/admin-stattrak take");
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
        return s;
    }
}
