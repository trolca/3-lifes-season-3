package me.trololo11.lifespluginseason3;

import me.trololo11.lifespluginseason3.commands.*;
import me.trololo11.lifespluginseason3.commands.tabcompleters.SetLifesTabCompleter;
import me.trololo11.lifespluginseason3.commands.tabcompleters.SetProgressTabCompleter;
import me.trololo11.lifespluginseason3.events.PlayerChangeLifesEvent;
import me.trololo11.lifespluginseason3.listeners.CustomItemsCraftingFix;
import me.trololo11.lifespluginseason3.listeners.GoldLifeMenuExitListener;
import me.trololo11.lifespluginseason3.listeners.MenuManager;
import me.trololo11.lifespluginseason3.listeners.QuestFinishedListener;
import me.trololo11.lifespluginseason3.listeners.cardlisteners.*;
import me.trololo11.lifespluginseason3.listeners.datasetups.QuestsAwardsDataSetup;
import me.trololo11.lifespluginseason3.listeners.datasetups.QuestsProgressDataSetup;
import me.trololo11.lifespluginseason3.listeners.lifeslisteners.GoldLifeUseListener;
import me.trololo11.lifespluginseason3.listeners.questslisteners.BreakBlocksListener;
import me.trololo11.lifespluginseason3.listeners.revivelisteners.ReviveCardRenameListener;
import me.trololo11.lifespluginseason3.listeners.revivelisteners.ReviveCardUseListener;
import me.trololo11.lifespluginseason3.listeners.lifeslisteners.LifeUseListener;
import me.trololo11.lifespluginseason3.listeners.lifeslisteners.PlayerChangeLifesListener;
import me.trololo11.lifespluginseason3.listeners.lifeslisteners.PlayerDeathListener;
import me.trololo11.lifespluginseason3.listeners.datasetups.PlayerLifesDataSetup;
import me.trololo11.lifespluginseason3.managers.*;
import me.trololo11.lifespluginseason3.tasks.WeeklyCardResetTask;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.QuestType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

public final class LifesPlugin extends JavaPlugin {

    private TeamsManager teamsManager;
    private DatabaseManager databaseManager;
    private LifesManager lifesManager;
    private RecipesManager recipesManager;
    private QuestManager questManager;
    private QuestsTimingsManager questsTimingsManager;
    private QuestsProgressDataSetup questsProgressDataSetup;
    private QuestsAwardsManager questsAwardsManager;
    private CardManager cardManager;
    private CardTimingsManager cardTimingsManager;
    private ArrayList<Quest> allSkippedQuests;

    private boolean detailedErrors;
    private int tier=1;
    public int dailyQuestsCount=6;
    public int weeklyQuestsCount=10;
    public int cardQuestsCount=11;
    public Properties globalDbProperties;
    public Logger logger;
    public final String loggerPerfix = "[LifesPluginS3]";

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        detailedErrors = getConfig().getBoolean("detailed-errors");
        tier = getConfig().getInt("quest-tier");
        dailyQuestsCount = getConfig().getInt("daily-quests-count");
        weeklyQuestsCount = getConfig().getInt("weekly-quests-count");
        cardQuestsCount = getConfig().getInt("card-quests-count");
        setupDbProperties();
        logger = Bukkit.getLogger();

        try {
            cardTimingsManager = new CardTimingsManager();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        databaseManager = new DatabaseManager();


        try {
            databaseManager.initialize();
            lifesManager = new LifesManager(databaseManager.getAllDeadPlayers(), databaseManager);


        } catch (SQLException e) {
            logger.severe("Error while connecting to the database");
            logger.severe("Make sure the info in config is accurate!");
            if(detailedErrors) e.printStackTrace();
            return;
        }

        try {
            setupDirs();
            cardTimingsManager = new CardTimingsManager();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        questsTimingsManager = new QuestsTimingsManager();
        teamsManager = new TeamsManager();
        recipesManager = new RecipesManager();
        questsAwardsManager = new QuestsAwardsManager();
        questManager = new QuestManager(databaseManager, questsTimingsManager, questsAwardsManager);
        questsProgressDataSetup = new QuestsProgressDataSetup(databaseManager, questManager);
        cardManager = new CardManager();


        try{
            allSkippedQuests = databaseManager.getAllSkippedQuests(questManager);
        }catch (SQLException e){
            throw new RuntimeException("Error while getting the skipped quests from the database");
        }

        teamsManager.registerEverything();

        //Registering general listeners used in plugin
        getServer().getPluginManager().registerEvents(new PlayerChangeLifesListener(lifesManager, teamsManager), this);
        getServer().getPluginManager().registerEvents(new PlayerLifesDataSetup(lifesManager, databaseManager), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(lifesManager), this);
        getServer().getPluginManager().registerEvents(new CustomItemsCraftingFix(recipesManager), this);
        getServer().getPluginManager().registerEvents(new LifeUseListener(lifesManager), this);
        getServer().getPluginManager().registerEvents(new MenuManager(), this);
        getServer().getPluginManager().registerEvents(new ReviveCardRenameListener(lifesManager), this);
        getServer().getPluginManager().registerEvents(new ReviveCardUseListener(lifesManager, databaseManager), this);
        getServer().getPluginManager().registerEvents(questsProgressDataSetup, this);
        getServer().getPluginManager().registerEvents(new QuestsAwardsDataSetup(questsAwardsManager, databaseManager), this);
        getServer().getPluginManager().registerEvents(new QuestFinishedListener(questManager, questsAwardsManager), this);
        getServer().getPluginManager().registerEvents(new GoldLifeMenuExitListener(recipesManager), this);
        getServer().getPluginManager().registerEvents(new GoldLifeUseListener(lifesManager), this);

        //Registering the listeners that are used for cards
        getServer().getPluginManager().registerEvents(new TakeLifeCardListener(cardManager, lifesManager, recipesManager), this);
        getServer().getPluginManager().registerEvents(new GiveLifeCardUseListener(cardManager, lifesManager), this);
        getServer().getPluginManager().registerEvents(new SkipQuestsCardListener(questManager), this);
        getServer().getPluginManager().registerEvents(new SkipRandomQuestListener(questManager, databaseManager), this);
        getServer().getPluginManager().registerEvents(new GoldCardUseListener(recipesManager), this);
        getServer().getPluginManager().registerEvents(new ChangeQuestCardUseListener(questManager), this);
        getServer().getPluginManager().registerEvents(new RequirementsCardListener(questManager), this);



        try {
            setupData();
        } catch (SQLException e) {
            logger.warning("[LifesPluginS3] Error while setting up data!");
            if(detailedErrors) e.printStackTrace();
        }

        long timeWeeklyReset = (cardTimingsManager.getCardEndTime()-new Date().getTime())/50 ;
        if(timeWeeklyReset < 0 ) timeWeeklyReset = 0; //We cannot have negite delay in tasks so we need to check if it is

        new WeeklyCardResetTask(cardTimingsManager, databaseManager).runTaskLater(this, timeWeeklyReset);


        getServer().getPluginManager().registerEvents(new BreakBlocksListener(questManager), this);

        getCommand("getitems").setExecutor(new GetItemsCommand(recipesManager));
        getCommand("setlifes").setExecutor(new SetLifesCommand());
        getCommand("takelife").setExecutor(new TakeLifeCommand(lifesManager, recipesManager));
        getCommand("lifesmenu").setExecutor(new LifesMenuCommand(questManager, recipesManager, questsAwardsManager, databaseManager));
        getCommand("setprogress").setExecutor(new SetProgressCommand(questManager));
        getCommand("getallcards").setExecutor(new GetAllCardsItems(cardManager));
        getCommand("isinvfull").setExecutor(new InvFullCommand());
        getCommand("ping").setExecutor(new PingCommand());
        getCommand("getcard").setExecutor(new GetRandomCardCommand(cardManager, databaseManager));

        getCommand("setlifes").setTabCompleter(new SetLifesTabCompleter());
        getCommand("setprogress").setTabCompleter(new SetProgressTabCompleter(questManager));



    }

    @Override
    public void onDisable(){

        for(Player player : Bukkit.getOnlinePlayers()){


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

            for(QuestType questType : QuestType.values()){
                questsProgressDataSetup.setupPlayerQuestsProgress(questType, player);
            }


            questManager.calculatePlayerFinishedQuests(player);

            ArrayList<Byte> takenAwards = databaseManager.getPlayerTakenAwards(player.getUniqueId());
            if(takenAwards.isEmpty()){
                databaseManager.addPlayerTakenAwards(player.getUniqueId());
                takenAwards = databaseManager.getPlayerTakenAwards(player.getUniqueId());
            }

            questsAwardsManager.setAwardsTakenForPlayer(player, QuestType.DAILY, takenAwards.get(0));
            questsAwardsManager.setAwardsTakenForPlayer(player, QuestType.WEEKLY, takenAwards.get(1));
            questsAwardsManager.setAwardsTakenForPlayer(player, QuestType.CARD, takenAwards.get(2));
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

        file = new File(dataFolder + "/quests-data/daily-quests");
        if (!file.exists()) file.mkdirs();

        createSubfoldersForQuests("daily-quests");

        file = new File(dataFolder + "/quests-data/weekly-quests");
        if (!file.exists()) file.mkdirs();

        createSubfoldersForQuests("weekly-quests");

        file = new File(dataFolder + "/quests-data/card-quests");
        if (!file.exists()) file.mkdirs();

        createSubfoldersForQuests("card-quests");


        file = new File(dataFolder + "/quests-data/quests-timings.yml");
        if(!file.exists()) file.createNewFile();
    }

    private void createSubfoldersForQuests(String questsName) throws IOException {
        String basicPath = this.getDataFolder() + "/quests-data/"+questsName;

        for(int i=0; i < 3; i++){
            File file = new File(basicPath + "/tier-"+(i+1));
            if(!file.exists()) file.mkdirs();
        }

        File activeFolder = new File(basicPath + "/active-quests");
        if(!activeFolder.exists()) activeFolder.mkdirs();

        File tierCheck = new File(basicPath + "/active-quests/curr-tier.yml");
        if(!tierCheck.exists()) tierCheck.createNewFile();

    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        if(tier < 1) tier = 1;
        else if (tier > 3) tier = 3;
        this.tier = tier;
    }

    public ArrayList<Quest> getAllSkippedQuests(){
        return allSkippedQuests;
    }

    public void addSkippedQuest(Quest quest){
        allSkippedQuests.add(quest);
    }

    public static LifesPlugin getPlugin(){
        return LifesPlugin.getPlugin(LifesPlugin.class);
    }

    public boolean isDetailedErrors() {
        return detailedErrors;
    }
}
