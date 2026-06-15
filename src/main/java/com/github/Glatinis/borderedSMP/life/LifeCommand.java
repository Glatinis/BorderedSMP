package com.github.Glatinis.borderedSMP.life;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LifeCommand implements CommandExecutor {

    private static final String USAGE =
            "Usage: /lives <set|add|remove|check> <player> [amount]";

    private final LifeManager manager;

    public LifeCommand(LifeManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("borderedsmp.admin")) {
            sender.sendMessage("You don't have permission to use this command.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(USAGE);
            return true;
        }

        String sub = args[0].toLowerCase();
        String playerName = args[1];

        UUID uuid = resolveUUID(playerName);
        if (uuid == null) {
            sender.sendMessage("Player '" + playerName + "' has never joined this server.");
            return true;
        }

        switch (sub) {
            case "set" -> {
                int amount = parseAmount(sender, args);
                if (amount == Integer.MIN_VALUE) return true;
                manager.setLives(uuid, playerName, amount);
                sender.sendMessage("Set " + playerName + "'s lives to " + amount + ".");
            }
            case "add" -> {
                int amount = parseAmount(sender, args);
                if (amount == Integer.MIN_VALUE) return true;
                manager.adjustLives(uuid, playerName, amount);
                sender.sendMessage("Added " + amount + " life/lives to " + playerName
                        + ". Now has " + manager.getLives(uuid) + ".");
            }
            case "remove" -> {
                int amount = parseAmount(sender, args);
                if (amount == Integer.MIN_VALUE) return true;
                manager.adjustLives(uuid, playerName, -amount);
                sender.sendMessage("Removed " + amount + " life/lives from " + playerName
                        + ". Now has " + manager.getLives(uuid) + ".");
            }
            case "check" -> {
                int lives = manager.getLives(uuid);
                sender.sendMessage(playerName + " has " + lives + " life/lives.");
            }
            default -> sender.sendMessage(USAGE);
        }

        return true;
    }

    private int parseAmount(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(USAGE);
            return Integer.MIN_VALUE;
        }
        try {
            int val = Integer.parseInt(args[2]);
            if (val < 0) {
                sender.sendMessage("Amount must be a non-negative number.");
                return Integer.MIN_VALUE;
            }
            return val;
        } catch (NumberFormatException e) {
            sender.sendMessage("'" + args[2] + "' is not a valid number.");
            return Integer.MIN_VALUE;
        }
    }

    @SuppressWarnings("deprecation")
    private UUID resolveUUID(String name) {
        Player online = Bukkit.getPlayerExact(name);
        if (online != null) return online.getUniqueId();

        OfflinePlayer offline = Bukkit.getOfflinePlayer(name);
        return offline.hasPlayedBefore() ? offline.getUniqueId() : null;
    }
}
