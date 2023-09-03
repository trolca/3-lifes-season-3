package me.trololo11.lifespluginseason3.listeners.lifeslisteners;

import me.trololo11.lifespluginseason3.events.PlayerChangeLifesEvent;
import me.trololo11.lifespluginseason3.managers.LifesManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private LifesManager lifesManager;

    public PlayerDeathListener(LifesManager lifesManager){
        this.lifesManager = lifesManager;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){

        Player player = e.getEntity();

        Bukkit.getPluginManager().callEvent(new PlayerChangeLifesEvent(player, (byte) (lifesManager.getPlayerLifes(player)-1)));
    }
}