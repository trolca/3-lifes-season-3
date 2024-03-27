package me.trololo11.lifespluginseason3.tasks;

import me.trololo11.lifespluginseason3.managers.PlayerAfkManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is responsible for setting players afk using the {@link PlayerAfkManager}.<br>
 * It checks the player location if it's the same 2 times and if it is it sets them afk.
 * When a player moves and is afk then it sets them as not afk using {@link PlayerAfkManager#setPlayerNotAfk(Player)}
 */
public class CheckAfkPlayerTask extends BukkitRunnable implements Listener {

    private HashMap<Player, Location> playerLocations = new HashMap<>();
    private ArrayList<Player> checkPlayers = new ArrayList<>();
    private PlayerAfkManager playerAfkManager;

    public CheckAfkPlayerTask(PlayerAfkManager playerAfkManager){
        this.playerAfkManager = playerAfkManager;
    }

    @Override
    public void run() {

        for (Player player : Bukkit.getOnlinePlayers()) {


            if(playerLocations.get(player) == null){
                playerLocations.put(player, player.getLocation());
                continue;
            }
            if(playerAfkManager.isPlayerAfk(player)) continue;
            //checks if player saved location is the same as it's current location
            //and adds them into the check players array
            if (playerLocations.get(player).equals(player.getLocation()) && !checkPlayers.contains(player)) {
                checkPlayers.add(player);
            }else if(checkPlayers.contains(player) && playerLocations.get(player).equals(player.getLocation())){
                playerAfkManager.setPlayerAfk(player);
                checkPlayers.remove(player);
            }else checkPlayers.remove(player);

            playerLocations.put(player, player.getLocation());


        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        playerLocations.put(e.getPlayer(), e.getPlayer().getLocation());
    }

    /**
     * When a player moves it sets them no afk
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){

        playerAfkManager.setPlayerNotAfk(e.getPlayer());
        playerLocations.put(e.getPlayer(), e.getPlayer().getLocation());
    }

    /**
     * When a player quits the server it makes them not afk.
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){

        playerAfkManager.setPlayerNotAfk(e.getPlayer());
        playerLocations.remove(e.getPlayer());
    }

}
