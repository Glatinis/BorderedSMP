package com.github.Glatinis.borderedSMP.life;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class LifeRepository {

    private final JavaPlugin plugin;
    private final File file;
    private FileConfiguration config;

    public LifeRepository(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "lives.yml");
    }

    public void load() {
        if (!file.exists()) {
            plugin.saveResource("lives.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save lives.yml: " + e.getMessage());
        }
    }

    public int getDefaultLives() {
        return config.getInt("default-lives", 3);
    }

    public void setDefaultLives(int lives) {
        config.set("default-lives", lives);
        save();
    }

    private String key(UUID uuid) {
        return "players." + uuid;
    }

    public int getLives(UUID uuid) {
        return config.getInt(key(uuid), getDefaultLives());
    }

    public void setLives(UUID uuid, int lives) {
        config.set(key(uuid), lives);
        save();
    }

    public int removeLife(UUID uuid) {
        int current = getLives(uuid);
        int updated = current - 1;
        setLives(uuid, updated);
        return updated;
    }

    public int resetLives(UUID uuid) {
        int lives = getDefaultLives();
        setLives(uuid, lives);
        return lives;
    }

    public void setLivesForAll(int lives) {
        var section = config.getConfigurationSection("players");
        if (section != null) {
            for (String uuidStr : section.getKeys(false)) {
                config.set("players." + uuidStr, lives);
            }
        }
        save();
    }

    public boolean hasEntry(UUID uuid) {
        return config.contains(key(uuid));
    }

    public void removeEntry(UUID uuid) {
        config.set(key(uuid), null);
        save();
    }
}