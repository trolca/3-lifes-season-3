package me.trololo11.lifespluginseason3.listeners;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.events.QuestFinishedEvent;
import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.managers.QuestsAwardsManager;
import me.trololo11.lifespluginseason3.utils.PlayerStats;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.QuestType;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * It listens for the {@link QuestFinishedEvent}. It is used
 * to print a message to the player that they've finished this quest.
 * Also, it adds it the finished quests value and checks if the player
 * unlocked any awards. It also adds to the player stats.
 */
public class QuestFinishedListener implements Listener {

    private QuestManager questManager;
    private QuestsAwardsManager questsAwardsManager;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public QuestFinishedListener(QuestManager questManager, QuestsAwardsManager questsAwardsManager){
        this.questManager = questManager;
        this.questsAwardsManager = questsAwardsManager;
    }
    @EventHandler
    public void onFinish(QuestFinishedEvent e){
        Player player = e.getPlayer();
        Quest quest = e.getQuest();
        QuestType questType = quest.getQuestType();
        boolean showMessage = e.getShowMessage();
        questManager.incrementPlayerFinishedQuests(player, questType);

        if(showMessage) {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            player.sendMessage(Utils.chat("&aWłaśnie ukończyłeś quest &f[" + quest.getName() + "&f]"));
        }

        //Sets the stats for the player
        PlayerStats playerStats = plugin.getPlayerStats(player);

        switch (questType){

            case DAILY -> playerStats.dailyQuestCompleted++;
            case WEEKLY -> playerStats.weeklyQuestCompleted++;
            case CARD -> playerStats.cardQuestCompleted++;
        }

        playerStats.allQuestCompleted++;

        int playerFinishedQuests = questManager.getPlayerFinishedQuests(player, questType);
        int questsPerAward = questsAwardsManager.getQuestsPerAward(questType);


        boolean isAward = playerFinishedQuests % questsPerAward == 0 || playerFinishedQuests >= questManager.getCorrespondingQuestArray(questType).size();

        if( isAward){
            if(questType != QuestType.CARD) player.sendMessage(ChatColor.GOLD + "Właśnie odblokowałeś kolejną "+getRewardWord(questType)+" nagrode!");
            else player.sendMessage(ChatColor.GOLD + "Właśnie odblokowałeś kolejna karte z questów!");
        }


    }

    private String getRewardWord(QuestType questType){

        switch (questType){

            case DAILY -> {
                return "dzienną";
            }

            case WEEKLY -> {
                return "tygodniową";
            }

            default -> {
                return "";
            }

        }


    }
}
