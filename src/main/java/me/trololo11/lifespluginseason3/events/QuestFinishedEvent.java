package me.trololo11.lifespluginseason3.events;

import me.trololo11.lifespluginseason3.utils.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class QuestFinishedEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final Player player;
    private final Quest quest;

    public QuestFinishedEvent(Player player, Quest quest){
        this.player = player;
        this.quest = quest;
    }

    public Player getPlayer() {
        return player;
    }

    public Quest getQuest() {
        return quest;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
