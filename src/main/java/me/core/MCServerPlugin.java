package me.core;

import me.core.commands.CommandManager;
import me.core.containers.ContainerData;
import me.core.containers.ContainerManager;
import me.core.enchantments.PluginEnchantments;
import me.core.gui.ContainerGUI;
import me.core.listeners.*;
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
        PluginEnchantments.loadEnchantments();
        this.commandManager = new CommandManager();
        this.commandManager.setup();
        this.mailManager = new MailManager();
        this.containerManager = new ContainerManager();
        this.registerEvents();
        ServerGUIListener.getOpenedGUI().clear();
        for (Player p : this.getServer().getOnlinePlayers()) {
            ServerPlayer.getServerPlayer(p);
        }
        this.log("Plugin Enable");
    }

    @Override
    public void onDisable() {
        if (ContainerManager.isClassNotNull() && ContainerGUI.getViews().size() != 0) {
            for (ContainerGUI gui : ContainerGUI.getViews().values()) {
                if (gui.isOpening()) {
                    ServerPlayer sp = ServerPlayer.getServerPlayer(gui.getPlayer());
                    ContainerData data = gui.getData();
                    sp.safeAddItem(data.getDrop());
                    Component component = data.getDrop().displayName();
                    component.hoverEvent(data.getDrop().asHoverEvent());
                    sp.getPlayer().sendMessage(Component.translatable("chat.container.opened_item").args(component));
                    ContainerManager.unregisterContainerData(data);
                }
            }
        }
        for (Player p : this.getServer().getOnlinePlayers()) {
            p.closeInventory();
            ServerPlayer sp = ServerPlayer.getServerPlayer(p);
            sp.save();
        }
        this.mailManager.save();
        this.containerManager.save();
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
