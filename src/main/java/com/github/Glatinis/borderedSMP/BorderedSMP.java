package com.github.Glatinis.borderedSMP;

import com.github.Glatinis.borderedSMP.border.BorderCommand;
import com.github.Glatinis.borderedSMP.border.BorderConfig;
import com.github.Glatinis.borderedSMP.border.BorderListener;
import com.github.Glatinis.borderedSMP.border.BorderManager;
import com.github.Glatinis.borderedSMP.life.LifeCommand;
import com.github.Glatinis.borderedSMP.life.LifeListener;
import com.github.Glatinis.borderedSMP.life.LifeManager;
import com.github.Glatinis.borderedSMP.life.LifeRepository;
import com.github.Glatinis.borderedSMP.revive.ReviveCommand;
import com.github.Glatinis.borderedSMP.revive.ReviveConfig;
import com.github.Glatinis.borderedSMP.revive.ReviveListener;
import com.github.Glatinis.borderedSMP.revive.ReviveManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BorderedSMP extends JavaPlugin {

    @Override
    public void onEnable() {
        LifeRepository repo = new LifeRepository(this);
        repo.load();

        LifeManager lifeManager = new LifeManager(this, repo);
        getServer().getPluginManager().registerEvents(new LifeListener(lifeManager), this);
        getCommand("lives").setExecutor(new LifeCommand(lifeManager));

        saveDefaultConfig();
        BorderConfig borderConfig = new BorderConfig(this);
        BorderManager borderManager = new BorderManager(borderConfig);
        getServer().getPluginManager().registerEvents(new BorderListener(borderManager), this);
        getCommand("border").setExecutor(new BorderCommand(borderConfig));

        ReviveConfig reviveConfig = new ReviveConfig(this);
        ReviveManager reviveManager = new ReviveManager(this, reviveConfig, lifeManager);
        reviveManager.registerRecipe();
        getServer().getPluginManager().registerEvents(new ReviveListener(this, reviveManager), this);
        getCommand("revive").setExecutor(new ReviveCommand(reviveConfig, reviveManager));

        getLogger().info("Loaded plugin.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Unloaded plugin.");
    }
}
