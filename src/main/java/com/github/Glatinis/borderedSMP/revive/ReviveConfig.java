package com.github.Glatinis.borderedSMP.revive;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

public class ReviveConfig {

    private final JavaPlugin plugin;

    public ReviveConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isEnabled() {
        return plugin.getConfig().getBoolean("revive.enabled", true);
    }

    public void setEnabled(boolean enabled) {
        plugin.getConfig().set("revive.enabled", enabled);
        plugin.saveConfig();
    }

    public int getLivesToRestore() {
        return plugin.getConfig().getInt("revive.lives-to-restore", 3);
    }

    public String getResultMaterial() {
        return plugin.getConfig().getString("revive.recipe.result-material", "TOTEM_OF_UNDYING");
    }

    public List<String> getShape() {
        return plugin.getConfig().getStringList("revive.recipe.shape");
    }

    public Map<String, Object> getIngredients() {
        var section = plugin.getConfig().getConfigurationSection("revive.recipe.ingredients");
        return section != null ? section.getValues(false) : Map.of();
    }
}
