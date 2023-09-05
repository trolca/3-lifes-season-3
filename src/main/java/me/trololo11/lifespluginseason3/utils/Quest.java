package me.trololo11.lifespluginseason3.utils;

import java.util.ArrayList;
import java.util.List;

public class Quest {

    private final String name;
    protected final ArrayList<String> description;
    private final String databaseName;
    private final int maxProgress;
    private QuestType questType;
    private List<Object> targets;

    public Quest(String name, String databaseName, int maxProgress,ArrayList<String> description,   QuestType questType, List<Object> targets) {
        this.name = name;
        this.databaseName = databaseName;
        this.maxProgress = maxProgress;
        this.description = description;
        this.questType = questType;
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

    public int getMaxProgress() {
        return maxProgress;
    }

    public QuestType getQuestType() {
        return questType;
    }

    public List<Object> getTargets() {
        return targets;
    }







}
