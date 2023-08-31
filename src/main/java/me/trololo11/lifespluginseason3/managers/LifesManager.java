package me.trololo11.lifespluginseason3.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * This class manages how much lifes a plyer has on the server side. <br>
 * This data is stored in a hashMap.s
 * @see me.trololo11.lifespluginseason3.listeners.PlayerLifesDataSetup
 */
public class LifesManager {

    private final HashMap<Player, Byte> playerLifes = new HashMap<>();

    /**
     * Gets the stored lifes of a player.
     * @param player The player to get the lifes from.
     * @return How many lifes a player has
     */
    public byte getPlayerLifes(Player player){
        return playerLifes.getOrDefault(player,  (byte) 0);
    }

    /**
     * Sets a new amount of lifes for a player.
     * @param player The player to set the new amount of lifes for.
     * @param lifes The new amount of lifes
     */
    public void setPlayerLifes(Player player, byte lifes){
        playerLifes.put(player, lifes);
    }

    /**
     * Removes a specified player from the hashMap.
     * @param player The player to remove
     */
    public void removePlayerLifes(Player player){
        playerLifes.remove(player);
    }
}
