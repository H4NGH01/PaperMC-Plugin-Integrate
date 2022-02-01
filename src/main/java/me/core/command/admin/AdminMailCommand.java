package me.core.command.admin;

import me.core.command.PluginCommand;
import me.core.mail.Mail;
import me.core.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminMailCommand extends PluginCommand {

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(Component.translatable("command.usages"));
            player.sendMessage("/admin-mail list");
            player.sendMessage("/admin-mail clear");
            return;
        }
        if (args[0].equalsIgnoreCase("list")) {
            int page = 1;
            if (args.length > 1) {
                try {
                    page = Integer.parseInt(args[1]);
                } catch (Exception ignored) {
                    player.sendMessage(Component.translatable("command.nan").args(Component.text(args[1])).color(ComponentUtil.convertTextColor(org.bukkit.ChatColor.RED)));
                    return;
                }
            }
            player.sendMessage(Component.translatable("command.total_mail_count").args(Component.text(plugin.getMailManager().getMailList().size())));
            if (plugin.getMailManager().getMailList().size() > 10) player.sendMessage(Component.translatable("command.page").args(Component.text(page)));
            for (int i = (page - 1) * 10; i < plugin.getMailManager().getMailList().size(); i++) {
                Mail mail = plugin.getMailManager().getMailList().get(i);
                TextComponent textComponent = new TextComponent(ChatColor.GRAY + "=====================" + ChatColor.YELLOW + (i + 1) + ChatColor.GRAY + "=====================\n");
                textComponent.addExtra(ChatColor.GRAY + "Mail ID: " + ChatColor.YELLOW + mail.getMailID() + "\n");
                textComponent.addExtra(ChatColor.GRAY + "Sender: " + ChatColor.YELLOW + (mail.getSender().startsWith("player@") ? plugin.getServer().getOfflinePlayer(UUID.fromString(mail.getSender().substring(7))).getName() : mail.getSender()) + "\n");
                textComponent.addExtra(ChatColor.GRAY + "Addressee: " + ChatColor.YELLOW + plugin.getServer().getOfflinePlayer(mail.getAddressee()).getName() + "\n");
                textComponent.addExtra(ChatColor.GRAY + "Title: ");
                TranslatableComponent tc = new TranslatableComponent(mail.getTitle());
                tc.setColor(ChatColor.YELLOW);
                textComponent.addExtra(tc);
                textComponent.addExtra("\n" + ChatColor.GRAY + "Text: ");
                TextComponent text = new TextComponent(ChatColor.GRAY + "[...]");
                ComponentBuilder cb = new ComponentBuilder();
                if (mail.getText().equals("gui.mail.no_text")) {
                    cb.append(new TranslatableComponent(mail.getText()));
                } else {
                    String[] sa = (mail.getText()).split("\\\\n");
                    for (String s : sa) {
                        cb.append("§7" + s);
                    }
                }
                text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, cb.create()));
                textComponent.addExtra(text);
                textComponent.addExtra("\n");
                textComponent.addExtra(ChatColor.GRAY + "Attachment: [");
                for (int j = 0; j < mail.getItemList().size(); j++) {
                    ItemStack stack = mail.getItemList().get(j);
                    TextComponent extra = new TextComponent(stack.getI18NDisplayName());
                    ComponentBuilder cb1 = new ComponentBuilder();
                    cb1.append(CraftItemStack.asNMSCopy(stack).b(new NBTTagCompound()).toString());
                    extra.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, cb1.create()));
                    textComponent.addExtra(extra);
                    if (j < mail.getItemList().size() - 1) textComponent.addExtra(", ");
                }
                textComponent.addExtra(ChatColor.GRAY + "]\n");
                textComponent.addExtra(ChatColor.GRAY + "Date: " + ChatColor.YELLOW + mail.getDate() + "\n");
                textComponent.addExtra(ChatColor.GRAY + "Received: " + (mail.isReceived() ? ChatColor.YELLOW + "Yes" : ChatColor.RED + "No"));
                player.sendMessage(textComponent);
                //
            }
            return;
        }
        if (args[0].equalsIgnoreCase("clear")) {
            plugin.getMailManager().getMailList().clear();
            player.sendMessage(Component.translatable("command.cleared_all_mail"));
            return;
        }
        player.sendMessage(ComponentUtil.translate(org.bukkit.ChatColor.RED, "command.unknown.argument"));
        player.sendMessage(Component.translatable("command.usages"));
        player.sendMessage("/admin-mail list");
        player.sendMessage("/admin-mail clear");
    }

    @Override
    public String name() {
        return "admin-mail";
    }

    @Override
    public String info() {
        return "Manage mail commands";
    }

    @Override
    public String[] aliases() {
        return new String[]{"a-mail"};
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.add("list");
            list.add("clear");
        }
        return list;
    }
}
