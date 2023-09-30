package me.trololo11.lifespluginseason3.managers;

import me.trololo11.lifespluginseason3.listeners.datasetups.PlayerLifesDataSetup;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class manages how much lifes a plyer has on the server side. <br>
 * This data is stored in a hashMap.
 * @see PlayerLifesDataSetup
 */
public class LifesManager {

    private final HashMap<Player, Byte> playerLifes = new HashMap<>();
    private final ArrayList<OfflinePlayer> deadPlayers;

    public LifesManager(ArrayList<OfflinePlayer> allDeadPlayers){
        this.deadPlayers = allDeadPlayers;
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

    public ArrayList<OfflinePlayer> getDeadPlayers() {
        return deadPlayers;
    }

}
