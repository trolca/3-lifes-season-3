package me.trololo11.lifespluginseason3.utils;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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



    public Quest(String name, String databaseName, int maxProgress, boolean showProgress, Material icon, ArrayList<String> description, QuestType questType, ListenerType listenerType, ArrayList<Object> targets) {
        this.name = name;
        this.databaseName = databaseName;
        this.showProgress = showProgress;
        this.icon = icon;
        this.maxProgress = maxProgress;
        this.description = description;
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







}
