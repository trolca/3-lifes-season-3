package me.trololo11.lifespluginseason3.listeners.lifeslisteners;

import me.trololo11.lifespluginseason3.events.PlayerChangeLifesEvent;
import me.trololo11.lifespluginseason3.managers.LifesManager;
import me.trololo11.lifespluginseason3.managers.TeamsManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Date;

public class PlayerChangeLifesListener implements Listener {

    private LifesManager lifesManager;
    private TeamsManager teamsManager;

    public PlayerChangeLifesListener(LifesManager lifesManager, TeamsManager teamsManager){
        this.lifesManager = lifesManager;
        this.teamsManager = teamsManager;
    }

    @EventHandler
    public void onLifeChange(PlayerChangeLifesEvent e){
        Player player = e.getPlayer();
        byte lifes = e.getNewLifes();

        lifesManager.setPlayerLifes(player, lifes);

        if(lifes <= 0){
            player.ban(ChatColor.RED + "UMARŁEŚ FFFF", (Date) null, null);
            return;
        }

        if(lifes <= 3) teamsManager.getLifesTeamList().get(lifes).addEntry(player.getName());
        else teamsManager.getLifesTeamList().get(4).addEntry(player.getName());


    }


}
