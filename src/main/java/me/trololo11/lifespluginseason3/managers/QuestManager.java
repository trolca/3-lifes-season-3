package me.trololo11.lifespluginseason3.managers;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.listeners.datasetups.QuestsProgressDataSetup;
import me.trololo11.lifespluginseason3.menus.MainLifesMenu;
import me.trololo11.lifespluginseason3.tasks.ChangePageTimeTask;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.QuestType;
import me.trololo11.lifespluginseason3.utils.QuestUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

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
    private final HashMap<QuestType, HashMap<String, Quest>> databaseNameQuestsHashMap = new HashMap<>();
    private final HashMap<String, Quest> allDatabaseNameQuestsHashMap = new HashMap<>();


    private final ArrayList<Quest> allUnactiveQuests = new ArrayList<>();
    private final ArrayList<Quest> allActiveQuests = new ArrayList<>();
    private final ArrayList<Quest> activeDailyQuests = new ArrayList<>();
    private final ArrayList<Quest> activeWeeklyQuests = new ArrayList<>();
    private final ArrayList<Quest> activeCardQuests = new ArrayList<>();


    public QuestManager(DatabaseManager databaseManager, QuestsTimingsManager questsTimingsManager, QuestsAwardsManager questsAwardsManager){

        for(QuestType questType : QuestType.values()) {
            databaseNameQuestsHashMap.put(questType, new HashMap<>());
        }

        this.databaseManager = databaseManager;
        this.questsTimingsManager = questsTimingsManager;
        this.questsAwardsManager = questsAwardsManager;
        try {
            setupAllQuests();
            checkPageQuestTimings();
            checkQuests();
        } catch (IOException | SQLException e) {
            plugin.logger.warning("Error while getting the page timings for quests!");
            if(plugin.isDetailedErrors()) e.printStackTrace(System.out);
        }
    }

    /**
     * This function gets all of the quests and adds them to a internal array. <br>
     * It also sorts the active quests to their own arrays.
     */
    private void setupAllQuests() throws SQLException {
        String mainFolder = plugin.getDataFolder() + "/quests-data";
        int tier = plugin.getTier();

        HashMap<QuestType, ArrayList<String>> halfedQuestMap = databaseManager.getAllQuestHalfed();

        allUnactiveQuests.addAll(getAllQuestsInFolder(mainFolder + "/daily-quests/tier-"+tier, QuestType.DAILY, false));
        allUnactiveQuests.addAll(getAllQuestsInFolder(mainFolder + "/weekly-quests/tier-"+tier, QuestType.WEEKLY, false));
        allUnactiveQuests.addAll(getAllQuestsInFolder(mainFolder + "/card-quests/tier-"+tier, QuestType.CARD, false));

        activeDailyQuests.addAll(getAllQuestsInFolder(mainFolder + "/daily-quests/active-quests", QuestType.DAILY, halfedQuestMap, true));
        activeWeeklyQuests.addAll(getAllQuestsInFolder(mainFolder + "/weekly-quests/active-quests", QuestType.WEEKLY, halfedQuestMap, true));
        activeCardQuests.addAll(getAllQuestsInFolder(mainFolder + "/card-quests/active-quests", QuestType.CARD, halfedQuestMap, true));

        allActiveQuests.addAll(activeDailyQuests);
        allActiveQuests.addAll(activeWeeklyQuests);
        allActiveQuests.addAll(activeCardQuests);

        for(Player player : Bukkit.getOnlinePlayers()){
            calculatePlayerFinishedQuests(player);
        }

        calculateListenerQuestArrays();

        for(QuestType questType : QuestType.values()){
            calculateQuestsPerAward(questType);
        }
    }

    /**
     * This function checks all of the active quests if they have a
     * coressponding column in the sql database and if they do not
     * it moves them to the unactivated quests folder
     * @throws SQLException On error with the connection to the database
     * @throws IOException On error while getting the tier of the quests
     */
    private void checkQuests() throws SQLException, IOException {

        ArrayList<Quest> questsToDeletion = new ArrayList<>();

        for(QuestType questType : QuestType.values()){
            ArrayList<Quest> questArray = getCorrespondingQuestArray(questType);
            ArrayList<String> columns = databaseManager.getAllQuestColumnNames(questType);

            String currTierPath = plugin.getDataFolder() + "/quests-data/" + getQuestFolderName(questType) + "/active-quests/curr-tier.yml";

            FileConfiguration tierConfig = YamlConfiguration.loadConfiguration(new File(currTierPath));

            String questsFolder = plugin.getDataFolder() + "/quests-data/" + getQuestFolderName(questType) + "/tier-"+tierConfig.getInt("tier");

            for(Quest quest : questArray){

                if(!columns.contains(quest.getDatabaseName())) {
                    moveQuest(quest, questsFolder);
                    questsToDeletion.add(quest);
                }
            }

        }

        //We have to remove these quests separately because we cannot modify and go through the array at the same time
        for(Quest quest : questsToDeletion){
            allUnactiveQuests.add(quest);
            listenerTypesQuests.get(quest.getListenerType()).remove(quest);
            getCorrespondingQuestArray(quest.getQuestType()).remove(quest);
            allActiveQuests.remove(quest);
        }

    }

    /**
     * This function sorts quests by their {@link ListenerType} to ArrayLists and
     * puts them into an internal array to make the listening for quests
     * easier
     */
    private void calculateListenerQuestArrays(){
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
        ArrayList<Quest> randomizedQuests = new ArrayList<>(allUnactiveQuests);
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
        databaseManager.removeAllQuestValues(questType);
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
            addQuest.activate();

            File questFile = new File(questFilePaths.get(addQuest));
            Path newPath = Path.of(activeQuestsPath + "/" + questFile.getName() );    
            Files.copy(questFile.toPath(), newPath, StandardCopyOption.REPLACE_EXISTING);
            questFilePaths.put(addQuest, newPath.toString());
            questFile.delete();

            allUnactiveQuests.remove(addQuest);
        }

        File tierCheck = new File(plugin.getDataFolder() + "/quests-data/" + getQuestFolderName(questType) + "/active-quests/curr-tier.yml");

        YamlConfiguration config = YamlConfiguration.loadConfiguration(tierCheck);
        config.set("tier", plugin.getTier());
        config.save(tierCheck);

        databaseManager.createQuestTable(questType, currQuestArray);
        questsTimingsManager.setEndDate(newDate, questType);
        calculateListenerQuestArrays();

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

        FileConfiguration tierConfig = YamlConfiguration.loadConfiguration(new File(activeQuestsPath + "/curr-tier.yml"));
        int tier = tierConfig.getInt("tier");

        questsFolderPath += "/tier-"+tier;

        for(Quest quest : activeQuests){
            quest.unActivate();
            moveQuest(quest, questsFolderPath);
        }
        //We clone the array list bcs we are gonna be modifing the other array list
        @SuppressWarnings("unchecked")
        ArrayList<Quest> cloneActiveQuests = (ArrayList<Quest>) activeQuests.clone();

        for(Quest quest : cloneActiveQuests){
            allUnactiveQuests.add(quest);
            listenerTypesQuests.get(quest.getListenerType()).remove(quest);
            getCorrespondingQuestArray(quest.getQuestType()).remove(quest);
            allActiveQuests.remove(quest);
        }

    }

    /**
     * Moves the provided quest from the active quests folder to the normal quest folder. <br>
     * <b> This doesn't remove it from the active quests arrayLists and adds it to the allQuests array list! </b>
     * @param quest The quest to move
     * @param moveToPath The path to move the quest to
     * @throws IOException While getting the tier of the quests finds unsucessfull
     */
    private void moveQuest(Quest quest, String moveToPath) throws IOException {
        File questFile = new File(questFilePaths.get(quest));
        String newPath = moveToPath + "/"+ questFile.getName();
        Files.copy(questFile.toPath(), Path.of(newPath), StandardCopyOption.REPLACE_EXISTING);
        questFilePaths.put(quest, newPath);
        questFile.delete();
    }

    /**
     * This function swaps the specified quests. <br>
     * It moves their files to the coressponding location and
     * removes and adds them to specific arrays to move them
     * in real-time. It also changes the sql tables
     * to make them compatible for this new quest.
     * @param quest The quest to change
     * @param changeToQuest The quest to change the pervious quest into
     * @throws IOException When it doesnt find a quest file
     * @throws SQLException When theres an error while renaming the sql tables
     */
    public void changeQuest(Quest quest, Quest changeToQuest) throws IOException, SQLException {
        if(quest.getQuestType() != changeToQuest.getQuestType()){
            plugin.logger.warning("[3LifesPluginS3] Tried to change quests that do not have the same quest type!");
            return;
        }
        QuestType questType = quest.getQuestType();

        String activeQuestsPath = plugin.getDataFolder() + "/quests-data/" + getQuestFolderName(questType) + "/active-quests";
        String currTierFilePath = activeQuestsPath + "/curr-tier.yml";
        FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(new File(currTierFilePath));
        String moveQuestTierPath =  plugin.getDataFolder() + "/quests-data/" + getQuestFolderName(questType) + "/tier-"+fileConfig.getInt("tier");


        //Basically swaps these files of quests
        moveQuest(quest, moveQuestTierPath);
        moveQuest(changeToQuest, activeQuestsPath);

        databaseManager.changeNameOfQuestColumn(questType, quest.getDatabaseName(), changeToQuest.getDatabaseName());

        quest.unActivate();
        changeToQuest.activate();

        ArrayList<Quest> correspondingArray = getCorrespondingQuestArray(questType);

        correspondingArray.remove(quest);
        correspondingArray.add(changeToQuest);
        allActiveQuests.remove(quest);
        allActiveQuests.add(changeToQuest);


        allUnactiveQuests.remove(changeToQuest);
        allUnactiveQuests.add(quest);


    }

    private ArrayList<Quest> getAllQuestsInFolder(String folderPath, QuestType questType, boolean isActiveFolder){
        return getAllQuestsInFolder(folderPath, questType, null, isActiveFolder);
    }

    /**
     * Gets all the YML files of quests in the specified folder and turns them to
     * a {@link Quest} objects using the {@link QuestManager#createQuest(File, QuestType, boolean)} function.
     * @param folderPath The path to get all of the quests from
     * @param questType The type of the quests
     * @param halfedQuestMap The hashMap ov every quest that has it's max progress cut in half.
     *                        The key is the quest type of the quest and the array list has all of these quests database name.
     * @return An {@link ArrayList} of Quests that were in the specified folder
     */
    private ArrayList<Quest> getAllQuestsInFolder(String folderPath, QuestType questType, HashMap<QuestType, ArrayList<String>> halfedQuestMap, boolean isActiveFolder){
        ArrayList<Quest> createdQuests = new ArrayList<>();
        File listFiles = new File(folderPath);
        File[] listedFileQuests = listFiles.listFiles();
        if(listedFileQuests == null) return createdQuests;

        for(File file : listedFileQuests){
            if(file.getName().equalsIgnoreCase("curr-tier.yml")) continue;

            Quest quest = createQuest(file, questType, halfedQuestMap, isActiveFolder);
            if(quest == null) continue;
            createdQuests.add(quest);
        }

        return createdQuests;
    }

    /**
     * This function creates a new {@link Quest} object based from the YML file
     * provided. (The blueprint and how to create the quest YML files is located in questHelp.txt)
     * <i>(This function automatically sets the quests isHalved value to false)</i>
     * @param questFile The YML file to create the Quest from
     * @param questType The type of the quest to create
     * @return A new {@link Quest} object
     */
    private Quest createQuest(File questFile, QuestType questType, boolean isActive){
        return createQuest(questFile, questType, null, isActive);
    }

    /**
     * This function creates a new {@link Quest} object based from the YML file
     * provided. (The blueprint and how to create the quest YML files is located in questHelp.txt)
     * @param questFile The YML file to create the Quest from
     * @param questType The type of the quest to create
     * @param halvedQuestsMap The hashMap ov every quest that has it's max progress cut in half.
     *                        The key is the quest type of the quest and the array list has all of these quests database name.
     * @param isActive Can players make progress in this quest
     * @return A new {@link Quest} object
     */
    private Quest createQuest(File questFile, QuestType questType, HashMap<QuestType, ArrayList<String>> halvedQuestsMap, boolean isActive){
        YamlConfiguration config = YamlConfiguration.loadConfiguration(questFile);
        String name = config.getString("quest-name");
        ArrayList<String> description = (ArrayList<String>) config.getStringList("description");
        String databaseName = config.getString("database-name");

        //wrong format of database name is very dangerous so we need to check it
        if(databaseName == null || databaseName.isBlank()){
            plugin.logger.severe("Couldn't find the database name for the quest " + questFile.getName());
            throw new RuntimeException();
        }else if(databaseName.length() > 100){
            plugin.logger.warning("[3LifesPluginS3] The database name for the quest "+ questFile.getName() + " is too long! (100 characters max)");
            return null;
        }else if(databaseName.contains("-") || databaseName.contains(" ") || databaseName.contains("uuid")){
            plugin.logger.warning("[3LifesPluginS3] The database name for the quest "+ questFile.getName() + " contains illegal characters!");
            plugin.logger.warning("[3LifesPluginS3] Illegal characters: (- and space and the word \"uuid\")");
            return null;
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

        //And also there cannot be any duplicates lol
        if(databaseNameQuestsHashMap.get(questType).containsKey(databaseName)){
            plugin.logger.warning("[3LifesPluginS3] The database name of quest "+ questFile.getName() + " in the "+questType.toString().toLowerCase()+" folder already exists!");
            plugin.logger.warning("[3LifesPluginS3] Please create unique database names for every quest!");
            plugin.logger.warning("[3LifesPluginS3] The duplicate database name: \""+ databaseName+"\"");
            return null;
        }

        Quest newQuest = new Quest(name, databaseName, maxProgress, showProgress, icon, description, questType,listenerType, targets, isActive);

        if(halvedQuestsMap != null && halvedQuestsMap.get(questType).contains(databaseName)) newQuest.setHalfed(true);

        databaseNameQuestsHashMap.get(questType).put(databaseName, newQuest);
        allDatabaseNameQuestsHashMap.put(databaseName, newQuest);
        questFilePaths.put(newQuest, questFile.getAbsolutePath());

        return newQuest;
    }

    /**
     * This functions calculates how many quests are needed to get 1 award and sets the result to
     * an hash map.
     * @param questType The quest type to calculate it for.
     */
    private void calculateQuestsPerAward(QuestType questType){
        int questsPerAward = (int) Math.ceil((double) getCorrespondingQuestArray(questType).size()/ (questsAwardsManager.getMaxAmountOfAwards(questType)-1));
        questsAwardsManager.setQuestsPerAwards(questType, questsPerAward);
    }

    //For season 4 - better this logic (maybe with enums?)
    /**
     * This function transforms every target which is saved as a string
     * to a specified object that the {@link ListenerType} provided requries. <br><br>
     *
     * For example if we had a quest with the {@link ListenerType#BREAK_BLOCKS} this function would transform
     * the list of targets which are a string to a new {@link Material} object
     * @param listenerType The listener type of the quest
     * @param stringTargets The targets to transform
     * @return A new ArrayList of transformed targets
     */
    private ArrayList<Object> getTargets(ListenerType listenerType, ArrayList<String> stringTargets){

        ArrayList<Object> targets = new ArrayList<>();


        switch (listenerType){

            case BREAK_BLOCKS, CRAFT, EAT, PLACE_BLOCKS, BREAK_ITEM, BREAK_BLOCKS_NO_SILK, SMITHING_USE,
                 USE_ITEM, SMELT_ITEM, VILLAGER_TRADE_SPEND, VILLAGER_TRADE_BUY, GET_ITEM_BY_MOB, GET_ITEM, USE_ITEM_ON_GROUND->
                    stringTargets.forEach(s -> targets.add(Material.getMaterial(s == null ? null : s.toUpperCase()))) ;

            case KILL_MOBS,RIDE_DISTANCE,TAME_ANIMAL,
                 BREED_ENTITY, RIGHT_CLICK_ENTITY, PUNCH_ENTITY -> stringTargets.forEach(s -> targets.add(s == null ? null : EntityType.valueOf(s.toUpperCase())));

            case PLAYER_HEART -> stringTargets.forEach(s -> targets.add(Integer.parseInt(s)));

            case PLAYER_DAMAGE -> stringTargets.forEach(s -> targets.add(s == null ? null : EntityDamageEvent.DamageCause.valueOf(s.toUpperCase())));
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

    /**
     * Returns an array of all unactive quests
     * @return All unactive quests
     */
    public ArrayList<Quest> getAllUnactiveQuests() {
        return allUnactiveQuests;
    }

    public Quest getQuestByDatabaseName(String databaseName){
        return allDatabaseNameQuestsHashMap.get(databaseName);
    }

    public Quest getQuestByDatabaseName(QuestType questType, String databaseName){
        return databaseNameQuestsHashMap.get(questType).get(databaseName);
    }

}