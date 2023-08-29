package me.trololo11.lifespluginseason3.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class LifesManager {

    private final HashMap<Player, Byte> playerLifes = new HashMap<>();

    public byte getPlayerLifes(Player player){
        return playerLifes.getOrDefault(player,  (byte) 0);
    }

    public void setPlayerLifes(Player player, byte lifes){
        playerLifes.put(player, lifes);
    }

    public void removePlayerLifes(Player player){
        playerLifes.remove(player);
    }
}
