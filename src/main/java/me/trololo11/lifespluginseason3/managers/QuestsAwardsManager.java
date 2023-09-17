package me.trololo11.lifespluginseason3.managers;

import me.trololo11.lifespluginseason3.utils.QuestType;
import org.bukkit.entity.Player;

import java.util.HashMap;

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

    public byte getMaxAmountOfAwards(QuestType questType){
        return numAwards.getOrDefault(questType, (byte) 1);
    }

    public void setAwardsTakenForPlayer(Player player, QuestType questType, byte howMuchTaken){
        numTakenAwardsPlayers.get(questType).put(player, howMuchTaken);
    }

    public byte getAwardsTakenForPlayer(Player player, QuestType questType){
        return numTakenAwardsPlayers.get(questType).getOrDefault(player, (byte) 0);
    }

    public void removePlayerFromAllTakenAwards(Player player){
        for(QuestType questType : QuestType.values()){
            numTakenAwardsPlayers.get(questType).remove(player);
        }
    }

}
