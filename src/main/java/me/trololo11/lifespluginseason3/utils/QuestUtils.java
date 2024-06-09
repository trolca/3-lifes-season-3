package me.trololo11.lifespluginseason3.utils;

import me.trololo11.lifespluginseason3.LifesPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class QuestUtils {

    private static final LifesPlugin plugin = LifesPlugin.getPlugin();

    /**
     * Gets the max amount of quests that the specified page can have.
     * @param questType The type of quest to get the count for.
     * @return The max amount of quests that the specified page can have.
     */
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

    /**
     * This function is responsible for creating the appropriate type
     * of item for a awards take menu
     * @param orginalItem The orginal item you want to modify
     * @param awardTaken The item to put if the award has been already taken by a player
     * @param normalMeta The item meta to put if the award cannot be taken by a player
     * @param takeMeta The item meta to put if the player can take this award but hasn't already
     * @param otherAwardsMeta The item meta to put if the player didnt take the awards before this awards
     * @param howMuchShouldTake How much awards should the player take based of it's finished quests
     * @param howManyTaken How much awards the player has already taken
     * @param allQuestsSize The amount of all quests in this type of quests
     * @param playerFinishedQuests How much quests has this player already finished
     * @param numOfTheItem The number of this award (The award number starts at 1)
     * @param maxAmountAwards The max amount of awards that a player can take (not count the "final" award (aka the card shard) )
     * @param questsPerAward How many quests the player has to do for 1 award
     * @return The appropriate item
     */
    public static ItemStack getAwardItem(ItemStack orginalItem,ItemStack awardTaken, ItemMeta normalMeta, ItemMeta takeMeta,ItemMeta otherAwardsMeta, int howMuchShouldTake, int howManyTaken, int allQuestsSize, int playerFinishedQuests, int numOfTheItem, int maxAmountAwards, int questsPerAward){

        if(howMuchShouldTake >= numOfTheItem && howManyTaken+1 == numOfTheItem ){
            orginalItem.setItemMeta(takeMeta);
        }else if(numOfTheItem > howManyTaken && howMuchShouldTake >= numOfTheItem){
            orginalItem.setItemMeta(otherAwardsMeta);
        } else if (howManyTaken >= numOfTheItem) {
            orginalItem = awardTaken;
        }else{

           if(numOfTheItem >= maxAmountAwards){
               normalMeta.setDisplayName(normalMeta.getDisplayName().replace("<num>", ( allQuestsSize-playerFinishedQuests )+""));
           }else{
               normalMeta.setDisplayName(normalMeta.getDisplayName().replace("<num>", ( (questsPerAward*numOfTheItem)-playerFinishedQuests )+""));
           }
            Utils.setPrivateName(normalMeta, "lifes-shard-blocked");
            orginalItem.setItemMeta(normalMeta);


        }


        return orginalItem;
    }








}
