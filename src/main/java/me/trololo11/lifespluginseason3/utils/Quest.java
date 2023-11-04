package me.trololo11.lifespluginseason3.utils;

import me.trololo11.lifespluginseason3.events.QuestFinishedEvent;
import me.trololo11.lifespluginseason3.managers.QuestManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents a custom quest which should be created from a YML file. <br><br>
 * It stores information such as:
 * <ul>
 *     <li>The name of the quest</li>
 *     <li>The description of this quest</li>
 *     <li>The database name for this quest</li>
 *     <li>The progress that player has to do to complete this quest (the variable is called maxProgress)</li>
 *     <li>If the quest should show the progress or not</li>
 *     <li>The icon of this quest</li>
 *     <li>The {@link ListenerType} of this quest</li>
 *     <li>The {@link QuestType} of this quest</li>
 *     <li>The targets of this quest</li>
 * </ul>
 * This class also stores local player's progress and
 * when a player is playing on the server and makes progress in a quest
 * <b>only the local (stored in this class) progress is changed.</b>
 * The data is saved to the sql database only when player leaves the game
 * and is set locally when a player joins the server.
 *
 */
public class Quest {

    private final String name;
    protected final ArrayList<String> description;
    private final String databaseName;
    private final int maxProgress;

    private boolean showProgress;
    private boolean isHalfed = false;
    private Material icon;

    private ListenerType listenerType;
    private QuestType questType;
    private final ArrayList<Object> targets;

    /**
     * A hashMap that stores all of the player's quests progress which are online on the server.
     * When a player leaves or the server stopes the progress is saved to the database from this hashMap
     */
    private HashMap<Player, Integer> playerProgress = null;

    /**
     * This class represents a custom quest which should be created from a YML file. <br><br>
     *  * It stores information such as:
     * @param name The name of the quest that is displayed by this plugin
     * @param databaseName The name that is used for storing the progress in the sql database.
     *                     This name is also used for general identification
     * @param maxProgress The progress that players are meant to get
     * @param showProgress If this quest shows progress (how much a player has done it) in the quest menu
     * @param icon The icon of this plugin
     * @param description The description of this quest that is gonna be displayed
     * @param questType The {@link QuestType} of this quest (aka in which page you can see it)
     * @param listenerType The {@link ListenerType} of this quest.
     *                     You can read more about this in {@link QuestManager}
     * @param targets The targets for the specified listener type
     * @param isActive If the quest is active aka if players can make progress in it.
     *                 (If it's false the internal hashMap where the online players progress is stored going to be null)
     */
    public Quest(String name, String databaseName, int maxProgress, boolean showProgress, Material icon, ArrayList<String> description, QuestType questType, ListenerType listenerType, ArrayList<Object> targets, boolean isActive) {
        this.name = name;
        this.databaseName = databaseName;
        this.showProgress = showProgress;
        this.icon = icon;
        this.maxProgress = maxProgress;
        ArrayList<String> realDescription = new ArrayList<>();
        for(String str : description){
            realDescription.add(Utils.chat("&f" + str));
        }
        this.description = realDescription;
        this.questType = questType;
        this.listenerType = listenerType;
        this.targets = targets;
        if(isActive) playerProgress = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getDescription() {
        return description;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public boolean getShowProgress() {
        return showProgress;
    }

    public Material getIcon() {
        return icon;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public ListenerType getListenerType() {
        return listenerType;
    }

    public QuestType getQuestType() {
        return questType;
    }

    public ArrayList<Object> getTargets() {
        return new ArrayList<>(targets);
    }

    /**
     * Returns wheather or not the quest has it's max progress cut into half or not. <br>
     * (Player can do this with the lower requirements card)
     * @return Are requrements halfed
     */
    public boolean isHalfed() {
        return isHalfed;
    }

    public void setHalfed(boolean halfed) {
        isHalfed = halfed;
    }

    /**
     * Sets a player's progress for this quest. <br>
     * This function show a message in chat if a player finished this quest
     * @param player The player to set the progress for
     * @param progress The progress to set
     */
    public void setPlayerProgress(Player player, int progress){
        boolean isInital = playerProgress.containsKey(player) && !hasFinished(player);
        playerProgress.put(player, progress);
        if(hasFinished(player) && isInital) Bukkit.getServer().getPluginManager().callEvent(new QuestFinishedEvent(player, this, true));
    }

    /**
     * Sets the players progress but if player finishes this quest
     * it doesn't show the message in chat
     * @param player The player to set the progress for
     * @param progress The new progress
     */
    public void setSilentPlayerProgress(Player player, int progress){
        playerProgress.put(player, progress);
        if(hasFinished(player)) Bukkit.getServer().getPluginManager().callEvent(new QuestFinishedEvent(player, this, false));
    }

    /**
     * This function "activates" this quest.
     * Makes the hashMap that saves the online players progress
     */
    public void activate(){
        playerProgress = new HashMap<>();
    }

    /**
     * Makes the  hashMap that saves the online players progress null
     */
    public void unActivate(){
        playerProgress = null;
    }


    public void removePlayerProgress(Player player){
        playerProgress.remove(player);
    }

    public int getPlayerProgress(Player player){
        return playerProgress.getOrDefault(player, 0);
    }

    public boolean hasFinished(Player player){
        return isHalfed ? getPlayerProgress(player) >= getMaxProgress()/2 : getPlayerProgress(player) >= getMaxProgress();
    }






}
