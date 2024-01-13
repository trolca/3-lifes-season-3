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

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){

        playerAfkManager.setPlayerNotAfk(e.getPlayer());
        playerLocations.put(e.getPlayer(), e.getPlayer().getLocation());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){

        playerAfkManager.setPlayerNotAfk(e.getPlayer());
        playerLocations.remove(e.getPlayer());
    }

}
