package me.trololo11.lifespluginseason3.managers;

import me.trololo11.lifespluginseason3.LifesPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class QuestsTimingsManager {

    private File file;
    private FileConfiguration fileConfig;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public Date getDailyEndDate() throws IOException {
        file = new File(plugin.getDataFolder()+"/quests-data/quests-timings.yml");
        checkFile(file);
        fileConfig = YamlConfiguration.loadConfiguration(file);
        long time = fileConfig.getLong("daily-date");

        return new Date(time);

    }

    public Date getWeeklyEndDate() throws IOException {
        file = new File(plugin.getDataFolder()+"/quests-data/quests-timings.yml");
        checkFile(file);
        fileConfig = YamlConfiguration.loadConfiguration(file);
        long time = fileConfig.getLong("weekly-date");


        return new Date(time);

    }

    public Date getCardEndDate() throws IOException{
        file = new File(plugin.getDataFolder()+"/quests-data/quests-timings.yml");
        checkFile(file);
        fileConfig = YamlConfiguration.loadConfiguration(file);
        long time = fileConfig.getLong("card-date");


        return new Date(time);
    }

    public void setDailyEndDate(Date date) throws IOException {
        file = new File(plugin.getDataFolder()+"/quests-data/quests-timings.yml");
        checkFile(file);
        fileConfig = YamlConfiguration.loadConfiguration(file);

        fileConfig.set("daily-date", date.getTime());
        fileConfig.save(file);
    }

    public void setCardEndDate(Date date) throws IOException {
        file = new File(plugin.getDataFolder()+"/quests-data/quests-timings.yml");
        checkFile(file);
        fileConfig = YamlConfiguration.loadConfiguration(file);

        fileConfig.set("card-date", date.getTime());
        fileConfig.save(file);
    }

    public void setWeeklyEndDate(Date date) throws IOException {
        file = new File(plugin.getDataFolder()+"/quests-data/quests-timings.yml");
        checkFile(file);
        fileConfig = YamlConfiguration.loadConfiguration(file);

        fileConfig.set("weekly-date", date.getTime());
        fileConfig.save(file);
    }


    private void checkFile(File file) throws IOException {
        if(!file.exists()){
            file.createNewFile();

            fileConfig = YamlConfiguration.loadConfiguration(file);
            fileConfig.set("weekly-date", new Date().getTime());
            fileConfig.set("daily-date", new Date().getTime());

            fileConfig.save(file);
        }

    }
}
