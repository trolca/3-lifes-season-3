package me.trololo11.lifespluginseason3.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event is called when a player's amount of lives are changed.
 */
public class PlayerChangeLifesEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private Player player;
    private byte newLifes;

    public PlayerChangeLifesEvent(Player player, byte newLifes){
        this.player = player;
        this.newLifes = newLifes;
    }

    /**
     * Returns the player for whom the amount of lives is changed
     * @return The player who's got their lives changed
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * The new amount of lives that the player lives were changed into
     * @return The new amount of lives
     */
    public byte getNewLifes() {
        return newLifes;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
