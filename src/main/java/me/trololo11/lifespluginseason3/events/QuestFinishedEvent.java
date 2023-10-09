package me.trololo11.lifespluginseason3.events;

import me.trololo11.lifespluginseason3.utils.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class QuestFinishedEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final Player player;
    private final Quest quest;

    private final boolean showMessage;

    public QuestFinishedEvent(Player player, Quest quest, boolean showMessage){
        this.player = player;
        this.quest = quest;
        this.showMessage = showMessage;
    }

    public Player getPlayer() {
        return player;
    }

    public Quest getQuest() {
        return quest;
    }

    public boolean getShowMessage() {
        return showMessage;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
