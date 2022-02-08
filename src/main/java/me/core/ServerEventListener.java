package me.core;

import me.core.mail.Mail;
import me.core.mail.MailManager;
import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ServerEventListener implements Listener {

    private final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e) {
        Player p = e.getPlayer();
        MCServerPlugin.getServerPlayerHashMap().put(p, new ServerPlayer(p));
        if (!p.hasPlayedBefore()) {
            List<ItemStack> stacks = new ArrayList<>();
            ItemStack stack = new ItemStack(Material.ARROW);
            ItemMeta meta = stack.getItemMeta();
            assert meta != null;
            meta.displayName(Component.text("The bug arrow"));
            stack.setItemMeta(meta);
            stacks.add(stack);
            Mail mail = new Mail("server", p, "Welcome to " + plugin.getServer().getName() + "!", "Use this to become a stand user.", stacks);
            MailManager.sendMail(mail);
        }
        ServerPlayer sp = MCServerPlugin.getServerPlayerHashMap().get(p);
        if (sp.getStorage().size() != 0) {
            for (ItemStack stack : sp.getStorage()) {
                sp.safeAddItem(stack);
            }
            sp.getStorage().clear();
        }
        if (sp.getNewMail() != 0) {
            p.sendMessage(Component.translatable("chat.mail_received_offline").args(Component.text(sp.getNewMail()).color(NamedTextColor.YELLOW)));
            sp.setNewMail(0);
        }
    }

    @EventHandler
    public void onLeave(@NotNull PlayerQuitEvent e) {
        Player p = e.getPlayer();
        ServerPlayer sp = plugin.getServerPlayer(p);
        MCServerPlugin.getServerPlayerHashMap().remove(p);
        sp.save();
    }

    @EventHandler
    public void onBlockPlace(@NotNull BlockPlaceEvent e) {
        Player p = e.getPlayer();
        ItemStack stack = p.getInventory().getItemInMainHand();
        if (NBTHelper.hasTag(stack, "placeable") && !NBTHelper.getTag(stack).q("placeable")) e.setCancelled(true);
    }

}
