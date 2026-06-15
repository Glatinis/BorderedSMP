package com.github.Glatinis.borderedSMP.border;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BorderListener implements Listener {

    private final BorderManager manager;

    public BorderListener(BorderManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        manager.onPlayerDeath(event.getEntity().getWorld());
    }
}
