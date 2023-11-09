package me.trololo11.lifespluginseason3.utils;

import org.bukkit.entity.Player;

public class PlayerStats {

    public final Player owner;
    public int kills;
    public int lifesCrafted;
    public int revivesCrafted;
    public int revivedSomeone;
    public int allQuestCompleted;
    public int dailyQuestCompleted;
    public int weeklyQuestCompleted;
    public int cardQuestCompleted;
    public int goldLifesUsed;
    public int cardsUsed;
    public int weeklyShardReedemed;
    public int dailyShardsReedemed;


    public PlayerStats(Player owner, int kills, int lifesCrafted, int revivesCrafted, int revivedSomeone, int allQuestCompleted, int dailyQuestCompleted, int weeklyQuestCompleted,int cardQuestCompleted, int goldLifesUsed, int cardsUsed, int weeklyShardReedemed, int dailyShardsReedemed) {
        this.owner = owner;
        this.kills = kills;
        this.lifesCrafted = lifesCrafted;
        this.revivesCrafted = revivesCrafted;
        this.revivedSomeone = revivedSomeone;
        this.allQuestCompleted = allQuestCompleted;
        this.dailyQuestCompleted = dailyQuestCompleted;
        this.weeklyQuestCompleted = weeklyQuestCompleted;
        this.cardQuestCompleted = cardQuestCompleted;
        this.goldLifesUsed = goldLifesUsed;
        this.cardsUsed = cardsUsed;
        this.weeklyShardReedemed = weeklyShardReedemed;
        this.dailyShardsReedemed = dailyShardsReedemed;
    }

}
