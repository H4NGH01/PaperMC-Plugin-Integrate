package me.core;

import me.core.commands.CommandManager;
import me.core.containers.ContainerManager;
import me.core.enchantments.PluginEnchantments;
import me.core.gui.ContainerGUI;
import me.core.listeners.ContainerListener;
import me.core.listeners.ServerChatBarListener;
import me.core.listeners.ServerGUIListener;
import me.core.listeners.StatTrakListener;
import me.core.mail.MailManager;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MCServerPlugin extends JavaPlugin {

    private ConfigurationManager configManager;
    private CommandManager commandManager;
    private MailManager mailManager;
    private ContainerManager containerManager;

    @Override
    public void onEnable() {
        this.loadConfig();
        this.configManager = new ConfigurationManager();
        this.configManager.setup();
        this.commandManager = new CommandManager();
        this.commandManager.setup();
        PluginEnchantments.loadEnchantments();
        this.mailManager = new MailManager();
        this.containerManager = new ContainerManager();
        this.registerEvents();
        ServerGUIListener.getOpenedGUI().clear();
        this.log("Plugin Enable");
    }

    @Override
    public void onDisable() {
        if (ContainerManager.c() && ContainerGUI.getViews() != null && ContainerGUI.getViews().size() != 0) {
            for (ContainerGUI gui : ContainerGUI.getViews().values()) {
                if (gui.isOpening()) {
                    ServerPlayer sp = ServerPlayer.getServerPlayer(gui.getPlayer());
                    sp.safeAddItem(gui.getContainer().getDrop());
                    Component component = gui.getContainer().getDrop().displayName();
                    component.hoverEvent(gui.getContainer().getDrop().asHoverEvent());
                    sp.getPlayer().sendMessage(Component.translatable("chat.container.opened_item").args(component));
                    ContainerManager.unregisterContainerData(gui.getContainer());
                }
            }
        }
        for (Player p : this.getServer().getOnlinePlayers()) {
            p.closeInventory();
            ServerPlayer sp = ServerPlayer.getServerPlayer(p);
            sp.save();
        }
        mailManager.save();
        containerManager.save();
        this.configManager.save("player.yml");
        PluginEnchantments.unloadEnchantments();
        this.log("Plugin Disable");
    }

    private void loadConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new ServerEventListener(), this);
        this.getServer().getPluginManager().registerEvents(new ServerGUIListener(), this);
        this.getServer().getPluginManager().registerEvents(new ServerChatBarListener(), this);
        this.getServer().getPluginManager().registerEvents(new ContainerListener(), this);
        this.getServer().getPluginManager().registerEvents(new StatTrakListener(), this);
    }

    public ConfigurationManager getConfigManager() {
        return this.configManager;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public MailManager getMailManager() {
        return this.mailManager;
    }

    public ContainerManager getContainerManager() {
        return this.containerManager;
    }

    public void log(String s) {
        this.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[" + getPlugin(this.getClass()).getName() + "] " + s + ChatColor.RESET);
    }
}
