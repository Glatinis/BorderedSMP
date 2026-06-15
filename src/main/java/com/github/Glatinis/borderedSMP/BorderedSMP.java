package com.github.Glatinis.borderedSMP;

import org.bukkit.plugin.java.JavaPlugin;

public final class BorderedSMP extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Loaded plugin.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Unloaded plugin.");
    }
}
