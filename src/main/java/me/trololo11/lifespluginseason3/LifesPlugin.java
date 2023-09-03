package me.trololo11.lifespluginseason3;

import me.trololo11.lifespluginseason3.commands.GetItemsCommand;
import me.trololo11.lifespluginseason3.commands.SetLifesCommand;
import me.trololo11.lifespluginseason3.commands.TakeLifeCommand;
import me.trololo11.lifespluginseason3.commands.tabcompleters.SetLifesTabCompleter;
import me.trololo11.lifespluginseason3.events.PlayerChangeLifesEvent;
import me.trololo11.lifespluginseason3.listeners.CustomItemsCraftingFix;
import me.trololo11.lifespluginseason3.listeners.lifeslisteners.LifeUseListener;
import me.trololo11.lifespluginseason3.listeners.lifeslisteners.PlayerChangeLifesListener;
import me.trololo11.lifespluginseason3.listeners.lifeslisteners.PlayerDeathListener;
import me.trololo11.lifespluginseason3.listeners.datasetups.PlayerLifesDataSetup;
import me.trololo11.lifespluginseason3.managers.DatabaseManager;
import me.trololo11.lifespluginseason3.managers.LifesManager;
import me.trololo11.lifespluginseason3.managers.RecipesManager;
import me.trololo11.lifespluginseason3.managers.TeamsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public final class LifesPlugin extends JavaPlugin {

    private TeamsManager teamsManager;
    private DatabaseManager databaseManager;
    private LifesManager lifesManager;
    private RecipesManager recipesManager;

    private boolean detailedErrors;

    public Properties globalDbProperties;
    public Logger logger;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        detailedErrors = getConfig().getBoolean("detailed-errors");
        setupDbProperties();

        teamsManager = new TeamsManager();
        databaseManager = new DatabaseManager();
        recipesManager = new RecipesManager();
        logger = Bukkit.getLogger();

        teamsManager.registerEverything();




        try {
            databaseManager.initialize();
            lifesManager = new LifesManager(databaseManager.getAllDeadPlayers());

        } catch (SQLException e) {
            logger.severe("Error while connecting to the database");
            logger.severe("Make sure the info in config is accurate!");
            if(detailedErrors) e.printStackTrace();
            return;
        }

        getServer().getPluginManager().registerEvents(new PlayerChangeLifesListener(lifesManager, teamsManager), this);
        getServer().getPluginManager().registerEvents(new PlayerLifesDataSetup(lifesManager, databaseManager), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(lifesManager), this);
        getServer().getPluginManager().registerEvents(new CustomItemsCraftingFix(recipesManager), this);
        getServer().getPluginManager().registerEvents(new LifeUseListener(lifesManager), this);

        try {
            setupData();
        } catch (SQLException e) {
            logger.warning("[LifesPluginS3] Error while setting up data!");
            if(detailedErrors) e.printStackTrace();
        }

        getCommand("getitems").setExecutor(new GetItemsCommand(recipesManager));
        getCommand("setlifes").setExecutor(new SetLifesCommand());
        getCommand("takelife").setExecutor(new TakeLifeCommand(lifesManager, recipesManager));

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

    public static LifesPlugin getPlugin(){
        return LifesPlugin.getPlugin(LifesPlugin.class);
    }

    public boolean isDetailedErrors() {
        return detailedErrors;
    }
}
