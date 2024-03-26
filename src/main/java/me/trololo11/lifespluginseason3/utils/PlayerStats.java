package me.trololo11.lifespluginseason3.utils;

import org.bukkit.entity.Player;

/**
 * The stats of a player which are stored in this class.
 * Most of these stats are updated in the listeners. <br>
 * The stats stored:
 * <ul>
 *     <li>How much player has this player killed.</li>
 *     <li>How many lives has this player crafted</li>
 *     <li>How many revive cards has this player crafted</li>
 *     <li>How manu times has this player revived someone</li>
 *     <li>The total amount of quests this player has completed</li>
 *     <li>The total amount of daily quests this player has completed</li>
 *     <li>The total amount of weekly quests this player has completed</li>
 *     <li>The total amount of card quests this player has completed</li>
 *     <li>The amount of gold lives this player has used</li>
 *     <li>The total amount of weekly shards redeemed by this player</li>
 *     <li>The total amount of daily shards redeemed by this player</li>
 * </ul>
 */
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
    public int weeklyShardRedeemed;
    public int dailyShardsRedeemed;


    public PlayerStats(Player owner, int kills, int lifesCrafted, int revivesCrafted, int revivedSomeone, int allQuestCompleted, int dailyQuestCompleted, int weeklyQuestCompleted, int cardQuestCompleted, int goldLifesUsed, int cardsUsed, int weeklyShardRedeemed, int dailyShardsRedeemed) {
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
        this.weeklyShardRedeemed = weeklyShardRedeemed;
        this.dailyShardsRedeemed = dailyShardsRedeemed;
    }

}
