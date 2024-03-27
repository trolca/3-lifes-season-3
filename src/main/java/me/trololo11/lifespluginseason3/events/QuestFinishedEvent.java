package me.trololo11.lifespluginseason3.events;

import me.trololo11.lifespluginseason3.utils.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Thrown when a player completes a {@link Quest}.
 */
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

    /**
     * Returns the player that completed the quest.
     * @return The player that completed the quest.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the quest that has been completed.
     * @return The quest that has been completed.
     */
    public Quest getQuest() {
        return quest;
    }

    /**
     * Returns whether a message should be sent to the player that they completed this quest.
     * @return Whether a message should be sent to the player that they completed this quest.
     */
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
