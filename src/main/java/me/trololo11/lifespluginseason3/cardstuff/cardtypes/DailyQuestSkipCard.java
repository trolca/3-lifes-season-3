package me.trololo11.lifespluginseason3.cardstuff.cardtypes;

import me.trololo11.lifespluginseason3.cardstuff.CardClass;
import me.trololo11.lifespluginseason3.cardstuff.CardType;
import org.bukkit.ChatColor;

public class DailyQuestSkipCard extends CardClass {


    @Override
    protected void createDescription() {
        description.add(ChatColor.WHITE + "Skip a daily quest!");
    }

    @Override
    public String getName() {
        return "&c&lSkip daily";
    }

    @Override
    protected int getCustomModelData() {
        return 23123;
    }

    @Override
    public CardType getCardType() {
        return CardType.DAILY_SKIP;
    }

    @Override
    public int getPercentageChance() {
        return 30;
    }
}
