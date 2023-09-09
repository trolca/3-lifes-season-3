package me.trololo11.lifespluginseason3.managers;

import me.trololo11.lifespluginseason3.tasks.ChangePageTimeTask;
import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.QuestType;
import me.trololo11.lifespluginseason3.utils.QuestUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.*;

public class QuestManager {

    private final LifesPlugin plugin = LifesPlugin.getPlugin();
    private final DatabaseManager databaseManager;
    private final QuestsTimingsManager questsTimingsManager;

    private final HashMap<Quest, String> questFilePaths = new HashMap<>();

    private ArrayList<Quest> allQuests = new ArrayList<>();
    private ArrayList<Quest> activeDailyQuests = new ArrayList<>();
    private ArrayList<Quest> activeWeeklyQuests = new ArrayList<>();
    private ArrayList<Quest> activeCardQuests = new ArrayList<>();



    public QuestManager(DatabaseManager databaseManager, QuestsTimingsManager questsTimingsManager){
        this.databaseManager = databaseManager;
        this.questsTimingsManager = questsTimingsManager;
        setupAllQuests();
        try {
            checkPageQuestTimings();
        } catch (IOException | SQLException e) {
            plugin.logger.warning("Error while getting the page timings for quests!");
            if(plugin.isDetailedErrors()) e.printStackTrace(System.out);
        }
    }

    private void setupAllQuests(){
        String mainFolder = plugin.getDataFolder() + "/quests-data";
        int tier = plugin.getTier();

        allQuests.addAll(getAllQuestsInFolder(mainFolder + "/daily-quests/tier-"+tier, QuestType.DAILY));
        allQuests.addAll(getAllQuestsInFolder(mainFolder + "/weekly-quests/tier-"+tier, QuestType.WEEKLY));
        allQuests.addAll(getAllQuestsInFolder(mainFolder + "/card-quests/tier-"+tier, QuestType.CARD));



        activeDailyQuests.addAll(getAllQuestsInFolder(mainFolder + "/daily-quests/active-quests", QuestType.DAILY));
        activeWeeklyQuests.addAll(getAllQuestsInFolder(mainFolder + "/weekly-quests/active-quests", QuestType.WEEKLY));
        activeCardQuests.addAll(getAllQuestsInFolder(mainFolder + "/card-quests/active-quests", QuestType.CARD));
    }

    public void checkPageQuestTimings() throws IOException, SQLException {
        Date dailyDate = questsTimingsManager.getEndDate(QuestType.DAILY);
        Date weeklyDate = questsTimingsManager.getEndDate(QuestType.WEEKLY);
        Date cardDate = questsTimingsManager.getEndDate(QuestType.CARD);

        checkDate(dailyDate, 86400000-1, QuestType.DAILY);
        checkDate(weeklyDate, 604800000, QuestType.WEEKLY);
        checkDate(cardDate, 604800000, QuestType.CARD);
    }

    /*
    soo this is a kida hard function so im gonna explain it as best as I can lol
    in the plugin data folder we have a file which stores the date when weekly and daily pages of quests have to end
    so this function calculates the timer logic for the pages and checks if its after today
    the function above is responcible (thats not how you spell is it) for calling it
    (its explained im more detail in the function)

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

        if(ranQuestsLenght > count) count = ranQuestsLenght;
        resetAllActiveQuestFiles(currQuestArray, plugin.getDataFolder() + "/quests-data/" + getQuestFolderName(questType) + "/tier-" + plugin.getTier());
        databaseManager.removeQuestTable(questType);
        currQuestArray.clear();

        for(int i=0; i < count; i++){

            Quest addQuest = randomizedQuests.get(r.nextInt(ranQuestsLenght));

            while(existingNames.contains(addQuest.getDatabaseName())){
                addQuest = randomizedQuests.get(r.nextInt(ranQuestsLenght));
            }
            existingNames.add(addQuest.getDatabaseName());

            currQuestArray.add(addQuest);

            File questFile = new File(questFilePaths.get(addQuest));
            Path newPath = Path.of(plugin.getDataFolder() + "/quests-data/" + getQuestFolderName(questType) + "/active-quests/" + questFile.getName() );
            Files.copy(questFile.toPath(), newPath, StandardCopyOption.REPLACE_EXISTING);
            questFilePaths.put(addQuest, newPath.toString());
            questFile.delete();
        }



        databaseManager.createQuestTable(questType, currQuestArray);
        questsTimingsManager.setEndDate(newDate, questType);

    }


    private void resetAllActiveQuestFiles(ArrayList<Quest> activeQuests, String normalQuestsPath) throws IOException {

        for(Quest quest : activeQuests){
            File file = new File(questFilePaths.get(quest));
            Files.copy(file.toPath(), Path.of(normalQuestsPath + "/" + file.getName()), StandardCopyOption.REPLACE_EXISTING);
            file.delete();
        }

    }

    private ArrayList<Quest> getAllQuestsInFolder(String folderPath, QuestType questType){
        ArrayList<Quest> createdQuests = new ArrayList<>();
        File listFiles = new File(folderPath);
        File[] listedFileQuests = listFiles.listFiles();
        if(listedFileQuests == null) return createdQuests;

        for(File file : listedFileQuests){
            Quest quest = createQuest(file, questType);
            createdQuests.add(quest);
        }

        return createdQuests;
    }


    private Quest createQuest(File questFile, QuestType questType){
        YamlConfiguration config = YamlConfiguration.loadConfiguration(questFile);
        String name = config.getString("quest-name");
        ArrayList<String> description = (ArrayList<String>) config.getStringList("description");
        String databaseName = config.getString("database-name");
        if(databaseName == null || databaseName.isBlank()){
            plugin.logger.severe("Couldn't find the database name for the quest "+ config.getName());
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

        String target = config.getString("target");
        ArrayList<String> stringTargets;
        if(target == null){
            stringTargets = (ArrayList<String>) config.getStringList("target");
        }else{
            stringTargets = new ArrayList<>();
            stringTargets.add(target);
        }

        ArrayList<Object> targets = getTargets(listenerType, stringTargets);

        Quest newQuest = new Quest(name, databaseName, maxProgress, showProgress, icon, description, questType,listenerType, targets);

        questFilePaths.put(newQuest, questFile.getAbsolutePath());

        return newQuest;
    }


    private ArrayList<Object> getTargets(ListenerType questType, ArrayList<String> stringTargets){

        ArrayList<Object> targets = new ArrayList<>();

        switch (questType){

            case BREAK_BLOCKS -> stringTargets.forEach(o -> targets.add(Material.getMaterial(o)));

        }

        return targets;

    }

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

    private ArrayList<Quest> getCorrespondingQuestArray(QuestType questType){

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




}
