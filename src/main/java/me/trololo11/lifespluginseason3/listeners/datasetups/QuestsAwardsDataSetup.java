package me.trololo11.lifespluginseason3.listeners.datasetups;

import me.trololo11.lifespluginseason3.managers.DatabaseManager;
import me.trololo11.lifespluginseason3.managers.QuestsAwardsManager;
import me.trololo11.lifespluginseason3.utils.QuestType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.ArrayList;

public class QuestsAwardsDataSetup implements Listener {

    private QuestsAwardsManager questsAwardsManager;
    private DatabaseManager databaseManager;

    public QuestsAwardsDataSetup(QuestsAwardsManager questsAwardsManager, DatabaseManager databaseManager){
        this.questsAwardsManager = questsAwardsManager;
        this.databaseManager = databaseManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws SQLException {
        Player player = e.getPlayer();
        ArrayList<Byte> takenAwards = databaseManager.getPlayerTakenAwards(player.getUniqueId());
        if(takenAwards.isEmpty()){
            databaseManager.addPlayerTakenAwards(player.getUniqueId());
            takenAwards = databaseManager.getPlayerTakenAwards(player.getUniqueId());
        }

        questsAwardsManager.setAwardsTakenForPlayer(player, QuestType.DAILY, takenAwards.get(0));
        questsAwardsManager.setAwardsTakenForPlayer(player, QuestType.WEEKLY, takenAwards.get(1));
        questsAwardsManager.setAwardsTakenForPlayer(player, QuestType.CARD, takenAwards.get(2));

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) throws SQLException {
        Player player = e.getPlayer();
        databaseManager.updatePlayerTakenAwards(player.getUniqueId(),
                questsAwardsManager.getAwardsTakenForPlayer(player, QuestType.DAILY),
                questsAwardsManager.getAwardsTakenForPlayer(player, QuestType.WEEKLY),
                questsAwardsManager.getAwardsTakenForPlayer(player, QuestType.CARD));

        questsAwardsManager.removePlayerFromAllTakenAwards(player);
    }
}
