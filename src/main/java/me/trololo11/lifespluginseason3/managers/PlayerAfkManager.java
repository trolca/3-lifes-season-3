package me.trololo11.lifespluginseason3.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerAfkManager {

    private ArrayList<Player> playersAfk = new ArrayList<>();
    private LifesManager lifesManager;
    private TeamsManager teamsManager;
    private ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();

    public PlayerAfkManager(LifesManager lifesManager, TeamsManager teamsManager){
        this.lifesManager = lifesManager;
        this.teamsManager = teamsManager;
    }

    public void setPlayerAfk(Player player){
        Scoreboard scoreboard = scoreboardManager.getMainScoreboard();
        Team playerAfkTeam = scoreboard.registerNewTeam(player.getName() + "_afk");

        playerAfkTeam.setColor(ChatColor.GRAY);
        playerAfkTeam.setSuffix(ChatColor.DARK_RED + " ("+lifesManager.getPlayerLifes(player)+")");

        playerAfkTeam.addEntry(player.getName());
        playersAfk.add(player);
    }



    public void setPlayerNotAfk(Player player){
        Scoreboard scoreboard = scoreboardManager.getMainScoreboard();
        Team playerAfkTeam = scoreboard.getTeam(player.getName() + "_afk");

        playersAfk.remove(player);

        if(playerAfkTeam == null) return;

        playerAfkTeam.removeEntry(player.getName());

        playerAfkTeam.unregister();

        byte lifes = lifesManager.getPlayerLifes(player);
        teamsManager.getLifesTeamList().get(lifes >= 3 ? lifes : 4).addEntry(player.getName());
    }


    public boolean isPlayerAfk(Player player){
        return playersAfk.contains(player);
    }
}
