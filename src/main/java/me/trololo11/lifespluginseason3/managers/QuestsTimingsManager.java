package me.trololo11.lifespluginseason3.managers;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.utils.QuestType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class QuestsTimingsManager {

    private File file;
    private FileConfiguration fileConfig;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public Date getEndDate(QuestType questType) throws IOException{
        file = new File(plugin.getDataFolder()+"/quests-data/quests-timings.yml");
        checkFile(file);
        fileConfig = YamlConfiguration.loadConfiguration(file);
        long time = fileConfig.getLong(getPathName(questType));

        return new Date(time);
    }

    public void setEndDate(Date date, QuestType questType) throws IOException{
        file = new File(plugin.getDataFolder()+"/quests-data/quests-timings.yml");
        checkFile(file);
        fileConfig = YamlConfiguration.loadConfiguration(file);

        fileConfig.set(getPathName(questType), date.getTime());
        fileConfig.save(file);
    }

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
