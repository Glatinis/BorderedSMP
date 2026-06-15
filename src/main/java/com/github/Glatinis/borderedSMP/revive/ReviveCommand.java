package com.github.Glatinis.borderedSMP.revive;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReviveCommand implements CommandExecutor, TabCompleter {

    private static final String USAGE = ChatColor.YELLOW + "Usage: /revive <toggle|reload>";

    private final ReviveConfig config;
    private final ReviveManager manager;

    public ReviveCommand(ReviveConfig config, ReviveManager manager) {
        this.config = config;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("borderedsmp.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(USAGE);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "toggle" -> {
                boolean newState = !config.isEnabled();
                config.setEnabled(newState);
                String stateStr = newState ? (ChatColor.GREEN + "enabled") : (ChatColor.RED + "disabled");
                sender.sendMessage(ChatColor.GOLD + "Revive system " + stateStr + ChatColor.GOLD + ".");
            }
            case "reload" -> {
                manager.reloadRecipe();
                sender.sendMessage(ChatColor.GREEN + "Revive recipe reloaded from config.");
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
            return Arrays.asList("toggle", "reload").stream()
                    .filter(s -> s.startsWith(partial))
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}
