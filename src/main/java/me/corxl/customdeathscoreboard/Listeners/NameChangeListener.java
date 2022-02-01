package me.corxl.customdeathscoreboard.Listeners;

import me.corxl.customdeathscoreboard.CustomDeathScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class NameChangeListener implements Listener {

    @EventHandler
    public void onNameChange(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().toLowerCase().startsWith("/nick") || event.getMessage().toLowerCase().startsWith("/nickname")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    CustomDeathScoreboard.getDeaths().get(event.getPlayer().getUniqueId()).setDisplayName(event.getPlayer().getDisplayName());
                    for(Player online : Bukkit.getOnlinePlayers())
                        CustomDeathScoreboard.getPlugin().createBoard(online);
                }
            }.runTaskLater(CustomDeathScoreboard.getPlugin(), 5);
        }
    }
}
