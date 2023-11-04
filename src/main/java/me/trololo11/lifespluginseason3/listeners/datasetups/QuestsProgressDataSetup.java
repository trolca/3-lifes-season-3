package me.trololo11.lifespluginseason3.listeners.datasetups;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.managers.DatabaseManager;
import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.QuestType;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class QuestsProgressDataSetup implements Listener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();
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

        //Skips all of the quests that are skipped for everyone
        for(Quest quest : plugin.getAllSkippedQuests()){

            if(!quest.hasFinished(player)){
                quest.setSilentPlayerProgress(player, quest.getMaxProgress());
                player.sendMessage(Utils.chat("&eQuest &f["+quest.getName()+"&f] &ezostał pominięty!"));
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            }

        }


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
