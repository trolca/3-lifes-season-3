package me.trololo11.lifespluginseason3.tasks;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.managers.CardTimingsManager;
import me.trololo11.lifespluginseason3.managers.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class WeeklyCardResetTask extends BukkitRunnable {

    private CardTimingsManager cardTimingsManager;
    private DatabaseManager databaseManager;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public WeeklyCardResetTask(CardTimingsManager cardTimingsManager, DatabaseManager databaseManager){
        this.cardTimingsManager = cardTimingsManager;
        this.databaseManager = databaseManager;
    }

    @Override
    public void run() {

        try {

            cardTimingsManager.setCardEndTime(new Date().getTime()+604800000);
            databaseManager.resetTakenWeeklyCardForAll();

            for(Player player : Bukkit.getOnlinePlayers()){
                player.sendMessage(ChatColor.GREEN + "Możesz odebrać nową karte używając /getcard!");
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            }

            long timeWeeklyReset = (cardTimingsManager.getCardEndTime()-new Date().getTime())/50 ;
            if(timeWeeklyReset < 0 ) timeWeeklyReset = 0; //We cannot have negite delay in tasks so we need to check if it is

            new WeeklyCardResetTask(cardTimingsManager, databaseManager).runTaskLater(plugin, timeWeeklyReset);

        } catch (IOException | SQLException e) {
            plugin.logger.severe("[3LifesS3] Error while reseting the the weekly card timings!");
            if(plugin.isDetailedErrors()) e.printStackTrace(System.out);
        }

    }
}
