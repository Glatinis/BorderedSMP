package com.github.Glatinis.borderedSMP.border;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BorderCommand implements CommandExecutor, TabCompleter {

    private static final String USAGE = ChatColor.YELLOW + "Usage: /border setrange <min> <max>";

    private final BorderConfig config;

    public BorderCommand(BorderConfig config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("borderedsmp.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length < 1 || !args[0].equalsIgnoreCase("setrange")) {
            sender.sendMessage(USAGE);
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(USAGE);
            return true;
        }

        int min, max;
        try {
            min = Integer.parseInt(args[1]);
            max = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Min and max must be integers.");
            return true;
        }

        if (min < 0 || max < 0) {
            sender.sendMessage(ChatColor.RED + "Min and max must be non-negative.");
            return true;
        }

        if (min > max) {
            sender.sendMessage(ChatColor.RED + "Min must be less than or equal to max.");
            return true;
        }

        config.setRange(min, max);
        sender.sendMessage(ChatColor.GREEN + "Border range set to " + ChatColor.YELLOW + min
                + ChatColor.GREEN + "–" + ChatColor.YELLOW + max
                + ChatColor.GREEN + " blocks per death.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("borderedsmp.admin")) return List.of();

        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            return List.of("setrange").stream()
                    .filter(s -> s.startsWith(partial))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            String partial = args[1];
            return Arrays.asList("10", "50", "100", "500").stream()
                    .filter(s -> s.startsWith(partial))
                    .collect(Collectors.toList());
        }

        if (args.length == 3) {
            String partial = args[2];
            return Arrays.asList("100", "500", "1000", "5000").stream()
                    .filter(s -> s.startsWith(partial))
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}
