package me.trololo11.lifespluginseason3.listeners.datasetups;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.managers.DatabaseManager;
import me.trololo11.lifespluginseason3.utils.PlayerStats;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

public class PlayerStatsDataSetup implements Listener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();
    private DatabaseManager databaseManager;

    public PlayerStatsDataSetup(DatabaseManager databaseManager){
        this.databaseManager = databaseManager;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws SQLException {
        Player player = e.getPlayer();
        PlayerStats playerStats = databaseManager.getPlayerStats(player);
        if(playerStats == null){
            playerStats = new PlayerStats(player, 0,0,0,0,0,0,0,0,0,0,0,0);
            databaseManager.addPlayerStats(playerStats);
        }

        plugin.addPlayerStats(playerStats);


    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) throws SQLException {
        Player player = e.getPlayer();
        databaseManager.updatePlayerStats(plugin.getPlayerStats(player));
        plugin.removePlayerStats(player);
    }
}
