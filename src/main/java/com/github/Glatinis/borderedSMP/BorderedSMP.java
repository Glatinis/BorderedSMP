package com.github.Glatinis.borderedSMP;

import com.github.Glatinis.borderedSMP.life.LifeCommand;
import com.github.Glatinis.borderedSMP.life.LifeListener;
import com.github.Glatinis.borderedSMP.life.LifeManager;
import com.github.Glatinis.borderedSMP.life.LifeRepository;
import org.bukkit.plugin.java.JavaPlugin;

public final class BorderedSMP extends JavaPlugin {

    @Override
    public void onEnable() {
        LifeRepository repo = new LifeRepository(this);
        repo.load();

        LifeManager manager = new LifeManager(this, repo);

        getServer().getPluginManager().registerEvents(new LifeListener(manager), this);
        getCommand("lives").setExecutor(new LifeCommand(manager));

        getLogger().info("Loaded plugin.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Unloaded plugin.");
    }
}
