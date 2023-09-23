package me.trololo11.lifespluginseason3.managers;

import me.trololo11.lifespluginseason3.utils.QuestType;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * This class is responsible for storing how much awards have players taken. <br>
 * It also specifies the max amount of awards for one quest type
 */
public class QuestsAwardsManager {

    private final HashMap<QuestType, Byte> numAwards = new HashMap<>();

    private final HashMap<QuestType, HashMap<Player, Byte>> numTakenAwardsPlayers = new HashMap<>();

    public QuestsAwardsManager(){
        numAwards.put(QuestType.DAILY, (byte) 3);
        numAwards.put(QuestType.WEEKLY, (byte) 4);
        numAwards.put(QuestType.CARD, (byte) 4);

        for(QuestType questType : QuestType.values()){
            numTakenAwardsPlayers.put(questType, new HashMap<>());
        }
    }

    /**
     * Gets how much awards there are in a specified {@link QuestType}
     * (This also counts the final award aka the card shard)
     * @param questType The questType to check
     * @return The amount of awards
     */
    public byte getMaxAmountOfAwards(QuestType questType){
        return numAwards.getOrDefault(questType, (byte) 1);
    }

    /**
     * Sets how much awards a player has already taken for a specified {@link QuestType}
     * @param player The player to set the awards to
     * @param questType The questType to set the new awards to
     * @param howMuchTaken The new amount of awards to set
     */
    public void setAwardsTakenForPlayer(Player player, QuestType questType, byte howMuchTaken){
        numTakenAwardsPlayers.get(questType).put(player, howMuchTaken);
    }

    /**
     * Gets how many awards a specified player has already taken
     * for the {@link QuestType} specified
     * @param player The player to get
     * @param questType The {@link QuestType} to check
     * @return The amount of awards
     */
    public byte getAwardsTakenForPlayer(Player player, QuestType questType){
        return numTakenAwardsPlayers.get(questType).getOrDefault(player, (byte) 0);
    }

    /**
     * Removes the player from the taken awards hashMap <br>
     * Should be used when a player leaves
     * @param player The player to remove
     */
    public void removePlayerFromAllTakenAwards(Player player){
        for(QuestType questType : QuestType.values()){
            numTakenAwardsPlayers.get(questType).remove(player);
        }
    }

}
