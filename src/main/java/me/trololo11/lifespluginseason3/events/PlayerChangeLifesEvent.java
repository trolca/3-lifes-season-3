package me.trololo11.lifespluginseason3.events;

import me.trololo11.lifespluginseason3.managers.LifesManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerChangeLifesEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private Player player;
    private byte newLifes;

    public PlayerChangeLifesEvent(Player player, byte newLifes){
        this.player = player;
        this.newLifes = newLifes;
    }

    public Player getPlayer() {
        return player;
    }

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
