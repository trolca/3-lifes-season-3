package me.trololo11.lifespluginseason3.listeners;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.utils.PlayerStats;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * This class listens if a player killed another player.
 * It is used to add to the{@link PlayerStats#kills} stat.
 */
public class PlayerKillListener implements Listener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @EventHandler
    public void onKill(PlayerDeathEvent e){
        Player dead = e.getEntity();
        if(dead.getKiller() == null) return;
        Player killer = dead.getKiller();


        plugin.getPlayerStats(killer).kills += 1;

    }
}
