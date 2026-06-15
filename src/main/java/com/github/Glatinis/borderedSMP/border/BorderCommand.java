package com.github.Glatinis.borderedSMP.border;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BorderCommand implements CommandExecutor {

    private static final String USAGE = "Usage: /border setrange <min> <max>";

    private final BorderConfig config;

    public BorderCommand(BorderConfig config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
            sender.sendMessage("Min and max must be integers.");
            return true;
        }

        if (min < 0 || max < 0) {
            sender.sendMessage("Min and max must be non-negative.");
            return true;
        }

        if (min > max) {
            sender.sendMessage("Min must be less than or equal to max.");
            return true;
        }

        config.setRange(min, max);
        sender.sendMessage("Border range set to " + min + "–" + max + " blocks per death.");
        return true;
    }
}
