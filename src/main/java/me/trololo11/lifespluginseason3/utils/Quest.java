package me.trololo11.lifespluginseason3.utils;

import me.trololo11.lifespluginseason3.events.QuestFinishedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
    private Material icon;

    private ListenerType listenerType;
    private QuestType questType;
    private final ArrayList<Object> targets;

    private final HashMap<Player, Integer> playerProgress = new HashMap<>();

    public Quest(String name, String databaseName, int maxProgress, boolean showProgress, Material icon, ArrayList<String> description, QuestType questType, ListenerType listenerType, ArrayList<Object> targets) {
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
        return targets;
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

    public void removePlayerProgress(Player player){
        playerProgress.remove(player);
    }

    public int getPlayerProgress(Player player){
        return playerProgress.getOrDefault(player, 0);
    }

    public boolean hasFinished(Player player){
        return getPlayerProgress(player) >= getMaxProgress();
    }





}
