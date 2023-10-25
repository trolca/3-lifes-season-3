package me.trololo11.lifespluginseason3.managers;

import me.trololo11.lifespluginseason3.listeners.datasetups.PlayerLifesDataSetup;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerProfile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class manages how much lifes a player has on the server side. <br>
 * This data is stored in a hashMap.
 * @see PlayerLifesDataSetup
 */
public class LifesManager {

    private final HashMap<Player, Byte> playerLifes = new HashMap<>();
    private final ArrayList<OfflinePlayer> deadPlayers;
    private final DatabaseManager databaseManager;

    public LifesManager(ArrayList<OfflinePlayer> allDeadPlayers, DatabaseManager databaseManager){
        this.deadPlayers = allDeadPlayers;
        this.databaseManager = databaseManager;
    }


    /**
     * Gets the stored lifes of a player.
     * @param player The player to get the lifes from.
     * @return How many lifes a player has
     */
    public byte getPlayerLifes(Player player){
        return playerLifes.get(player);
    }

    /**
     * Sets a new amount of lifes for a player.
     * @param player The player to set the new amount of lifes for.
     * @param lifes The new amount of lifes
     */
    public void setPlayerLifes(Player player, byte lifes){
        if(lifes <= 0) deadPlayers.add(player);
        else deadPlayers.remove(player);

        playerLifes.put(player, lifes);
    }

    /**
     * Removes a specified player from the hashMap.
     * @param player The player to remove
     */
    public void removePlayerLifes(Player player){
        playerLifes.remove(player);
    }

    /**
     * Removes a plyer from the deadPlayer arrayList
     * @param player The player to remove
     */
    public void removeDeadPlayer(OfflinePlayer player){
        deadPlayers.remove(player);
    }

    /**
     * Revives the specified player. <br>
     * This means: <br>
     * Removes the specified player from the deadPlayers ArrayList, sets the
     * player to be given 2 lifes on join and removes them from the banned list
     * @param deadPlayer The player to revive
     * @throws SQLException On error while setting the revivning on join value
     */
    public void revivePlayer(OfflinePlayer deadPlayer, byte lifesAfterRevive) throws SQLException {
        databaseManager.setIsRevived(deadPlayer.getUniqueId(), true);
        databaseManager.updatePlayerLifes(deadPlayer.getUniqueId(), lifesAfterRevive);
        removeDeadPlayer(deadPlayer);
        BanList<PlayerProfile> banList = Bukkit.getBanList(BanList.Type.PROFILE);

        banList.pardon(deadPlayer.getPlayerProfile());
    }

    public ArrayList<OfflinePlayer> getDeadPlayers() {
        return deadPlayers;
    }

}
