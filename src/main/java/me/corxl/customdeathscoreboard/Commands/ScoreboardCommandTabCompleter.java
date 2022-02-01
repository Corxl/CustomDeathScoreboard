package me.corxl.customdeathscoreboard.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardCommandTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> options = new ArrayList<>();
        if (!command.getName().equalsIgnoreCase("scoreboard")) return null;
        if (args.length==1) {
            if (sender.hasPermission("scoreboard.toggle"))
                options.add("toggle");
            if (sender.hasPermission("scoreboard.set"))
                options.add("set");
            return options;
        }
        if (args[0].equalsIgnoreCase("set") && args.length==3) {
            if (sender.hasPermission("scoreboard.set"))
                options.add("0");
            return options;
        }
        return null;
    }
}
