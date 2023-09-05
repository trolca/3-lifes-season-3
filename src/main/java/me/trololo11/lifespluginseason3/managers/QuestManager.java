package me.trololo11.lifespluginseason3.managers;

import me.trololo11.lifespluginseason3.LifesPlugin;

import java.io.File;
import java.io.IOException;

public class QuestManager {

    private final LifesPlugin plugin = LifesPlugin.getPlugin();
    private final DatabaseManager databaseManager;

    public QuestManager(DatabaseManager databaseManager){
        this.databaseManager = databaseManager;
    }



}
