package me.trololo11.lifespluginseason3.listeners;

import me.trololo11.lifespluginseason3.LifesPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

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
