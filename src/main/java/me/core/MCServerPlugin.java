package me.core;

import me.core.command.CommandManager;
import me.core.enchantments.PluginEnchantments;
import me.core.event.ServerChatBarListener;
import me.core.event.ServerGUIListener;
import me.core.event.StatTrakEvent;
import me.core.mail.MailManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class MCServerPlugin extends JavaPlugin {

    private static HashMap<Player, ServerPlayer> serverPlayerHashMap;
    private ConfigurationManager configManager;
    private CommandManager commandManager;
    private MailManager mailManager;

    @Override
    public void onEnable() {
        this.loadConfig();
        this.configManager = new ConfigurationManager();
        this.configManager.setup();
        this.commandManager = new CommandManager();
        this.commandManager.setup();
        PluginEnchantments.loadEnchantments();
        serverPlayerHashMap = new HashMap<>();
        this.mailManager = new MailManager();
        this.registerEvents();
        for (Player player : this.getServer().getOnlinePlayers()) {
            serverPlayerHashMap.put(player, new ServerPlayer(player));
        }
        this.log("Server Plugin Enable");
    }

    @Override
    public void onDisable() {
        for (Player p : this.getServer().getOnlinePlayers()) {
            p.closeInventory();
            ServerPlayer sp = serverPlayerHashMap.get(p);
            if (sp != null) sp.save();
        }
        PluginEnchantments.unloadEnchantments();
        this.mailManager.save();
        this.configManager.savePlayerConfig();
        this.log("Server Plugin Disable");
    }

    private void loadConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new ServerEventListener(), this);
        this.getServer().getPluginManager().registerEvents(new ServerGUIListener(), this);
        this.getServer().getPluginManager().registerEvents(new ServerChatBarListener(), this);
        this.getServer().getPluginManager().registerEvents(new StatTrakEvent(), this);
    }

    public static HashMap<Player, ServerPlayer> getServerPlayerHashMap() {
        return serverPlayerHashMap;
    }

    public ServerPlayer getServerPlayer(Player player) {
        return serverPlayerHashMap.get(player);
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

    public void log(String s) {
        this.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[" + getPlugin(this.getClass()).getName() + "] " + s + ChatColor.RESET);
    }
}
