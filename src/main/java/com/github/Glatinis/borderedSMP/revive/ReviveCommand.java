package com.github.Glatinis.borderedSMP.revive;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReviveCommand implements CommandExecutor {

    private static final String USAGE = "Usage: /revive <toggle|reload>";

    private final ReviveConfig config;
    private final ReviveManager manager;

    public ReviveCommand(ReviveConfig config, ReviveManager manager) {
        this.config = config;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("borderedsmp.admin")) {
            sender.sendMessage("You don't have permission to use this command.");
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
                sender.sendMessage("Revive system " + (newState ? "enabled" : "disabled") + ".");
            }
            case "reload" -> {
                manager.reloadRecipe();
                sender.sendMessage("Revive recipe reloaded from config.");
            }
            default -> sender.sendMessage(USAGE);
        }

        return true;
    }
}
