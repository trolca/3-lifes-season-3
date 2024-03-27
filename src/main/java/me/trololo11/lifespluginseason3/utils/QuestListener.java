package me.trololo11.lifespluginseason3.utils;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;

/**
 * A blueprint for every listener that is tasked with listening
 * for a specified {@link ListenerType}.
 */
public abstract class QuestListener implements Listener {

    protected QuestManager questManager;

    public QuestListener(QuestManager questManager){
        this.questManager = questManager;
    }

    /**
     * Gets this {@link ListenerType} that its listening for.
     * @return The {@link ListenerType} of this class.
     */
    public abstract ListenerType getThisListenerType();


    /**
     * Checks if the specified object is equal to any of the targets of every quest
     * in {@link ListenerType} category, and if it is it adds 1 to
     * the player quest progress.<br>
     *
     * If the target list is null it is going to add 1 to the progress anyways.
     * @param checkTarget The object to check if equal with any of the targets in the quests.
     * @param player The player to add the progress for.
     */
    protected void checkTarget(Object checkTarget, Player player){
        checkTarget(checkTarget, player, 1);
    }

    /**
     * Checks if the specified object is equal to any of the targets of every quest
     * in {@link ListenerType} category, and if it is it adds the specified progress to
     * the player quest progress.<br>
     *
     * If the target list is null it is going to add the specified progress anyways.
     *
     * @param checkTarget The object to check if equal with any of the targets in the quests.
     * @param player The player to add the progress for.
     * @param customAddProgress How much progress to add on correct check
     */
    protected void checkTarget(Object checkTarget, Player player, int customAddProgress){
        ArrayList<Quest> checkQuests = questManager.getListenerTypesQuests().get(getThisListenerType());

        for(Quest quest : checkQuests){

            if ((quest.getTargets().contains(checkTarget) || quest.getTargets() == null || quest.getTargets().isEmpty()) && !quest.hasFinished(player)) {

                quest.setPlayerProgress(player, quest.getPlayerProgress(player) + customAddProgress);

            }

        }

    }
}
