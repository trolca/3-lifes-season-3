package me.trololo11.lifespluginseason3.managers;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.utils.QuestType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * This class manages the getting and storing the time when the
 * specific quest types quests are going to reset. <br>
 * These values are stored in an external YAML file. And
 * they are epoch values which specify the date
 * when the quests are due to be reset.
 */
public class QuestsTimingsManager {

    private File file;
    private FileConfiguration fileConfig;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    /**
     * Gets the date when quests for the specified quest type
     * are going to be reset.
     * @param questType The quest type to get the date for.
     * @return The date of quests reset.
     * @throws IOException On error while reading the file.
     */
    public Date getEndDate(QuestType questType) throws IOException{
        file = new File(plugin.getDataFolder()+"/quests-data/quests-timings.yml");
        checkFile(file);
        fileConfig = YamlConfiguration.loadConfiguration(file);
        long time = fileConfig.getLong(getPathName(questType));

        return new Date(time);
    }

    /**
     * Sets a new date when the quests are going to be reset for
     * the specified quest type. <br>
     * The info is going to be stored as an epoch timestamp in an external YAML file
     * @param date The new quests reset date.
     * @param questType The quest types to set the reset time for
     * @throws IOException On error while reading the file.
     */
    public void setEndDate(Date date, QuestType questType) throws IOException{
        file = new File(plugin.getDataFolder()+"/quests-data/quests-timings.yml");
        checkFile(file);
        fileConfig = YamlConfiguration.loadConfiguration(file);

        fileConfig.set(getPathName(questType), date.getTime());
        fileConfig.save(file);
    }

    /**
     * Returns the name of the path where the reset date is stored
     * as an epoch timestamp.
     * @param questType The quest type to get the path for.
     * @return The name of the path.
     */
    private String getPathName(QuestType questType){

        switch (questType){

            case DAILY -> {
                return "daily-date";
            }

            case WEEKLY -> {
                return "weekly-date";
            }

            case CARD -> {
                return "card-date";
            }

            default -> {
                return "generic-date";
            }
        }

    }


    private void checkFile(File file) throws IOException {
        if(!file.exists()){
            file.createNewFile();

            fileConfig = YamlConfiguration.loadConfiguration(file);
            fileConfig.set("weekly-date", new Date().getTime());
            fileConfig.set("daily-date", new Date().getTime());
            fileConfig.set("card-date", new Date().getTime());

            fileConfig.save(file);
        }

    }
}
