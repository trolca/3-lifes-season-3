package me.trololo11.lifespluginseason3.listeners;

import me.trololo11.lifespluginseason3.events.PlayerChangeLifesEvent;
import me.trololo11.lifespluginseason3.managers.DatabaseManager;
import me.trololo11.lifespluginseason3.managers.LifesManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

/**
 * This class manages all of the database and server side saving of player lifes. <br>
 * When player joins it gets it's lifes from the sql database and assings adds them to the
 * {@link LifesManager} hashMap and past that every modification of player lifes should be done
 * using {@link LifesManager}. <br> <br>
 *
 * When player leaves the server it's values from the LifesManager hashMap are saved to the sql
 * database and it's removed from the {@link LifesManager} hashMap.
 */
public class PlayerLifesDataSetup implements Listener {

    private LifesManager lifesManager;
    private DatabaseManager databaseManager;

    public PlayerLifesDataSetup(LifesManager lifesManager, DatabaseManager databaseManager){
        this.lifesManager = lifesManager;
        this.databaseManager = databaseManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws SQLException {
        Player player = e.getPlayer();
        byte lifes = databaseManager.getPlayerLifes(player.getUniqueId());

        if(lifes == -11){
            lifes = 3;

            databaseManager.addPlayerLifes(player.getUniqueId(), lifes);
        }

        Bukkit.getPluginManager().callEvent(new PlayerChangeLifesEvent(player, lifes));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) throws SQLException {
        Player player = e.getPlayer();


        byte lifes = lifesManager.getPlayerLifes(player);

        databaseManager.updatePlayerLifes(player.getUniqueId(), lifes);

        lifesManager.removePlayerLifes(player);
    }

}
