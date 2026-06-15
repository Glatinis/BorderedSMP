package com.github.Glatinis.borderedSMP.life;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class LifeListener implements Listener {

    private final LifeManager manager;

    public LifeListener(LifeManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        manager.handleDeath(event.getEntity());
    }
}
