package me.trololo11.lifespluginseason3.utils;

import me.trololo11.lifespluginseason3.LifesPlugin;

public class QuestUtils {

    private static final LifesPlugin plugin = LifesPlugin.getPlugin();

    public static int getQuestsCount(QuestType questType){

        switch (questType){

            case DAILY -> {
                return plugin.dailyQuestsCount;
            }

            case WEEKLY -> {
                return plugin.weeklyQuestsCount;
            }

            case  CARD -> {
                return plugin.cardQuestsCount;
            }

            default -> {
                return 1;
            }

        }
    }








}
