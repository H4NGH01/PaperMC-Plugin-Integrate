package me.core;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigurationManager {

    private final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);
    private FileConfiguration playerConfig;
    private File playerFile;

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        playerFile = new File(plugin.getDataFolder(), "player.yml");
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
            } catch (IOException e) {
                plugin.getServer().getConsoleSender().sendMessage("Could not load the player.yml file.");
            }
        }
        playerConfig = YamlConfiguration.loadConfiguration(playerFile);
        plugin.getServer().getConsoleSender().sendMessage("player.yml file loaded.");
    }

    public FileConfiguration getPlayerConfig() {
        return playerConfig;
    }

    public void savePlayerConfig() {
        try {
            playerConfig.save(playerFile);
        } catch (IOException e) {
            plugin.getServer().getConsoleSender().sendMessage("Could not save the player.yml file.");
        }
    }

}
