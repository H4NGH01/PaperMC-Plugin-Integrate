package me.core.listeners;

import me.core.MCServerPlugin;
import me.core.PlayerSettings;
import me.core.ServerPlayer;
import me.core.mail.Mail;
import me.core.mail.MailManager;
import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
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
            Mail mail = new Mail("server", p, "Welcome to " + plugin.getServer().getName() + "!", "Good luck and have fun!", stacks);
            MailManager.sendMail(mail);
        }
        ServerPlayer sp = ServerPlayer.getServerPlayer(p);
        if (sp.getStorage().size() != 0) {
            for (ItemStack stack : sp.getStorage()) {
                sp.safeAddItem(stack);
            }
            sp.getStorage().clear();
        }
        if (sp.getSettings().get(PlayerSettings.NEW_MAIL_MESSAGE) && sp.getNewMail() != 0) {
            p.sendMessage(Component.translatable("chat.mail.received_offline").args(Component.text(sp.getNewMail()).color(NamedTextColor.YELLOW)));
            sp.setNewMail(0);
        }
    }

    @EventHandler
    public void onLeave(@NotNull PlayerQuitEvent e) {
        Player p = e.getPlayer();
        ServerPlayer sp = ServerPlayer.getServerPlayer(p);
        sp.save();
        ServerPlayer.getServerPlayerHashMap().remove(p.getUniqueId());
    }

    @EventHandler
    public void onBlockPlace(@NotNull BlockPlaceEvent e) {
        Player p = e.getPlayer();
        ItemStack stack = p.getInventory().getItemInMainHand();
        if (NBTHelper.hasTag(stack, "placeable") && !NBTHelper.getTag(stack).q("placeable")) e.setCancelled(true);
    }

}
