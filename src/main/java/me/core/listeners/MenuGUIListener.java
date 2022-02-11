package me.core.listeners;

import me.core.PlayerSettings;
import me.core.ServerPlayer;
import me.core.events.GUIClickEvent;
import me.core.gui.mail.MailBoxGUI;
import me.core.gui.menu.MenuGUI;
import me.core.gui.menu.MenuSettingsGUI;
import me.core.items.MCServerItems;
import me.core.utils.nbt.NBTHelper;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MenuGUIListener {

    public void onClick(@NotNull GUIClickEvent e) {
        if (e.getGUI() instanceof MenuGUI) {
            this.onMenuClick(e);
            return;
        }
        if (e.getGUI() instanceof MenuSettingsGUI) {
            this.onSettingClick(e);
        }
    }

    private void onMenuClick(@NotNull GUIClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item == null || item.getItemMeta() == null) return;
        Player p = (Player) e.getWhoClicked();
        MenuGUI gui = (MenuGUI) e.getGUI();
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.menu.mail")) {
            MailBoxGUI mailGUI = new MailBoxGUI(p);
            mailGUI.setLastInventory(gui);
            mailGUI.openToPlayer();
            return;
        }
        if (MCServerItems.equalWithTag(item, "ItemTag", "gui.menu.settings")) {
            MenuSettingsGUI settingsGUI = new MenuSettingsGUI(p);
            settingsGUI.setLastInventory(gui);
            settingsGUI.openToPlayer();
        }
    }

    private void onSettingClick(@NotNull GUIClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item == null || item.getItemMeta() == null) return;
        Player p = (Player) e.getWhoClicked();
        MenuSettingsGUI gui = (MenuSettingsGUI) e.getGUI();
        if (!NBTHelper.getTag(item).l("ItemTag").equals("gui.settings.toggle")) return;
        ItemStack stack = Objects.requireNonNull(e.getClickedInventory()).getItem(e.getSlot() - 9);
        ServerPlayer sp = ServerPlayer.getServerPlayer(p);
        boolean b = false;
        if (MCServerItems.equalWithTag(stack, "ItemTag", "gui.settings.new_mail_message")) {
            b = !sp.getSettings().get(PlayerSettings.NEW_MAIL_MESSAGE);
            sp.getSettings().put(PlayerSettings.NEW_MAIL_MESSAGE, b);
        }
        if (MCServerItems.equalWithTag(stack, "ItemTag", "gui.settings.container_animation")) {
            b = !sp.getSettings().get(PlayerSettings.CONTAINER_ANIMATION);
            sp.getSettings().put(PlayerSettings.CONTAINER_ANIMATION, b);
        }
        gui.setInventory();
        p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 0.7f, b ? 0.6f : 0.5f);
    }

}
