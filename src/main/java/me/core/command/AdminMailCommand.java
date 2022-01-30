package me.core.command;

import me.core.MCServerPlugin;
import me.core.mail.Mail;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class AdminMailCommand extends PluginCommand {

    private final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);
    private final String[] s = new String[]{"amail"};

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.GREEN + "Usages:");
            player.sendMessage(ChatColor.GREEN + "/admin-mail list");
            player.sendMessage(ChatColor.GREEN + "/admin-mail clear");
            return;
        }
        if (args[0].equalsIgnoreCase("list")) {
            int page = 1;
            if (args.length > 1) {
                try {
                    page = Integer.parseInt(args[1]);
                } catch (Exception ignored) {
                    player.sendMessage("§c'" + args[1] + "' is not a number");
                    return;
                }
            }
            player.sendMessage("§aTotal server mail count: " + plugin.getMailManager().getMailList().size());
            if (plugin.getMailManager().getMailList().size() > 10) player.sendMessage("§aPage " + page);
            for (int i = (page - 1) * 10; i < plugin.getMailManager().getMailList().size(); i++) {
                Mail mail = plugin.getMailManager().getMailList().get(i);
                TextComponent textComponent = new TextComponent(ChatColor.GRAY + "=====================" + ChatColor.YELLOW + (i + 1) + ChatColor.GRAY + "=====================\n");
                textComponent.addExtra(ChatColor.GRAY + "Mail ID: " + ChatColor.YELLOW + mail.getMailID() + "\n");
                textComponent.addExtra(ChatColor.GRAY + "Sender: " + ChatColor.YELLOW + (mail.getSender().startsWith("player@") ? plugin.getServer().getOfflinePlayer(UUID.fromString(mail.getSender().substring(7))).getName() : mail.getSender()) + "\n");
                textComponent.addExtra(ChatColor.GRAY + "Addressee: " + ChatColor.YELLOW + plugin.getServer().getOfflinePlayer(mail.getAddressee()).getName() + "\n");
                textComponent.addExtra(ChatColor.GRAY + "Title: " + ChatColor.YELLOW + mail.getTitle() + "\n");
                TextComponent text = new TextComponent(ChatColor.GRAY + "Text: [...]");
                ComponentBuilder cb = new ComponentBuilder();
                String[] sa = (mail.getText()).split("\\\\n");
                for (String s : sa) {
                    cb.append("§7" + s);
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
                player.spigot().sendMessage(textComponent);
                //
            }
            return;
        }
        if (args[0].equalsIgnoreCase("clear")) {
            plugin.getMailManager().getMailList().clear();
            player.sendMessage(ChatColor.GREEN + "Cleared all mail");
            return;
        }
        player.sendMessage(ChatColor.RED + "Unknown sub command.");
        player.sendMessage(ChatColor.GREEN + "Usages:");
        player.sendMessage(ChatColor.GREEN + "/admin-mail list");
        player.sendMessage(ChatColor.GREEN + "/admin-mail clear");
    }

    @Override
    public String name() {
        return "admin-mail";
    }

    @Override
    public String info() {
        return "Manage mail";
    }

    @Override
    public String[] aliases() {
        return s;
    }
}
