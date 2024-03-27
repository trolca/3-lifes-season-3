package me.trololo11.lifespluginseason3.listeners.datasetups;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.managers.DatabaseManager;
import me.trololo11.lifespluginseason3.managers.LifesManager;
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

/**
 * This class manages all of the database and server side saving of player quest progress. <br>
 * It gets all of the progress values from the database and goes through all of the active quests
 * and sets their progress for the player to the progress in the database. If a quest progress doesn't
 * exists in the database it sets it to 0. It also goes through all of the skipped quests
 * and if the player doesn't have one skipped it skips it for them. <br><br>
 *
 * When a player leaves their quest progress is saved to the database and removed
 * from the internal hash map in this plugin.
 */
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

        //Skips all the quests that are skipped for everyone
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
