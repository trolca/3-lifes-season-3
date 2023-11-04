package me.trololo11.lifespluginseason3.utils;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public abstract class QuestListener implements Listener {

    protected QuestManager questManager;

    public QuestListener(QuestManager questManager){
        this.questManager = questManager;
    }

    public abstract ListenerType getThisListenerType();
    protected void checkTarget(Object checkTarget, Player player){
        ArrayList<Quest> checkQuests = questManager.getListenerTypesQuests().get(getThisListenerType());

        for(Quest quest : checkQuests){
            if ((quest.getTargets().contains(checkTarget) || quest.getTargets() == null || quest.getTargets().isEmpty()) && !quest.hasFinished(player)) {

                quest.setPlayerProgress(player, quest.getPlayerProgress(player) + 1);

            }

        }

    }

    protected void checkTarget(Object checkTarget, Player player, int customAddProgress){
        ArrayList<Quest> checkQuests = questManager.getListenerTypesQuests().get(getThisListenerType());

        for(Quest quest : checkQuests){

            if ((quest.getTargets().contains(checkTarget) || quest.getTargets() == null || quest.getTargets().isEmpty()) && !quest.hasFinished(player)) {

                quest.setPlayerProgress(player, quest.getPlayerProgress(player) + customAddProgress);

            }

        }

    }
}
