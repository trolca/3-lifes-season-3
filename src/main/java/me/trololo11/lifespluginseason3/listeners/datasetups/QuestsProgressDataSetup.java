package me.trololo11.lifespluginseason3.listeners.datasetups;

import me.trololo11.lifespluginseason3.managers.DatabaseManager;
import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.QuestType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class QuestsProgressDataSetup implements Listener {

    private DatabaseManager databaseManager;
    private QuestManager questManager;
    public QuestsProgressDataSetup(DatabaseManager databaseManager, QuestManager questManager){
        this.databaseManager = databaseManager;
        this.questManager = questManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws SQLException {
        Player player = e.getPlayer();
        for(QuestType questType : QuestType.values()){
            setupPlayerQuestsProgress(questType, player);
        }
        questManager.calculatePlayerFinishedQuests(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) throws SQLException {
        Player player = e.getPlayer();

        for(QuestType questType : QuestType.values()){
            databaseManager.updateQuestDataForPlayer(questType, player, questManager);
        }

        for(Quest quest : questManager.getAllActiveQuests()){
            quest.removePlayerProgress(player);
        }

        questManager.removePlayerCompletedQuests(player);
    }

    public void setupPlayerQuestsProgress(QuestType questType, Player player) throws SQLException {
        HashMap<String, Integer> playerQuestData = databaseManager.getQuestsDataFromTable(questType, player.getUniqueId(), questManager);


        if(playerQuestData == null){
            databaseManager.addQuestDataForPlayer(questType, player, questManager);
            playerQuestData = databaseManager.getQuestsDataFromTable(questType, player.getUniqueId(), questManager);
        }

        ArrayList<Quest> activeQuests = questManager.getCorrespondingQuestArray(questType);

        for(Quest quest : activeQuests){
            quest.setPlayerProgress(player, playerQuestData.getOrDefault(quest.getDatabaseName(), 0));
        }

    }

}
