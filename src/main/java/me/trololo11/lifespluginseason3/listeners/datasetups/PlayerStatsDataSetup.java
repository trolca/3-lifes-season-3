package me.trololo11.lifespluginseason3.listeners.datasetups;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.managers.DatabaseManager;
import me.trololo11.lifespluginseason3.managers.LifesManager;
import me.trololo11.lifespluginseason3.utils.PlayerStats;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

/**
 * This class manages all of the database and server side saving of player stats. <br>
 * When player joins it gets it's stats from the sql database and assigns adds them to the
 * plugin's player stats hashMap and past that every modification of player lifes should be done
 * using the plugin instance. <br> <br>
 *
 * When player leaves the server it's values from the plugin's stats hashMap are saved to the sql
 * database and it's removed from the plugin's hashMap.
 */
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
