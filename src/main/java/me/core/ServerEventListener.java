package me.core;

import me.core.mail.Mail;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
        if (!p.hasPlayedBefore()) {
            List<ItemStack> stacks = new ArrayList<>();
            ItemStack stack = new ItemStack(Material.ARROW);
            ItemMeta meta = stack.getItemMeta();
            assert meta != null;
            meta.setDisplayName("The bug arrow");
            stack.setItemMeta(meta);
            stacks.add(stack);
            Mail mail = new Mail("server", p, "Welcome to " + plugin.getServer().getName() + "!", "Use this to become a stand user.", stacks);
            plugin.getMailManager().sendMail(mail);
        }
    }

    @EventHandler
    public void onLeave(@NotNull PlayerQuitEvent e) {
        Player p = e.getPlayer();
    }

}
