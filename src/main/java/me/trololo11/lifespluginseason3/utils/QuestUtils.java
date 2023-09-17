package me.trololo11.lifespluginseason3.utils;

import me.trololo11.lifespluginseason3.LifesPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

    public static ItemStack getAwardItem(ItemStack orginalItem,ItemStack awardTaken, ItemMeta normalMeta, ItemMeta takeMeta, int howMuchShouldTake, int howManyTaken, int allQuestsSize, int playerFinishedQuests, int numOfTheItem, int maxAmountAwards, int questsPerAward){
        int numCheck = maxAmountAwards-numOfTheItem;



        if(howMuchShouldTake >= numCheck && howManyTaken < numCheck ){
            orginalItem.setItemMeta(takeMeta);
        } else if (howManyTaken >= numCheck) {
            orginalItem = awardTaken;
        }else{

           if(numOfTheItem >= maxAmountAwards){
               normalMeta.setDisplayName(normalMeta.getDisplayName().replace("<num>", ( allQuestsSize-playerFinishedQuests )+""));
           }else{
               normalMeta.setDisplayName(normalMeta.getDisplayName().replace("<num>", ( (questsPerAward*numOfTheItem)-playerFinishedQuests )+""));
           }
            normalMeta.setLocalizedName("lifes-shard-blocked");
            orginalItem.setItemMeta(normalMeta);


        }

        return orginalItem;
    }








}
