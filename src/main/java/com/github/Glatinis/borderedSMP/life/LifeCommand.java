package com.github.Glatinis.borderedSMP.life;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LifeCommand implements CommandExecutor, TabCompleter {

    private static final String USAGE =
            ChatColor.YELLOW + "Usage: /lives <set|add|remove|check> <player> [amount]";

    private final LifeManager manager;

    public LifeCommand(LifeManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("borderedsmp.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
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
            sender.sendMessage(ChatColor.RED + "Player '" + playerName + "' has never joined this server.");
            return true;
        }

        switch (sub) {
            case "set" -> {
                int amount = parseAmount(sender, args);
                if (amount == Integer.MIN_VALUE) return true;
                manager.setLives(uuid, playerName, amount);
                sender.sendMessage(ChatColor.GREEN + "Set " + ChatColor.YELLOW + playerName
                        + ChatColor.GREEN + "'s lives to " + ChatColor.YELLOW + amount
                        + ChatColor.GREEN + ".");
            }
            case "add" -> {
                int amount = parseAmount(sender, args);
                if (amount == Integer.MIN_VALUE) return true;
                manager.adjustLives(uuid, playerName, amount);
                int now = manager.getLives(uuid);
                sender.sendMessage(ChatColor.GREEN + "Added " + ChatColor.YELLOW + amount
                        + ChatColor.GREEN + " " + plural(amount) + " to "
                        + ChatColor.YELLOW + playerName + ChatColor.GREEN + ". Now has "
                        + ChatColor.YELLOW + now + ChatColor.GREEN + " " + plural(now) + ".");
            }
            case "remove" -> {
                int amount = parseAmount(sender, args);
                if (amount == Integer.MIN_VALUE) return true;
                manager.adjustLives(uuid, playerName, -amount);
                int now = manager.getLives(uuid);
                sender.sendMessage(ChatColor.GREEN + "Removed " + ChatColor.YELLOW + amount
                        + ChatColor.GREEN + " " + plural(amount) + " from "
                        + ChatColor.YELLOW + playerName + ChatColor.GREEN + ". Now has "
                        + ChatColor.YELLOW + now + ChatColor.GREEN + " " + plural(now) + ".");
            }
            case "check" -> {
                int lives = manager.getLives(uuid);
                sender.sendMessage(ChatColor.GOLD + playerName + ChatColor.WHITE + " has "
                        + ChatColor.YELLOW + lives + ChatColor.WHITE + " " + plural(lives) + ".");
            }
            default -> sender.sendMessage(USAGE);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("borderedsmp.admin")) return List.of();

        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            return Arrays.asList("set", "add", "remove", "check").stream()
                    .filter(s -> s.startsWith(partial))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            String partial = args[1].toLowerCase();
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(partial))
                    .collect(Collectors.toList());
        }

        if (args.length == 3) {
            String sub = args[0].toLowerCase();
            if (sub.equals("set") || sub.equals("add") || sub.equals("remove")) {
                String partial = args[2];
                return Arrays.asList("1", "2", "3", "5", "10").stream()
                        .filter(s -> s.startsWith(partial))
                        .collect(Collectors.toList());
            }
        }

        return List.of();
    }

    private static String plural(int n) {
        return n == 1 ? "life" : "lives";
    }

    private int parseAmount(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(USAGE);
            return Integer.MIN_VALUE;
        }
        try {
            int val = Integer.parseInt(args[2]);
            if (val < 0) {
                sender.sendMessage(ChatColor.RED + "Amount must be a non-negative number.");
                return Integer.MIN_VALUE;
            }
            return val;
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a valid number.");
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
