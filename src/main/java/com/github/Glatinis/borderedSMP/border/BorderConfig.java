package com.github.Glatinis.borderedSMP.border;

import org.bukkit.plugin.java.JavaPlugin;

public class BorderConfig {

    private final JavaPlugin plugin;

    public BorderConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isEnabled() {
        return plugin.getConfig().getBoolean("border.enabled", true);
    }

    public int getRangeMin() {
        return plugin.getConfig().getInt("border.range.min", 1);
    }

    public int getRangeMax() {
        return plugin.getConfig().getInt("border.range.max", 10);
    }

    public float getSoundVolume() {
        return (float) plugin.getConfig().getDouble("border.sound.volume", 1.0);
    }

    public void setRange(int min, int max) {
        plugin.getConfig().set("border.range.min", min);
        plugin.getConfig().set("border.range.max", max);
        plugin.saveConfig();
    }
}
