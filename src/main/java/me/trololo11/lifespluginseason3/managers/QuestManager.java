package me.trololo11.lifespluginseason3.managers;

import me.trololo11.lifespluginseason3.menus.MainLifesMenu;
import me.trololo11.lifespluginseason3.tasks.ChangePageTimeTask;
import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.QuestType;
import me.trololo11.lifespluginseason3.utils.QuestUtils;
import me.trololo11.lifespluginseason3.listeners.datasetups.QuestsProgressDataSetup;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.*;

/**
 * This class stores and manages all of the object of {@link Quest} that
 * are used in this plugin. <b>This should be the only place that a new objects
 * of {@link Quest} are created</b> and all of the other classes should get their Quests
 * objects from this class.<br><br>
 * This class also manages the changing of active quests every x amount of
 * time and setting new randomized active quests. <br>
 * This class does not set the local data of quests, the {@link LifesPlugin} and
 * {@link QuestsProgressDataSetup} are responsible for that.
 */
public class QuestManager {

    private final LifesPlugin plugin = LifesPlugin.getPlugin();
    private final DatabaseManager databaseManager;
    private final QuestsTimingsManager questsTimingsManager;
    private final QuestsAwardsManager questsAwardsManager;

    private final HashMap<ListenerType, ArrayList<Quest>> listenerTypesQuests = new HashMap<>();
    private final HashMap<QuestType, String> pageTimingText = new HashMap<>();
    private final HashMap<Quest, String> questFilePaths = new HashMap<>();
    private final HashMap<Player, HashMap<QuestType, Integer> > playerAmountOfFinishedQuests = new HashMap<>();
    private final HashMap<QuestType, Integer> questsPerAwards = new HashMap<>();

    private final ArrayList<Quest> allQuests = new ArrayList<>();
    private final ArrayList<Quest> allActiveQuests = new ArrayList<>();
    private final ArrayList<Quest> activeDailyQuests = new ArrayList<>();
    private final ArrayList<Quest> activeWeeklyQuests = new ArrayList<>();
    private final ArrayList<Quest> activeCardQuests = new ArrayList<>();


    public QuestManager(DatabaseManager databaseManager, QuestsTimingsManager questsTimingsManager, QuestsAwardsManager questsAwardsManager){
        this.databaseManager = databaseManager;
        this.questsTimingsManager = questsTimingsManager;
        this.questsAwardsManager = questsAwardsManager;
        setupAllQuests();
        try {
            checkPageQuestTimings();
        } catch (IOException | SQLException e) {
            plugin.logger.warning("Error while getting the page timings for quests!");
            if(plugin.isDetailedErrors()) e.printStackTrace(System.out);
        }
    }

    /**
     * This function gets all of the quests and adds them to a internal array. <br>
     * It also sorts the active quests to their own arrays.
     */
    private void setupAllQuests(){
        String mainFolder = plugin.getDataFolder() + "/quests-data";
        int tier = plugin.getTier();

        allQuests.addAll(getAllQuestsInFolder(mainFolder + "/daily-quests/tier-"+tier, QuestType.DAILY));
        allQuests.addAll(getAllQuestsInFolder(mainFolder + "/weekly-quests/tier-"+tier, QuestType.WEEKLY));
        allQuests.addAll(getAllQuestsInFolder(mainFolder + "/card-quests/tier-"+tier, QuestType.CARD));


        activeDailyQuests.addAll(getAllQuestsInFolder(mainFolder + "/daily-quests/active-quests", QuestType.DAILY));
        activeWeeklyQuests.addAll(getAllQuestsInFolder(mainFolder + "/weekly-quests/active-quests", QuestType.WEEKLY));
        activeCardQuests.addAll(getAllQuestsInFolder(mainFolder + "/card-quests/active-quests", QuestType.CARD));

        allActiveQuests.addAll(activeDailyQuests);
        allActiveQuests.addAll(activeWeeklyQuests);
        allActiveQuests.addAll(activeCardQuests);

        for(Player player : Bukkit.getOnlinePlayers()){
            calculatePlayerFinishedQuests(player);
        }

        calulcateListenerQuestArrays();

        for(QuestType questType : QuestType.values()){
            calculateQuestsPerAward(questType);
        }
    }

    /**
     * This function sorts quests by their {@link ListenerType} to ArrayLists and
     * puts them into an internal array to make the listening for quests
     * easier
     */
    private void calulcateListenerQuestArrays(){
        for(ListenerType listenerType : ListenerType.values()){

            ArrayList<Quest> listenerQuests = new ArrayList<>(allActiveQuests.stream().filter(quest -> quest.getListenerType() == listenerType).toList());
            listenerTypesQuests.put(listenerType, listenerQuests);
        }
    }

    /**
     * Calculates how much quests has a specified player finished
     * @param player The player to check
     */
    public void calculatePlayerFinishedQuests(Player player){

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        HashMap<QuestType, Integer> playerAmountCompleted = playerAmountOfFinishedQuests.getOrDefault(player, new HashMap<>());

        for(QuestType questType : QuestType.values()){
            int completedAmount = 0;
            ArrayList<Quest> currQuests = getCorrespondingQuestArray(questType);

            for(Quest quest : currQuests){

                if(quest.hasFinished(player)) completedAmount++;

            }

            playerAmountCompleted.put(questType, completedAmount);

        }

        playerAmountOfFinishedQuests.put(player, playerAmountCompleted);

    }

    /**
     * This function is used to check all of the quests time. <br>
     * It check if the quests should be deleted.
     */
    public void checkPageQuestTimings() throws IOException, SQLException {
        Date dailyDate = questsTimingsManager.getEndDate(QuestType.DAILY);
        Date weeklyDate = questsTimingsManager.getEndDate(QuestType.WEEKLY);
        Date cardDate = questsTimingsManager.getEndDate(QuestType.CARD);

        checkDate(dailyDate, 86400000-1, QuestType.DAILY);
        checkDate(weeklyDate, 604800000, QuestType.WEEKLY);
        checkDate(cardDate, 604800000, QuestType.CARD);
    }

    /**
     * Checks if the date of every questType has passed (aka is after the current time). <br>
     * This function is responsible for calling the {@link ChangePageTimeTask} which changes the text in the main menu
     * and countdowns the time for the quests
     * @param date The date to check if is after
     * @param newTime The new time to add to the current time in the quests countdown
     * @param questType The quest type to check it
     * @throws IOException On error while saving quest-timings.yml
     * @throws SQLException On error while creating the tables
     */
    private void checkDate(Date date, long newTime, QuestType questType) throws IOException, SQLException {
        Date todayDate = new Date();
        if (date.after(todayDate)) {

            long timeLeftMils = date.getTime() - todayDate.getTime();

            //caculates the days (or hours) left by dividing the substraction of today and the date and dividng it by days or hours (written in milliseconds)
            int dateDays = (int) Math.floor((date.getTime() - todayDate.getTime()) / 86400000.0);
            int dateHours = (int) Math.floor((date.getTime() - todayDate.getTime()) / 3600000.0);
            int dateMinutes = (int) Math.floor((date.getTime() - todayDate.getTime()) / 60000.0);
            ChangePageTimeTask changePageTask;


            if (dateDays > 1 ) {

                //if the days are above 1 then start the timer

                //and this is basicly setting up the timerso that the days count down in real time (ypu can see i was tired while writing this lol)
                changePageTask = new ChangePageTimeTask('d', questType, this, dateDays, date);
                long startDelay = ((long) Math.ceil(timeLeftMils / 1000f) - ((dateDays) * 86400L)) * 20;


                changePageTask.runTaskTimer(plugin, startDelay, 1728000L);

            } else if(dateHours >= 1) {

                //this is basicly the same as the up one but counts hours
                changePageTask = new ChangePageTimeTask('h', questType,this, dateHours, date);
                long startDelay = (((long) Math.ceil(timeLeftMils / 1000f) - ((dateHours) * 3600L)) * 20)+60;


                changePageTask.runTaskTimer(plugin, startDelay, 72000L);
            }else{

                changePageTask = new ChangePageTimeTask('m', questType, this, dateMinutes, date);
                long startDelay = (((long) Math.ceil(timeLeftMils / 1000f) - ((dateMinutes) * 60L)) * 20)+60;


                changePageTask.runTaskTimer(plugin, startDelay, 1200L);
            }


        }else{

            createNewDate(questType, newTime);
        }
    }

    /**
     * This function is responsible for creating a new set of randomly generated quests for the specified
     * quest type. It also creates a new table in the database to store the progress of the quests.
     * @param questType The quest type to create the quests
     * @param newTime The new time to add to the current type
     * @throws IOException On quests-timings.yml file error
     * @throws SQLException On error while creating the tables
     */
    public void createNewDate(QuestType questType, long newTime) throws IOException, SQLException {
        Date todayDate = new Date();
        Date newDate = new Date(todayDate.getTime()+newTime);
        Random r = new Random();
        ArrayList<Quest> randomizedQuests = new ArrayList<>(allQuests);
        randomizedQuests.removeIf(quest -> quest.getQuestType() != questType);
        ArrayList<Quest> currQuestArray = getCorrespondingQuestArray(questType);
        ArrayList<String> existingNames = new ArrayList<>();


        int count = QuestUtils.getQuestsCount(questType);
        int ranQuestsLenght = randomizedQuests.size();

        if(ranQuestsLenght < 1){
            plugin.logger.severe("Please make at least one "+ questType.toString().toLowerCase() + " quest!");
            throw new RuntimeException();
        }

        if(ranQuestsLenght < count) count = ranQuestsLenght;

        String activeQuestsPath = plugin.getDataFolder() + "/quests-data/" + getQuestFolderName(questType) + "/active-quests";
        resetAllActiveQuestFiles(currQuestArray, plugin.getDataFolder() + "/quests-data/" + getQuestFolderName(questType), activeQuestsPath);
        databaseManager.removeQuestTable(questType);
        databaseManager.resetPlayerTakenAwards(questType);
        currQuestArray.clear();

        for(int i=0; i < count; i++){

            Quest addQuest = randomizedQuests.get(r.nextInt(ranQuestsLenght));

            while(existingNames.contains(addQuest.getDatabaseName())){
                addQuest = randomizedQuests.get(r.nextInt(ranQuestsLenght));
            }
            existingNames.add(addQuest.getDatabaseName());

            currQuestArray.add(addQuest);
            allActiveQuests.add(addQuest);
            listenerTypesQuests.get(addQuest.getListenerType()).add(addQuest);

            File questFile = new File(questFilePaths.get(addQuest));
            Path newPath = Path.of(activeQuestsPath + "/" + questFile.getName() );    
            Files.copy(questFile.toPath(), newPath, StandardCopyOption.REPLACE_EXISTING);
            questFilePaths.put(addQuest, newPath.toString());
            questFile.delete();

            allQuests.remove(addQuest);
        }

        File tierCheck = new File(plugin.getDataFolder() + "/quests-data/" + getQuestFolderName(questType) + "/active-quests/curr-tier.yml");

        YamlConfiguration config = YamlConfiguration.loadConfiguration(tierCheck);
        config.set("tier", plugin.getTier());
        config.save(tierCheck);

        databaseManager.createQuestTable(questType, currQuestArray);
        questsTimingsManager.setEndDate(newDate, questType);
        calulcateListenerQuestArrays();

        for(Player player : Bukkit.getOnlinePlayers()){
            playerAmountOfFinishedQuests.get(player).put(questType, 0);
        }

        calculateQuestsPerAward(questType);

        checkDate(newDate, newTime, questType);


    }

    /**
     * This function moves all of the active quests YML files to their orginal unactivated folder.
     * @param activeQuests The quests you want to move
     * @param questsFolderPath The folder of the type of quests
     * @param activeQuestsPath The active folder of the quests
     * @throws IOException When deleting a quest file fails
     */
    private void resetAllActiveQuestFiles(ArrayList<Quest> activeQuests, String questsFolderPath, String activeQuestsPath) throws IOException {

        YamlConfiguration tierConfig = YamlConfiguration.loadConfiguration(new File(activeQuestsPath + "/curr-tier.yml"));
        int tier = tierConfig.getInt("tier");
        if(tier == 0) tier = 1;

        for(Quest quest : activeQuests){
            allQuests.add(quest);
            listenerTypesQuests.get(quest.getListenerType()).remove(quest);
            allActiveQuests.remove(quest);
            File file = new File(questFilePaths.get(quest));
            Files.copy(file.toPath(), Path.of(questsFolderPath + "/tier-" + tier + "/" + file.getName()), StandardCopyOption.REPLACE_EXISTING);
            file.delete();
        }

    }

    /**
     * Gets all the YML files of quests in the specified folder and turns them to
     * a {@link Quest} objects using the {@link QuestManager#createQuest(File, QuestType)} function.
     * @param folderPath The path to get all of the quests from
     * @param questType The type of the quests
     * @return An {@link ArrayList} of Quests that were in the specified folder
     */
    private ArrayList<Quest> getAllQuestsInFolder(String folderPath, QuestType questType){
        ArrayList<Quest> createdQuests = new ArrayList<>();
        File listFiles = new File(folderPath);
        File[] listedFileQuests = listFiles.listFiles();
        if(listedFileQuests == null) return createdQuests;

        for(File file : listedFileQuests){
            if(file.getName().equalsIgnoreCase("curr-tier.yml")) continue;

            Quest quest = createQuest(file, questType);
            createdQuests.add(quest);
        }

        return createdQuests;
    }

    /**
     * This function creates a new {@link Quest} object based from the YML file
     * provided. (The blueprint and how to create the quest YML files is located in questHelp.txt)
     * @param questFile The YML file to create the Quest from
     * @param questType The type of the quest to create
     * @return A new {@link Quest} object
     */
    private Quest createQuest(File questFile, QuestType questType){
        YamlConfiguration config = YamlConfiguration.loadConfiguration(questFile);
        String name = config.getString("quest-name");
        ArrayList<String> description = (ArrayList<String>) config.getStringList("description");
        String databaseName = config.getString("database-name");
        if(databaseName == null || databaseName.isBlank()){
            plugin.logger.severe("Couldn't find the database name for the quest " + questFile.getName());
            throw new RuntimeException();
        }
        Material icon;
        try {
           icon = Material.valueOf(config.getString("icon").toUpperCase());
        }catch(IllegalArgumentException | NullPointerException e ){
            plugin.logger.warning("Couldn't find the icon for the quest "+ databaseName);
            icon = Material.RED_DYE;
        }
        boolean showProgress = config.getBoolean("show-progress");
        int maxProgress = config.getInt("max-progress");
        ListenerType listenerType;
        try {
            listenerType = ListenerType.valueOf(config.getString("listener-type"));
        }catch (IllegalArgumentException | NullPointerException e){
            plugin.logger.severe("The listener type in "+ databaseName + " quest isn't a valid one!");
            plugin.logger.severe("Please check the questHelp.txt to get all of the listener types.");
            throw new RuntimeException();
        }

        ArrayList<String> stringTargets = (ArrayList<String>) config.getStringList("target");

        if(stringTargets.isEmpty()) {
            stringTargets.add(config.getString("target"));
        }

        ArrayList<Object> targets = getTargets(listenerType, stringTargets);

        Quest newQuest = new Quest(name, databaseName, maxProgress, showProgress, icon, description, questType,listenerType, targets);

        questFilePaths.put(newQuest, questFile.getAbsolutePath());

        return newQuest;
    }

    private void calculateQuestsPerAward(QuestType questType){
        int questsPerAward = (int) Math.ceil((double) getCorrespondingQuestArray(questType).size()/ (questsAwardsManager.getMaxAmountOfAwards(questType)-1));
        questsPerAwards.put(questType, questsPerAward);
    }

    /**
     * This function transforms every target which is saved as a string
     * to a specified object that the {@link ListenerType} provided requries. <br><br>
     * For example if we had a quest with the {@link ListenerType#BREAK_BLOCKS} this function would transform
     * the list of targets which are a string to a new {@link Material} object
     * @param listenerType The listener type of the quest
     * @param stringTargets The targets to transform
     * @return A new ArrayList of transformed targets
     */
    private ArrayList<Object> getTargets(ListenerType listenerType, ArrayList<String> stringTargets){

        ArrayList<Object> targets = new ArrayList<>();


        switch (listenerType){

            case BREAK_BLOCKS -> stringTargets.forEach(s -> targets.add(Material.getMaterial(s.toUpperCase())));

        }


        return targets;

    }

    /**
     * Gets the folder name where the quests are stored of the specified questType
     * @param questType The questType to check
     * @return The folder name
     */
    private String getQuestFolderName(QuestType questType){
        switch (questType){

            case DAILY -> {
                return "daily-quests";
            }

            case WEEKLY -> {
                return "weekly-quests";
            }

            case CARD ->{
                return "card-quests";
            }

            default -> {
                return "generic-quests";
            }

        }
    }

    /**
     * Returns a {@link ArrayList} of {@link Quest}s which have the same {@link QuestType} as the one provided
     * @param questType The quest type of the quests you want to get
     * @return An {@link ArrayList<Quest>} of Quests
     */
    public ArrayList<Quest> getCorrespondingQuestArray(QuestType questType){

        switch (questType){

            case DAILY -> {
                return activeDailyQuests;
            }

            case WEEKLY -> {
                return activeWeeklyQuests;
            }

            case CARD -> {
                return activeCardQuests;
            }

            default -> {
                return new ArrayList<>();
            }

        }

    }

    /**
     *  Gets the text time that is displayed under the quests menu buttons
     *  in the {@link MainLifesMenu}.
     * @param questType The quest type to get the time text for.
     * @return The text time.
     */
    public String getQuestPageTimeText(QuestType questType){
        return pageTimingText.get(questType);
    }

    /**
     *  Sets the text time that is displayed under the quests menu buttons
     *  in the {@link MainLifesMenu}.
     * @param questType The quest type to set the time text for.
     * @param text The time text.
     */
    public void setQuestPageTimeText(QuestType questType, String text){
        pageTimingText.put(questType, text);

        for(Player player : Bukkit.getOnlinePlayers()){

            if(player.getOpenInventory().getTopInventory().getHolder() instanceof MainLifesMenu mainLifesMenu){
                mainLifesMenu.setMenuItems(player);
            }
        }
    }

    public void incrementPlayerFinishedQuests(Player player, QuestType questType){
        int newAmount = playerAmountOfFinishedQuests.get(player).get(questType)+1;
        playerAmountOfFinishedQuests.get(player).put(questType, newAmount);
    }

    public int getPlayerFinishedQuests(Player player, QuestType questType){
        return playerAmountOfFinishedQuests.get(player).getOrDefault(questType, 0);
    }

    public void removePlayerCompletedQuests(Player player){
        playerAmountOfFinishedQuests.remove(player);
    }

    public ArrayList<Quest> getAllActiveQuests() {
        return allActiveQuests;
    }

    public HashMap<ListenerType, ArrayList<Quest>> getListenerTypesQuests() {
        return listenerTypesQuests;
    }

    public ArrayList<Quest> getActiveDailyQuests() {
        return activeDailyQuests;
    }

    public ArrayList<Quest> getActiveWeeklyQuests() {
        return activeWeeklyQuests;
    }

    public ArrayList<Quest> getActiveCardQuests() {
        return activeCardQuests;
    }

    public int getQuestsPerAwards(QuestType questType){
        return questsPerAwards.get(questType);
    }

}
