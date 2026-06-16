package com.github.Glatinis.borderedSMP.border;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;

import java.util.concurrent.ThreadLocalRandom;

public class BorderManager {

    private final BorderConfig config;

    public BorderManager(BorderConfig config) {
        this.config = config;
    }

    public void onPlayerDeath(World world) {
        if (!config.isEnabled()) return;

        int delta = ThreadLocalRandom.current().nextInt(
                config.getRangeMin(), config.getRangeMax() + 1);

        WorldBorder border = world.getWorldBorder();
        border.setSize(border.getSize() + delta);

        float volume = config.getSoundVolume();
        Bukkit.getOnlinePlayers().forEach(p ->
                p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, volume, 1.0f));
    }
}
