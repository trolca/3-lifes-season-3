package me.trololo11.lifespluginseason3.managers;

import me.trololo11.lifespluginseason3.LifesPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * This class manages the timing of the random card that players can
 * get every week. <br>
 * It mostly saves the times to the weekly-card-timing.yml file in the
 * plugin data folder
 */
public class CardTimingsManager {

    //We get the files only once to save the time of getting the files
    private final File file;
    private final FileConfiguration fileConfig;

    public CardTimingsManager() throws IOException {
        LifesPlugin plugin = LifesPlugin.getPlugin();
        file = new File(plugin.getDataFolder() + "/quests-data/weekly-card-timing.yml");
        if(!file.exists()) {
            file.createNewFile();
            fileConfig = YamlConfiguration.loadConfiguration(file);
            fileConfig.set("card-time", new Date().getTime() - 1);
            fileConfig.save(file);
        }else{
            fileConfig = YamlConfiguration.loadConfiguration(file);
        }

    }

    /**
     * Returns the time in epoch that the players can reedem the cards
     * @return The time in epoch
     */
    public long getCardEndTime(){
         return fileConfig.getLong("card-time");
    }

    /**
     * Sets the time in epoch when the player reedem the cards
     * @param time The time in epoch
     * @throws IOException On yml saving error
     */
    public void setCardEndTime(long time) throws IOException {
        fileConfig.set("card-time", time);

        fileConfig.save(file);
    }
}
