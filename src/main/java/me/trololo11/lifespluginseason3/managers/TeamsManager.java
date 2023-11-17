package me.trololo11.lifespluginseason3.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;

/**
 * This class manages the teams that are required to change the color of players nicks to define how many lifes they have. <br>
 * It stores them in a ArrayList and the index of each team corresponds to players lifes (if they have more than three the team in index 4 is used)
 */
public class TeamsManager {

    private Team oneLife;
    private Team twoLifes;
    private Team threeLifes;
    private Team moreLifes;
    private Team dead;
    private ArrayList<Team> lifesTeamList = new ArrayList<>();

    public TeamsManager(){
        registerEverything();
    }

    /**
     * Registeres all of the teams to the main scoreboard.
     */
    public void registerEverything() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard teamScoreboard = scoreboardManager.getMainScoreboard();

        dead = teamScoreboard.getTeam("dead");
        if (dead == null) dead = teamScoreboard.registerNewTeam("dead");
        dead.setColor(ChatColor.DARK_RED);
        dead.setPrefix("" + ChatColor.DARK_RED);
        lifesTeamList.add(dead);

        oneLife = teamScoreboard.getTeam("oneLife");
        if (oneLife == null) oneLife = teamScoreboard.registerNewTeam("oneLife");
        oneLife.setColor(ChatColor.RED);
        oneLife.setPrefix("" + ChatColor.RED);
        lifesTeamList.add(oneLife);

        twoLifes = teamScoreboard.getTeam("twoLifes");
        if (twoLifes == null) twoLifes = teamScoreboard.registerNewTeam("twoLifes");
        twoLifes.setColor(ChatColor.YELLOW);
        twoLifes.setPrefix("" + ChatColor.YELLOW);
        lifesTeamList.add(twoLifes);

        threeLifes = teamScoreboard.getTeam("threeLifes");
        if (threeLifes == null) threeLifes = teamScoreboard.registerNewTeam("threeLifes");
        threeLifes.setColor(ChatColor.GREEN);
        threeLifes.setPrefix("" + ChatColor.GREEN);
        lifesTeamList.add(threeLifes);

        moreLifes = teamScoreboard.getTeam("moreLifes");
        if (moreLifes == null) moreLifes = teamScoreboard.registerNewTeam("moreLifes");
        moreLifes.setColor(ChatColor.DARK_GREEN);
        moreLifes.setPrefix("" + ChatColor.DARK_GREEN);
        lifesTeamList.add(moreLifes);



    }

    public ArrayList<Team> getLifesTeamList() {
        return lifesTeamList;
    }

}
