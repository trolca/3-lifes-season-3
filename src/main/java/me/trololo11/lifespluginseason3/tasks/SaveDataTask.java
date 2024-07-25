package me.trololo11.lifespluginseason3.tasks;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.managers.DatabaseManager;
import me.trololo11.lifespluginseason3.managers.LifesManager;
import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.managers.QuestsAwardsManager;
import me.trololo11.lifespluginseason3.utils.Menu;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.QuestType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public class SaveDataTask extends BukkitRunnable {

    private LifesPlugin plugin = LifesPlugin.getPlugin();
    private DatabaseManager databaseManager;
    private LifesManager lifesManager;
    private QuestsAwardsManager questsAwardsManager;
    private QuestManager questManager;

    public SaveDataTask(DatabaseManager databaseManager, LifesManager lifesManager, QuestsAwardsManager questsAwardsManager, QuestManager questManager){
        this.databaseManager = databaseManager;
        this.questManager = questManager;
        this.lifesManager = lifesManager;
        this.questsAwardsManager = questsAwardsManager;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        for(Player player : Bukkit.getOnlinePlayers()){

            if(player.getOpenInventory().getTopInventory().getHolder() instanceof Menu) player.closeInventory();
            byte lifes = lifesManager.getPlayerLifes(player);

            try {
                databaseManager.updatePlayerLifes(player.getUniqueId(), lifes);

                for(QuestType questType : QuestType.values()){
                    databaseManager.updateQuestDataForPlayer(questType, player, questManager);
                }

                databaseManager.updatePlayerTakenAwards(player.getUniqueId(),
                        questsAwardsManager.getAwardsTakenForPlayer(player, QuestType.DAILY),
                        questsAwardsManager.getAwardsTakenForPlayer(player, QuestType.WEEKLY),
                        questsAwardsManager.getAwardsTakenForPlayer(player, QuestType.CARD));

                databaseManager.updatePlayerStats(plugin.getPlayerStats(player));

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        for(Quest quest : questManager.getAllActiveQuests()){

            if(quest.isHalfed()) {
                try {
                    databaseManager.addRequirementsQuests(quest);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }

        Bukkit.getLogger().info("Successfully saved players data!");
    }
}
