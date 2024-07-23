package me.trololo11.lifespluginseason3.managers;

import me.trololo11.lifespluginseason3.tasks.CheckAfkPlayerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;

/**
 * This class manages the custom afk logic that is optional.<br>
 * When a player is not moving for more than 5 minutes they names become gray
 * and it tells other players how much lives do they have. <br>
 * This class is responsible for saving every player that is afk and
 * changing their name to show that they are afk.
 * @see CheckAfkPlayerTask
 */
public class PlayerAfkManager {

    private ArrayList<Player> playersAfk = new ArrayList<>();
    private LifesManager lifesManager;
    private TeamsManager teamsManager;
    private ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();

    public PlayerAfkManager(LifesManager lifesManager, TeamsManager teamsManager){
        this.lifesManager = lifesManager;
        this.teamsManager = teamsManager;
    }

    /**
     * Adds a player to a new afk team and changes their nick
     * to a gray color with the amount of lifes they have.
     * @param player The player to set afk
     */
    public void setPlayerAfk(Player player){
        Scoreboard scoreboard = scoreboardManager.getMainScoreboard();
        Team playerAfkTeam = scoreboard.getTeam(player.getName() + "_afk") == null ? scoreboard.registerNewTeam(player.getName() + "_afk") :
                scoreboard.getTeam(player.getName() + "_afk");

        assert playerAfkTeam != null;
        playerAfkTeam.setColor(ChatColor.GRAY);
        playerAfkTeam.setSuffix(ChatColor.DARK_RED + " ("+lifesManager.getPlayerLifes(player)+")");

        playerAfkTeam.addEntry(player.getName());
        playersAfk.add(player);
    }


    /**
     * Removes a player from being afk. <br>
     * It removes them from their afk team and adds them to the corresponding
     * lifes team to make them normal.
     * @param player The player to remove from being afk
     */
    public void setPlayerNotAfk(Player player){
        Scoreboard scoreboard = scoreboardManager.getMainScoreboard();
        Team playerAfkTeam = scoreboard.getTeam(player.getName() + "_afk");

        playersAfk.remove(player);

        if(playerAfkTeam == null) return;

        playerAfkTeam.removeEntry(player.getName());

        playerAfkTeam.unregister();

        byte lifes = lifesManager.getPlayerLifes(player);
        teamsManager.getLifesTeamList().get(lifes > 3 ? 4 : lifes).addEntry(player.getName());
    }

    /**
     * Gets if the specified player is afk.
     * @param player The player to check.
     * @return If the player is afk.
     */
    public boolean isPlayerAfk(Player player){
        return playersAfk.contains(player);
    }
}
