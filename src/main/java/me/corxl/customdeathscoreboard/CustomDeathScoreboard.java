package me.corxl.customdeathscoreboard;

import me.corxl.customdeathscoreboard.Commands.ScoreboardCommand;
import me.corxl.customdeathscoreboard.Commands.ScoreboardCommandTabCompleter;
import me.corxl.customdeathscoreboard.Listeners.NameChangeListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public final class CustomDeathScoreboard extends JavaPlugin implements Listener {

    private static HashMap<UUID, PlayerScoreData> deaths = new HashMap<>();

    private static CustomDeathScoreboard plugin;

    public static CustomDeathScoreboard getPlugin() {
        return plugin;
    }

    public static HashMap<UUID, PlayerScoreData> getDeaths() {
        return deaths;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginManager().registerEvents(new NameChangeListener(), this);

        this.getCommand("scoreboard").setExecutor(new ScoreboardCommand());
        this.getCommand("scoreboard").setTabCompleter(new ScoreboardCommandTabCompleter());

        File file = new File(this.getDataFolder().getAbsolutePath() + System.getProperty("file.separator") + "deathdata.data");
        if (file.exists()) {
            loadSave(file);
        }
        if (!Bukkit.getOnlinePlayers().isEmpty())
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (!deaths.containsKey(online.getUniqueId())) {
                    deaths.put(online.getUniqueId(), new PlayerScoreData(online.getDisplayName()));
                }
                createBoard(online);
            }
    }

    @Override
    public void onDisable() {
        saveToFile();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        if (!deaths.containsKey(player.getUniqueId())) {
            deaths.put(player.getUniqueId(), new PlayerScoreData(player.getDisplayName()));
        }
        deaths.get(event.getPlayer().getUniqueId()).increaseDeathScore();
        for (Player online : Bukkit.getOnlinePlayers())
            createBoard(online);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = event.getPlayer();
                if (!deaths.containsKey(player.getUniqueId())) {
                    deaths.put(player.getUniqueId(), new PlayerScoreData(player.getDisplayName()));
                }
                for (Player online : Bukkit.getOnlinePlayers())
                    createBoard(online);
            }
        }.runTaskLater(this, 5);
    }

    public void createBoard(Player player) {
        if (!deaths.get(player.getUniqueId()).getToggle()) return;
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("Deaths", "dummy", ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("scoreboard-title")));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        deaths.forEach((value, key) -> {
            objective.getScore(key.getDisplayName()).setScore(key.getDeathScore());
        });
        player.setScoreboard(board);
        saveToFile();
    }

    private void saveToFile() {
        File file = new File(this.getDataFolder().getAbsolutePath() + System.getProperty("file.separator") + "deathdata.data");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            ObjectOutputStream outstream = new ObjectOutputStream(out);
            outstream.writeObject(deaths);
            outstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSave(File file) {
        try {
            FileInputStream in = new FileInputStream(file);
            ObjectInputStream instream = new ObjectInputStream(in);
            deaths = (HashMap<UUID, PlayerScoreData>) instream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}






























