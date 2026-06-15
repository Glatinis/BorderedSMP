package com.github.Glatinis.borderedSMP.life;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.profile.PlayerProfile;

import java.util.UUID;

public class LifeManager {

    private static final String BAN_MESSAGE =
            "Banned for reaching zero lives, wait for someone to revive you";

    private final JavaPlugin plugin;
    private final LifeRepository repo;

    public LifeManager(JavaPlugin plugin, LifeRepository repo) {
        this.plugin = plugin;
        this.repo = repo;
    }

    public void handleDeath(Player player) {
        int remaining = repo.removeLife(player.getUniqueId());
        if (remaining <= 0) {
            applyBan(player.getUniqueId(), player.getName(), player);
        }
    }

    public void setLives(UUID uuid, String playerName, int lives) {
        repo.setLives(uuid, lives);
        applyBanOrPardon(uuid, playerName, lives, Bukkit.getPlayer(uuid));
    }

    public void adjustLives(UUID uuid, String playerName, int delta) {
        int result = repo.getLives(uuid) + delta;
        repo.setLives(uuid, result);
        applyBanOrPardon(uuid, playerName, result, Bukkit.getPlayer(uuid));
    }

    public int getLives(UUID uuid) {
        return repo.getLives(uuid);
    }

    public void setLivesGlobally(int lives) {
        repo.setDefaultLives(lives);
        repo.setLivesForAll(lives);
    }

    @SuppressWarnings("deprecation")
    private void applyBanOrPardon(UUID uuid, String playerName, int lives, Player onlinePlayer) {
        if (lives <= 0) {
            applyBan(uuid, playerName, onlinePlayer);
        } else {
            profileBanList().pardon(profile(uuid, playerName));
        }
    }

    @SuppressWarnings("deprecation")
    private void applyBan(UUID uuid, String playerName, Player onlinePlayer) {
        // null Instant = permanent ban; cast resolves addBan(PlayerProfile, String, Instant, String) overload
        profileBanList().addBan(profile(uuid, playerName), BAN_MESSAGE, (java.time.Instant) null, plugin.getName());
        if (onlinePlayer != null) {
            Bukkit.getScheduler().runTask(plugin,
                    () -> onlinePlayer.kick(Component.text(BAN_MESSAGE)));
        }
    }

    @SuppressWarnings("deprecation")
    private ProfileBanList profileBanList() {
        return Bukkit.getBanList(org.bukkit.BanList.Type.PROFILE);
    }

    @SuppressWarnings("deprecation")
    private PlayerProfile profile(UUID uuid, String name) {
        return Bukkit.createProfile(uuid, name);
    }
}
