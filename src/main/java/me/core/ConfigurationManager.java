package me.core;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ConfigurationManager {

    private final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);
    private HashMap<File, FileConfiguration> configurationMap;

    public void setup() {
        this.configurationMap = new HashMap<>();
        if (!this.plugin.getDataFolder().exists()) {
            this.plugin.getDataFolder().mkdir();
        }
        addConfig("player.yml");
    }

    private FileConfiguration addConfig(String fileName) {
        File file = new File(this.plugin.getDataFolder(), fileName);
        FileConfiguration config = this.load(new File(this.plugin.getDataFolder(), fileName));
        this.getConfigurationMap().put(file, config);
        return config;
    }

    public FileConfiguration load(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getServer().getConsoleSender().sendMessage("Could not load the " + file.getName() + " file.");
            }
        }
        plugin.getServer().getConsoleSender().sendMessage(file.getName() + " file loaded.");
        return YamlConfiguration.loadConfiguration(file);
    }

    public HashMap<File, FileConfiguration> getConfigurationMap() {
        return this.configurationMap;
    }

    public FileConfiguration getConfiguration(String fileName) {
        for (File file : this.getConfigurationMap().keySet()) {
            if (file.getName().equals(fileName)) {
                return this.getConfigurationMap().get(file);
            }
        }
        return this.addConfig(fileName);
    }

    public void save(String fileName) {
        for (File file : this.getConfigurationMap().keySet()) {
            if (file.getName().equals(fileName)) {
                try {
                    this.getConfigurationMap().get(file).save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    this.plugin.getServer().getConsoleSender().sendMessage("Could not save the " + fileName + " file.");
                }
                return;
            }
        }
    }
}
