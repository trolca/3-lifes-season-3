package me.trololo11.lifespluginseason3;

import me.trololo11.lifespluginseason3.commands.GetItemsCommand;
import me.trololo11.lifespluginseason3.commands.LifesMenuCommand;
import me.trololo11.lifespluginseason3.commands.SetLifesCommand;
import me.trololo11.lifespluginseason3.commands.TakeLifeCommand;
import me.trololo11.lifespluginseason3.commands.tabcompleters.SetLifesTabCompleter;
import me.trololo11.lifespluginseason3.events.PlayerChangeLifesEvent;
import me.trololo11.lifespluginseason3.listeners.CustomItemsCraftingFix;
import me.trololo11.lifespluginseason3.listeners.MenuManager;
import me.trololo11.lifespluginseason3.listeners.revivelisteners.ReviveCardRenameListener;
import me.trololo11.lifespluginseason3.listeners.revivelisteners.ReviveCardUseListener;
import me.trololo11.lifespluginseason3.listeners.lifeslisteners.LifeUseListener;
import me.trololo11.lifespluginseason3.listeners.lifeslisteners.PlayerChangeLifesListener;
import me.trololo11.lifespluginseason3.listeners.lifeslisteners.PlayerDeathListener;
import me.trololo11.lifespluginseason3.listeners.datasetups.PlayerLifesDataSetup;
import me.trololo11.lifespluginseason3.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public final class LifesPlugin extends JavaPlugin {

    private TeamsManager teamsManager;
    private DatabaseManager databaseManager;
    private LifesManager lifesManager;
    private RecipesManager recipesManager;
    private QuestManager questManager;
    private QuestsTimingsManager questsTimingsManager;

    private boolean detailedErrors;
    private int tier=1;
    public Properties globalDbProperties;
    public Logger logger;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        detailedErrors = getConfig().getBoolean("detailed-errors");
        tier = getConfig().getInt("quest-tier");
        setupDbProperties();
        logger = Bukkit.getLogger();

        databaseManager = new DatabaseManager();

        try {
            databaseManager.initialize();
            lifesManager = new LifesManager(databaseManager.getAllDeadPlayers());

        } catch (SQLException e) {
            logger.severe("Error while connecting to the database");
            logger.severe("Make sure the info in config is accurate!");
            if(detailedErrors) e.printStackTrace();
            return;
        }

        try {
            setupDirs();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        questsTimingsManager = new QuestsTimingsManager();
        teamsManager = new TeamsManager();
        recipesManager = new RecipesManager();
        questManager = new QuestManager(databaseManager, questsTimingsManager);



        teamsManager.registerEverything();

        getServer().getPluginManager().registerEvents(new PlayerChangeLifesListener(lifesManager, teamsManager), this);
        getServer().getPluginManager().registerEvents(new PlayerLifesDataSetup(lifesManager, databaseManager), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(lifesManager), this);
        getServer().getPluginManager().registerEvents(new CustomItemsCraftingFix(recipesManager), this);
        getServer().getPluginManager().registerEvents(new LifeUseListener(lifesManager), this);
        getServer().getPluginManager().registerEvents(new MenuManager(), this);
        getServer().getPluginManager().registerEvents(new ReviveCardRenameListener(lifesManager), this);
        getServer().getPluginManager().registerEvents(new ReviveCardUseListener(lifesManager, databaseManager), this);

        try {
            setupData();
        } catch (SQLException e) {
            logger.warning("[LifesPluginS3] Error while setting up data!");
            if(detailedErrors) e.printStackTrace();
        }

        getCommand("getitems").setExecutor(new GetItemsCommand(recipesManager));
        getCommand("setlifes").setExecutor(new SetLifesCommand());
        getCommand("takelife").setExecutor(new TakeLifeCommand(lifesManager, recipesManager));
        getCommand("lifesmenu").setExecutor(new LifesMenuCommand());

        getCommand("setlifes").setTabCompleter(new SetLifesTabCompleter());



    }

    @Override
    public void onDisable(){

        for(Player player : Bukkit.getOnlinePlayers()){


            byte lifes = lifesManager.getPlayerLifes(player);

            try {
                databaseManager.updatePlayerLifes(player.getUniqueId(), lifes);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

        databaseManager.turnOffDatabase();

    }


    public void setupData() throws SQLException {

        for(Player player : Bukkit.getOnlinePlayers()){

            byte lifes = databaseManager.getPlayerLifes(player.getUniqueId());

            if(lifes == -11){
                lifes = 3;

                databaseManager.addPlayerLifes(player.getUniqueId(), lifes);
            }


            Bukkit.getPluginManager().callEvent(new PlayerChangeLifesEvent(player, lifes));

        }

    }


    public void setupDbProperties(){
        globalDbProperties = new Properties();
        globalDbProperties.setProperty("minimumIdle", "1");
        globalDbProperties.setProperty("maximumPoolSize", "4");
        globalDbProperties.setProperty("initializationFailTimeout", "20000");
    }

    private void setupDirs() throws IOException {
        String dataFolder = this.getDataFolder().getPath();

        File file = new File(dataFolder + "/quests-data");
        if (!file.exists()) file.mkdirs();

        file = new File(dataFolder + "/quests-data/all-daily");
        if (!file.exists()) file.mkdirs();

        createTiersForQuests("all-daily");

        file = new File(dataFolder + "/quests-data/all-weekly");
        if (!file.exists()) file.mkdirs();

        createTiersForQuests("all-weekly");

        file = new File(dataFolder + "/quests-data/all-card-quests");
        if (!file.exists()) file.mkdirs();

        createTiersForQuests("all-card-quests");

        file = new File(dataFolder + "/quests-data/active-daily");
        if (!file.exists()) file.mkdirs();

        file = new File(dataFolder + "/quests-data/active-weekly");
        if (!file.exists()) file.mkdirs();

        file = new File(dataFolder + "/quests-data/active-card-quests");
        if (!file.exists()) file.mkdirs();


        file = new File(dataFolder + "/quests-data/quests-timings.yml");
        if(!file.exists()) file.createNewFile();
    }

    private void createTiersForQuests(String questsName){
        String basicPath = this.getDataFolder() + "/quests-data/"+questsName;

        for(int i=0; i < 3; i++){
            File file = new File(basicPath + "/tier-"+(i+1));

            if(!file.exists()) file.mkdirs();
        }

    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        if(tier < 1) tier = 1;
        else if (tier > 3) tier = 3;
        this.tier = tier;
    }

    public static LifesPlugin getPlugin(){
        return LifesPlugin.getPlugin(LifesPlugin.class);
    }

    public boolean isDetailedErrors() {
        return detailedErrors;
    }
}
