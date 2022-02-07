package me.core.commands.admin;

import me.core.commands.PluginCommand;
import me.core.gui.mail.MailViewerGUI;
import me.core.gui.mail.ViewType;
import me.core.mail.Mail;
import me.core.mail.MailManager;
import me.core.utils.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
            player.sendMessage("/admin-mail view [ID]");
            player.sendMessage("/admin-mail clear");
            return;
        }
        if (args[0].equalsIgnoreCase("list")) {
            int page = 1;
            if (args.length > 1) {
                try {
                    page = Integer.parseInt(args[1]);
                } catch (Exception ignored) {
                    player.sendMessage(Component.translatable("parsing.int.invalid").args(Component.text(args[1])).color(NamedTextColor.RED));
                    return;
                }
            }
            player.sendMessage(Component.translatable("command.mail.total_count").args(Component.text(MailManager.getMailList().size())));
            if (MailManager.getMailList().size() > 10)
                player.sendMessage(Component.translatable("command.page").args(Component.text(page)));

            for (int i = (page - 1) * 10; i < MailManager.getMailList().size(); i++) {
                Mail mail = MailManager.getMailList().get(i);
                net.kyori.adventure.text.TextComponent.Builder builder = Component.text();
                builder.append(Component.text("§7=====================§e" + (i + 1) + "§7=====================\n"));
                Component open = Component.translatable("command.mail.open");
                builder.append(Component.translatable("command.mail.id").args(Component.text("§e" + mail.getMailID() + "\n").hoverEvent(open.asHoverEvent()).clickEvent(ClickEvent.runCommand("/admin-mail view " + mail.getMailID()))));
                builder.append(Component.translatable("command.mail.sender").args(Component.text("§e" + (mail.getSender().startsWith("player@") ? plugin.getServer().getOfflinePlayer(UUID.fromString(mail.getSender().substring(7))).getName() : mail.getSender()) + "\n")));
                builder.append(Component.translatable("command.mail.addressee").args(Component.text("§e" + plugin.getServer().getOfflinePlayer(mail.getAddressee()).getName() + "\n")));
                builder.append(Component.translatable("command.mail.title").args(Component.translatable(mail.getTitle())).color(NamedTextColor.YELLOW)).append(Component.text("\n"));
                Component textComponent = Component.text("§7[...]");
                Component text;
                if (mail.getText().equals("gui.mail.no_text")) {
                    text = Component.translatable(mail.getText());
                } else {
                    StringBuilder sb = new StringBuilder();
                    String[] sa = mail.getText().split("\\\\n");
                    int j = 0;
                    for (String s : sa) {
                        sb.append("§7").append(s);
                        if (j++ < sa.length - 1) sb.append("\n");
                    }
                    text = Component.text(sb.toString());
                }
                builder.append(Component.translatable("command.mail.text").args(textComponent.hoverEvent(text.asHoverEvent()))).append(Component.text("\n"));
                builder.append(Component.translatable("command.mail.attachment"));
                if (mail.getItemList().size() == 0) {
                    builder.append(Component.translatable("gui.none"));
                } else {
                    for (int j = 0; j < mail.getItemList().size(); j++) {
                        ItemStack stack = mail.getItemList().get(j);
                        Component item = stack.displayName();
                        item.hoverEvent(stack.asHoverEvent());
                        builder.append(item);
                        if (j < mail.getItemList().size() - 1) builder.append(Component.text(", "));
                    }
                }
                builder.append(Component.text("\n"));
                builder.append(Component.translatable("command.mail.date")).append(Component.text(mail.getDate() + "\n").color(NamedTextColor.YELLOW));
                builder.append(Component.translatable("command.mail.received")).append(mail.isReceived() ? ComponentUtil.text(NamedTextColor.YELLOW, "✔") : ComponentUtil.text(NamedTextColor.RED, "✕"));
                player.sendMessage(builder.build());
            }
            return;
        }
        if (args[0].equalsIgnoreCase("view")) {
            if (args.length == 1) {
                player.sendMessage(Component.translatable("command.unknown.argument").color(NamedTextColor.RED));
                return;
            }
            String id = args[1];
            if (MailManager.getMailByID(id) == null) {
                player.sendMessage(Component.translatable("command.mail.invalid_id").args(Component.text(id)).color(NamedTextColor.RED));
                return;
            }
            MailViewerGUI gui = new MailViewerGUI(player, MailManager.getMailByID(id), ViewType.ADMIN);
            gui.openToPlayer();
            return;
        }
        if (args[0].equalsIgnoreCase("clear")) {
            MailManager.getMailList().clear();
            player.sendMessage(Component.translatable("command.mail.cleared_all"));
            return;
        }
        player.sendMessage(ComponentUtil.translate(NamedTextColor.RED, "command.unknown.argument"));
        player.sendMessage(Component.translatable("command.usages"));
        player.sendMessage("/admin-mail list");
        player.sendMessage("/admin-mail view [ID]");
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
            list.add("view");
            list.add("clear");
        }
        return list;
    }
}
