package me.corxl.customdeathscoreboard.Commands;

import me.corxl.customdeathscoreboard.CustomDeathScoreboard;
import me.corxl.customdeathscoreboard.PlayerScoreData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class ScoreboardCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("The console is trying to toggle it's scoreboard? You a dumbo or something?");
            return true;
        }
        if (!sender.hasPermission("scoreboard.toggle")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4You do not have permissions to use /scoreboard"));
            return true;
        }
        if (command.getName().equals("scoreboard")) {
            Player p = (Player) sender;
            if (args.length==0) {
                errorMessage(p);
                return true;
            }
            if (args[0].equalsIgnoreCase("toggle")) {
                HashMap<UUID, PlayerScoreData> dataMap = CustomDeathScoreboard.getDeaths();
                dataMap.get(p.getUniqueId()).toggleBoard();
                if (!dataMap.get(p.getUniqueId()).getToggle()) {
                    p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
                } else {
                    CustomDeathScoreboard.getPlugin().createBoard(p);
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Death Scoreboard &l&b» " + (dataMap.get(p.getUniqueId()).getToggle() ? "&2Enabled" : "&4Disabled")));
            } else if (args[0].equalsIgnoreCase("set")) {
                if (!p.hasPermission("scoreboard.set")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4You do not have permission to use /scoreboard set"));
                    return true;
                }
                if (args.length < 2 || args.length > 3) {
                    errorMessage(p);
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Player is either offline or does not exist..."));
                    return true;
                }
                try {
                    int value = Integer.parseInt(args[2]);
                    if (value < 0) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Value MUST be of type [Integer] and must be positive. \n&7Correct syntax: &c/sb set [Player-name] [value]"));
                        return true;
                    }
                    CustomDeathScoreboard.getDeaths().get(target.getUniqueId()).setDeathScore(value);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', target.getDisplayName() + "&6's &6deaths has been set to: &2&n" + value));
                    for(Player online : Bukkit.getOnlinePlayers()) {
                        CustomDeathScoreboard.getPlugin().createBoard(online);
                    }
                } catch (Exception e) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Value MUST be of type [Integer] and must be positive. \n&7Correct syntax: &c/sb set [Player-name] [value]"));
                    return true;
                }

            } else {
                errorMessage(p);
                return true;
            }
            return true;
        }
        return false;
    }

    private void errorMessage(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid use of /scoreboard | /sb" +
                "\n&7Correct usage: /scoreboard [toggle | set]" +
                "\n&7Correct usage: /sb [toggle | set]"));
    }
}
